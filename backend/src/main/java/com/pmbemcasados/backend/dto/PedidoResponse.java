package com.pmbemcasados.backend.dto;

import com.pmbemcasados.backend.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(

        UUID id,

        UUID clienteId,

        String clienteNome,

        String tipoEvento,

        LocalDate dataEventoSolicitada,

        LocalDate dataEventoConfirmada,

        StatusPedido status,

        BigDecimal valorEstimadoInicial,

        BigDecimal valorConfirmado,

        String localCep,

        String localCidade,

        String localBairro,

        String localLogradouro,

        String localNumero,

        String localComplemento,

        List<ItemPedidoResponse> itens,

        LocalDateTime criadoEm

) {
}