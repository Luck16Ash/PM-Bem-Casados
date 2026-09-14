package com.pmbemcasados.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FichaTecnicaId implements Serializable {

    @Column(name = "produto_id")
    private UUID produtoId;

    @Column(name = "insumo_id")
    private UUID insumoId;
}