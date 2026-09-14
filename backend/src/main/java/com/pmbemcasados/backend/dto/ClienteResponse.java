package com.pmbemcasados.backend.dto;

import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nome,
        String whatsapp,
        String email) {
}