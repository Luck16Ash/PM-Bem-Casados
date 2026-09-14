package com.pmbemcasados.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PedidoRequest(

        @NotNull UUID clienteId,

        String tipoEvento,

        @NotNull LocalDate dataEventoSolicitada,

        String localCep,

        String localCidade,

        String localBairro,

        String localLogradouro,

        String localNumero,

        String localComplemento,

        @NotNull Boolean consentimentoLgpd,

        @NotEmpty List<@Valid ItemPedidoRequest> itens

) {
}