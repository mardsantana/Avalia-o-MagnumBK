package br.com.magnumbk2.magnum_bk_t2.v2;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {}
