package com.pmbemcasados.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemPedidoRequest(

        @NotNull UUID produtoId,

        @NotNull @Min(1) Integer quantidade,

        String personalizacao

) {
}