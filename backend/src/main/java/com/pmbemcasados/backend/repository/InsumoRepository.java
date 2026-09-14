package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InsumoRepository extends JpaRepository<Insumo, UUID> {
}