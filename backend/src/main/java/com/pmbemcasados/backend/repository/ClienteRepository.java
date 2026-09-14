package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByWhatsapp(String whatsapp);

    Optional<Cliente> findByEmail(String email);
}