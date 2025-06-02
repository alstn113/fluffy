package com.fluffy.global.web

import com.fluffy.auth.application.AuthService
import com.fluffy.auth.application.response.MyInfoResponse
import com.fluffy.global.exception.CoreException
import com.fluffy.global.exception.UnauthorizedException
import com.fluffy.global.web.cookie.CookieManager
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthArgumentResolver(
    private val cookieManager: CookieManager,
    private val authService: AuthService,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasAuthAnnotation = parameter.hasParameterAnnotation(Auth::class.java)
        val isAccessorClass = Accessor::class.java.isAssignableFrom(parameter.parameterType)

        return hasAuthAnnotation && isAccessorClass
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Accessor {
        val auth = requireNotNull(parameter.getParameterAnnotation(Auth::class.java))
        val response = requireNotNull(webRequest.getNativeResponse(HttpServletResponse::class.java))

        val token = extractTokenFromCookie(webRequest) ?: return handleNoToken(auth)
        return handleToken(token, response)
    }

    private fun extractTokenFromCookie(webRequest: NativeWebRequest): String? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java) ?: return null

        return cookieManager.extractAccessToken(request)
    }

    private fun handleNoToken(auth: Auth): Accessor {
        if (auth.required) {
            throw UnauthorizedException("권한을 위한 토큰이 필요합니다.")
        }

        return Accessor.GUEST
    }

    private fun handleToken(token: String, response: HttpServletResponse): Accessor {
        try {
            val memberId = authService.getMemberIdByToken(token)
            val myInfo: MyInfoResponse = authService.getMyInfo(memberId)

            return Accessor(myInfo.id)
        } catch (e: CoreException) {
            clearAccessTokenCookie(response)

            throw UnauthorizedException("토큰이 유효하지 않습니다.", e)
        }
    }

    private fun clearAccessTokenCookie(response: HttpServletResponse) {
        val cookie = cookieManager.createExpiredAccessTokenCookie()

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }
}
