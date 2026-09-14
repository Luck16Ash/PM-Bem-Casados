package com.pmbemcasados.backend.dto;

import java.util.UUID;

public record TomCorResponse(
        UUID id,
        String nome,
        Boolean ativo) {
}