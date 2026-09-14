package com.pmbemcasados.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(

        @NotBlank String nome,

        @NotBlank String whatsapp,

        String email,

        Boolean consentimentoLgpd

) {
}