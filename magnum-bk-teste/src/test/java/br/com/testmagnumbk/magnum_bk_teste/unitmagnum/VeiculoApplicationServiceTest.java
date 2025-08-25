package br.com.testmagnumbk.magnum_bk_teste.unitmagnum;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.records.AtualizaVeiculoRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.MarcaResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.VeiculoResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.service.VeiculoApplicationService;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.model.Marca;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.model.Veiculo;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.repository.MarcaRepository;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class VeiculoApplicationServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private VeiculoApplicationService veiculoApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarMarcas_deveRetornarListaDeMarcaResponse() {
        // Arrange
        Marca marca1 = Marca.builder()
                .idMarca(UUID.randomUUID())
                .nome("FIAT")
                .build();

        Marca marca2 = Marca.builder()
                .idMarca(UUID.randomUUID())
                .nome("FORD")
                .build();

        when(marcaRepository.findAll()).thenReturn(List.of(marca1, marca2));
        // Act
        List<MarcaResponse> result = veiculoApplicationService.listarMarcas();
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).nome()).isEqualTo("FIAT");
        assertThat(result.get(1).nome()).isEqualTo("FORD");
        verify(marcaRepository, times(1)).findAll();
    }

    @Test
    void listarPorMarca_deveRetornarListaDeVeiculoResponse() {
        // Arrange
        UUID marcaId = UUID.randomUUID();
        Marca marca = Marca.builder()
                .idMarca(marcaId)
                .nome("FIAT")
                .build();

        Veiculo v1 = Veiculo.builder()
                .idVeiculo(UUID.randomUUID())
                .codigo("001")
                .modelo("UNO")
                .observacoes("Popular")
                .marca(marca)
                .build();

        Veiculo v2 = Veiculo.builder()
                .idVeiculo(UUID.randomUUID())
                .codigo("002")
                .modelo("ARGO")
                .observacoes("Completo")
                .marca(marca)
                .build();

        when(veiculoRepository.findByMarcaIdMarca(marcaId)).thenReturn(List.of(v1, v2));
        // Act
        List<VeiculoResponse> result = veiculoApplicationService.listarPorMarca(marcaId);
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).modelo()).isEqualTo("UNO");
        assertThat(result.get(1).modelo()).isEqualTo("ARGO");
        verify(veiculoRepository, times(1)).findByMarcaIdMarca(marcaId);
    }

    @Test
    void atualizar_quandoVeiculoExistir_deveAtualizarERetornarResponse() {
        // Arrange
        UUID veiculoId = UUID.randomUUID();
        UUID marcaId = UUID.randomUUID();

        Marca marca = Marca.builder()
                .idMarca(marcaId)
                .nome("FORD")
                .build();

        Veiculo veiculo = spy(Veiculo.builder()
                .idVeiculo(veiculoId)
                .codigo("123")
                .modelo("Focus")
                .observacoes("Sedan")
                .marca(marca)
                .build());

        AtualizaVeiculoRequest request = new AtualizaVeiculoRequest("Fusion",  "Sedan luxo");

        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(veiculo)).thenReturn(veiculo);
        // Act
        VeiculoResponse response = veiculoApplicationService.atualizar(veiculoId, request);
        // Assert
        assertThat(response.modelo()).isEqualTo("Fusion");
        assertThat(response.observacoes()).isEqualTo("Sedan luxo");
        verify(veiculo).atualizar(request);
        verify(veiculoRepository, times(1)).save(veiculo);
    }

    @Test
    void atualizar_quandoVeiculoNaoExistir_deveLancarExcecao() {
        // Arrange
        UUID veiculoId = UUID.randomUUID();
        AtualizaVeiculoRequest request = new AtualizaVeiculoRequest("Fusion",  "Sedan luxo");

        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.empty());
        // Assert
        assertThatThrownBy(() -> veiculoApplicationService.atualizar(veiculoId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("reason", "Veículo não encontrado")
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

        verify(veiculoRepository, never()).save(any());
    }
}
