package br.com.testmagnumbk.magnum_bk_teste.v1.domain.repository;

import br.com.testmagnumbk.magnum_bk_teste.v1.domain.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {
    List<Veiculo> findByMarcaIdMarca(UUID idMarca);
}
