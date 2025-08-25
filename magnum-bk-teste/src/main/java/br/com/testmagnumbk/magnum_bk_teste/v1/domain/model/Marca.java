package br.com.testmagnumbk.magnum_bk_teste.v1.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idMarca;

    @Column(nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Veiculo> veiculos = new ArrayList<>();
}
