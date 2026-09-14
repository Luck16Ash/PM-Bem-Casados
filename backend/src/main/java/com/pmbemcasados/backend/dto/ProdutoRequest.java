package com.pmbemcasados.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoRequest(

        @NotBlank String nome,

        String categoria,

        @NotNull Boolean personalizado,

        @NotNull @PositiveOrZero BigDecimal precoUnitario,

        String sabor,

        String papelEmbalagem,

        UUID tomCorId,

        String fotoUrl

) {
}