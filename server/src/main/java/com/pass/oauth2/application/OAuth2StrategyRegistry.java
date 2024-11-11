package com.pass.oauth2.application;

import static java.util.stream.Collectors.toMap;

import com.pass.oauth2.domain.OAuth2Provider;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class OAuth2StrategyRegistry {

    private final Map<OAuth2Provider, OAuth2Strategy> strategies;

    public OAuth2StrategyRegistry(Set<OAuth2Strategy> strategies) {
        this.strategies = strategies.stream()
                .collect(toMap(OAuth2Strategy::getProvider, Function.identity()));
    }

    public Optional<OAuth2Strategy> getOAuth2Strategy(OAuth2Provider provider) {
        return Optional.ofNullable(strategies.get(provider));
    }
}