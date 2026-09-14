package com.pmbemcasados.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record InsumoRequest(

        @NotBlank String nome,

        @NotBlank String unidadeMedida,

        @NotNull @PositiveOrZero BigDecimal quantidadeMinima

) {
}