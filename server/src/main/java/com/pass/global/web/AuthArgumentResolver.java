package com.pass.global.web;

import static java.util.Objects.requireNonNull;

import com.pass.auth.application.AuthService;
import com.pass.global.exception.BaseException;
import com.pass.global.web.cookie.CookieManager;
import com.pass.global.web.exception.TokenInvalidException;
import com.pass.global.web.exception.TokenRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final CookieManager cookieManager;
    private final AuthService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.hasParameterAnnotation(Auth.class);
        boolean isAccessorClass = Accessor.class.isAssignableFrom(parameter.getParameterType());

        return hasAuthAnnotation && isAccessorClass;
    }

    @Override
    public Accessor resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Auth auth = requireNonNull(parameter.getParameterAnnotation(Auth.class));
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        String token = extractTokenFromCookie(webRequest);
        if (token == null) {
            return handleNoToken(auth);
        }

        return handleToken(token, response);
    }

    private String extractTokenFromCookie(NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            return null;
        }

        return cookieManager.extractAccessToken(request);
    }

    private Accessor handleNoToken(Auth auth) {
        if (auth.required()) {
            throw new TokenRequiredException();
        }

        return Accessor.GUEST;
    }

    private Accessor handleToken(String token, HttpServletResponse response) {
        try {
            Long memberId = authService.getMemberIdByToken(token);

            return new Accessor(memberId);
        } catch (BaseException e) {
            clearAccessTokenCookie(response);

            throw new TokenInvalidException();
        }
    }

    private void clearAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = cookieManager.createExpiredAccessTokenCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}