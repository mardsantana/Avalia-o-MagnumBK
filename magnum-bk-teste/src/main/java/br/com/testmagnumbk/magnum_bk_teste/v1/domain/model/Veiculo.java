package br.com.testmagnumbk.magnum_bk_teste.v1.domain.model;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.records.AtualizaVeiculoRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idVeiculo;

    private String codigo;
    private String modelo;

    @Column(length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    public void atualizar(AtualizaVeiculoRequest request) {
        this.modelo = request.modelo();
        this.observacoes = request.observacoes();
    }

}
