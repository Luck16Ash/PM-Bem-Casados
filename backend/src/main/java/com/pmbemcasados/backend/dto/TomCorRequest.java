package com.pmbemcasados.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record TomCorRequest(

        @NotBlank String nome

) {
}