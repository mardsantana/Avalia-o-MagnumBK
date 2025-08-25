package br.com.magnumbk2.magnum_bk_t2.v2;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MarcaRepository extends JpaRepository<Marca, UUID> {
    Optional<Marca> findByCodigoFipe(Integer codigoFipe);
}
