package br.com.magnumbk2.magnum_bk_t2.v2;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue
    private UUID idMarca;

    @Column(nullable = false, unique = true)
    private String nome;

    private Integer codigoFipe;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Veiculo> veiculos = new ArrayList<>();

    public void adicionarVeiculo(Veiculo veiculo) {
        veiculos.add(veiculo);
        veiculo.setMarca(this);
    }
}
