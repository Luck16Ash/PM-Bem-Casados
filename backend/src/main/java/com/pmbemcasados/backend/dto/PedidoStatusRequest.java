package com.pmbemcasados.backend.dto;

import com.pmbemcasados.backend.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PedidoStatusRequest(

        @NotNull StatusPedido novoStatus,

        String motivo,

        UUID adminId,

        LocalDate dataEventoConfirmada

) {
}