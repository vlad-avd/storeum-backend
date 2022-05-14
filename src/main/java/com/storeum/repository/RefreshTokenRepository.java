package com.storeum.repository;

import com.storeum.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @EntityGraph(attributePaths = {"user"})
    Optional<RefreshToken> deleteByToken(String token);
}
