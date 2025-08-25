package br.com.testmagnumbk.magnum_bk_teste.unitmagnum;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.service.CargaInicialApplicationService;
import br.com.testmagnumbk.magnum_bk_teste.v1.infra.fipe.FipeClient;
import br.com.testmagnumbk.magnum_bk_teste.v1.infra.messaging.KafkaMarcaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class CargaInicialApplicationServiceTest {

    @Mock
    private FipeClient fipeClient;

    @Mock
    private KafkaMarcaProducer kafkaMarcaProducer;

    @InjectMocks
    private CargaInicialApplicationService cargaInicialApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void executarCargaInicial_deveBuscarMarcasEEnviarParaKafka() {
        // Arrange
        List<Map<String, Object>> marcas = List.of(
                Map.of("codigo", "1", "nome", "FIAT"),
                Map.of("codigo", "2", "nome", "FORD")
        );
        when(fipeClient.buscarMarcas()).thenReturn(marcas);
        // Act
        cargaInicialApplicationService.executarCargaInicial();
        // Assert
        verify(fipeClient, times(1)).buscarMarcas();
        verify(kafkaMarcaProducer, times(1)).enviarMarca(marcas.get(0));
        verify(kafkaMarcaProducer, times(1)).enviarMarca(marcas.get(1));
    }

    @Test
    void executarCargaInicial_quandoNaoExistemMarcas_naoDeveEnviarParaKafka() {
        // Arrange
        when(fipeClient.buscarMarcas()).thenReturn(List.of());
        // Act
        cargaInicialApplicationService.executarCargaInicial();
        // Assert
        verify(fipeClient, times(1)).buscarMarcas();
        verifyNoInteractions(kafkaMarcaProducer);
    }
}
