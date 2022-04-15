package com.storeum.repository;

import com.storeum.model.entity.OAuthExchangeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthExchangeTokenRepository extends JpaRepository<OAuthExchangeToken, Long> {

    Optional<OAuthExchangeToken> findByToken(String token);
}
