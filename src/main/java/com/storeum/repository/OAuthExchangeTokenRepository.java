package com.storeum.repository;

import com.storeum.model.entity.OAuthExchangeToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthExchangeTokenRepository extends CrudRepository<OAuthExchangeToken, Long> {

    Optional<OAuthExchangeToken> findByToken(String token);
}
