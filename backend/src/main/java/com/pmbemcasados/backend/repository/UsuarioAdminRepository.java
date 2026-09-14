package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.UsuarioAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioAdminRepository
        extends JpaRepository<UsuarioAdmin, UUID> {

    Optional<UsuarioAdmin> findByEmail(String email);
}