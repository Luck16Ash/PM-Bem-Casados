package com.pmbemcasados.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tom_cor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TomCor {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        if (ativo == null) {
            ativo = true;
        }

        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}