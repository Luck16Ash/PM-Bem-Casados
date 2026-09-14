package com.pmbemcasados.backend.repository;

import com.pmbemcasados.backend.domain.PedidoStatusHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoStatusHistoricoRepository
        extends JpaRepository<PedidoStatusHistorico, UUID> {

    List<PedidoStatusHistorico> findByPedidoIdOrderByAlteradoEmAsc(
            UUID pedidoId);
}