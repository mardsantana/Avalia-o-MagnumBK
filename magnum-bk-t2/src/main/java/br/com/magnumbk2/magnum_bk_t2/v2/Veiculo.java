package br.com.magnumbk2.magnum_bk_t2.v2;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Veiculo {

    @Id
    @GeneratedValue
    private UUID idVeiculo;

    private String codigo;
    private String modelo;

    @Column(length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;
}
