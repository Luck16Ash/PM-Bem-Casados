package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.ClienteLoginToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteLoginTokenRepository
        extends JpaRepository<ClienteLoginToken, UUID> {

    Optional<ClienteLoginToken> findByToken(UUID token);
}