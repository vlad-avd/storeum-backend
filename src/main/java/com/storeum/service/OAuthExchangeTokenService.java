package com.storeum.service;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.entity.OAuthExchangeToken;
import com.storeum.model.entity.User;
import com.storeum.repository.OAuthExchangeTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthExchangeTokenService {

    private final OAuthExchangeTokenRepository repository;

    public OAuthExchangeToken getToken(String token) {
        log.info("Trying to get OAuth Exchange Token");
        return repository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException(String.format("OAuth Exchange Token, value=%s was not found in DB", token))
        );
    }

    public OAuthExchangeToken createToken(User user) {
        OAuthExchangeToken exchangeToken = OAuthExchangeToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .isExchanged(false)
                .build();

        repository.save(exchangeToken);
        log.info("OAuth Exchange Token was created");

        return exchangeToken;
    }

    public OAuthExchangeToken exchangeToken(String token) {
        OAuthExchangeToken oAuthExchangeToken = getToken(token);
        oAuthExchangeToken.setExchanged(true);
        OAuthExchangeToken exchangedToken = repository.save(oAuthExchangeToken);
        log.info("OAuth Exchange Token was exchanged");
        return exchangedToken;
    }
}
