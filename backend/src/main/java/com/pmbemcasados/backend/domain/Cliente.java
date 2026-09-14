package com.pmbemcasados.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String whatsapp;

    @Column(unique = true)
    private String email;

    @Column(name = "email_verificado_em")
    private LocalDateTime emailVerificadoEm;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "consentimento_lgpd_em", nullable = false)
    private LocalDateTime consentimentoLgpdEm;

    @PrePersist
    public void prePersist() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}