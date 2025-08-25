package br.com.testmagnumbk.magnum_bk_teste.v1.domain.repository;

import br.com.testmagnumbk.magnum_bk_teste.v1.domain.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MarcaRepository extends JpaRepository<Marca, UUID> {
}
