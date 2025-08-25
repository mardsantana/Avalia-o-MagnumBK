package br.com.testmagnumbk.magnum_bk_teste.integration;

import br.com.testmagnumbk.magnum_bk_teste.MagnumBkTesteApplication;
import br.com.testmagnumbk.magnum_bk_teste.v1.infra.fipe.FipeClient;
import br.com.testmagnumbk.magnum_bk_teste.v1.infra.messaging.KafkaMarcaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MagnumBkTesteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CargaInicialControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private FipeClient fipeClient;

    @MockBean
    private KafkaMarcaProducer kafkaMarcaProducer;

    private HttpHeaders headers;

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private String obterTokenJwt() {
        // Endereço do endpoint de login.
        String urlLogin = "http://localhost:" + port + "/v1/api/auth/login";

        // Credenciais de login.
        Map<String, String> loginCredentials = Map.of("username", "Mardson Santana", "password", "12345678");

        // Faz a requisição de login.
        ResponseEntity<Map> response = restTemplate.postForEntity(urlLogin, loginCredentials, Map.class);

        // Verifica se o login foi bem-sucedido e extrai o token.
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("token");
        }
        throw new IllegalStateException("Falha ao obter o token JWT. Status: " + response.getStatusCode());
    }

    @Test
    void cargaInicial_deveChamarFipeClientEEnviarParaKafka() {
        // Obter o token JWT antes de fazer a requisição do teste.
        String jwtToken = obterTokenJwt();
        headers.setBearerAuth(jwtToken);


        // Arrange: mock das marcas
        Map<String, Object> marca1 = Map.of("nome", "FIAT");
        Map<String, Object> marca2 = Map.of("nome", "FORD");
        List<Map<String, Object>> marcasMock = List.of(marca1, marca2);

        when(fipeClient.buscarMarcas()).thenReturn(marcasMock);

        // Act: chama o endpoint com autenticação
        String url = "http://localhost:" + port + "/v1/api/veiculos/carga-inicial";
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Assert: status 200 e corpo correto
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo("Carga inicial enviada para processamento.");

        // Verifica se cada marca foi enviada para Kafka
        verify(kafkaMarcaProducer, times(1)).enviarProdutoParaFila(argThat(m -> "FIAT".equals(m.get("nome"))));
        verify(kafkaMarcaProducer, times(1)).enviarProdutoParaFila(argThat(m -> "FORD".equals(m.get("nome"))));

        // Verifica que o FipeClient foi chamado
        verify(fipeClient, times(1)).buscarMarcas();
    }
}