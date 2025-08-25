package br.com.magnumbk2.magnum_bk_t2.integration;

import br.com.magnumbk2.magnum_bk_t2.MagnumBkT2Application;
import br.com.magnumbk2.magnum_bk_t2.v2.FipeClient;
import br.com.magnumbk2.magnum_bk_t2.v2.MarcaRepository;
import br.com.magnumbk2.magnum_bk_t2.v2.VeiculoRepository;
import br.com.magnumbk2.magnum_bk_t2.v2.VeiculoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = MagnumBkT2Application.class)
@ActiveProfiles("test")
@Testcontainers
class MarcaKafkaConsumerIntegrationTest {

    @Container
    static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.1"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private FipeClient fipeClient;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @BeforeEach
    void setup() {
        veiculoRepository.deleteAll();
        marcaRepository.deleteAll();
    }

    @Test
    void consumerDeveProcessarMensagemEsalvarDadosNoBanco() {
        // Arrange: Simular a resposta da API da Fipe
        Integer codigoMarcaFiat = 21;
        String nomeMarcaFiat = "Fiat";
        List<VeiculoDTO> veiculosMock = List.of(
                new VeiculoDTO("250", "Palio 1.0", null),
                new VeiculoDTO("251", "Uno Mille", "Observacoes Teste")
        );
        when(fipeClient.buscarVeiculosPorMarca(eq(codigoMarcaFiat))).thenReturn(veiculosMock);

        // Arrange: Criar a mensagem que será enviada para o Kafka
        Map<String, Object> payload = Map.of(
                "codigo", codigoMarcaFiat,
                "nome", nomeMarcaFiat
        );

        // Act: Enviar a mensagem para o tópico do Kafka.
        kafkaTemplate.send("marcas-topic", payload);

        // Aguardar o consumidor processar a mensagem.
        // Espera de forma inteligente até que o número de veículos seja 2
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> veiculoRepository.count() == 2);

        // Assert: Verificar se a marca e os veículos foram salvos no banco de dados.
        var marcaSalva = marcaRepository.findByCodigoFipe(codigoMarcaFiat);
        assertThat(marcaSalva).isPresent();
        assertThat(marcaSalva.get().getNome()).isEqualTo(nomeMarcaFiat);

        var veiculosSalvos = veiculoRepository.findAll();
        assertThat(veiculosSalvos).hasSize(2);
        assertThat(veiculosSalvos)
                .extracting(v -> v.getCodigo())
                .containsExactlyInAnyOrder("250", "251");

        assertThat(veiculosSalvos)
                .extracting(v -> v.getModelo())
                .containsExactlyInAnyOrder("Palio 1.0", "Uno Mille");
    }
}