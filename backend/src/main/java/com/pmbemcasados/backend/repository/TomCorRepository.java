package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.TomCor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TomCorRepository extends JpaRepository<TomCor, UUID> {

    List<TomCor> findByAtivoTrue();

    boolean existsByNomeIgnoreCase(String nome);
}