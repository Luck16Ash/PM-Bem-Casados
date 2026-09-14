package com.pmbemcasados.backend.domain;

import com.pmbemcasados.backend.domain.enums.RegiaoFornecedor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    private String bairro;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "regiao_fornecedor")
    private RegiaoFornecedor regiao;

    private String contato;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}