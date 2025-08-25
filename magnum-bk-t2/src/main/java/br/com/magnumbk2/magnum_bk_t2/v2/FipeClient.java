package br.com.magnumbk2.magnum_bk_t2.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class FipeClient {


    private static final String URL_FIPE_BASE = "https://parallelum.com.br/fipe/api/v1/carros/marcas";
    private static final long DELAY_MS = 500;

    private final RestTemplate restTemplate;
    private final Map<Integer, List<VeiculoDTO>> cacheVeiculos = new ConcurrentHashMap<>();

    public FipeClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<MarcaDTO> buscarMarcas() {
        String url = URL_FIPE_BASE + "/marcas";
        try {
            Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);
            List<MarcaDTO> marcas = new ArrayList<>();
            if (response != null) {
                for (Map<String, Object> m : response) {
                    Object codigo = m.get("codigo");
                    Object nome = m.get("nome");
                    if (codigo != null && nome != null) {
                        marcas.add(new MarcaDTO(Integer.parseInt(codigo.toString()), nome.toString()));
                    }
                }
            }
            log.info("marcas carregadas da FIPE", marcas.size());
            return marcas;
        } catch (Exception e) {
            log.error("Erro ao buscar marcas FIPE: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    public List<VeiculoDTO> buscarVeiculosPorMarca(Integer codigoMarca) {
        if (cacheVeiculos.containsKey(codigoMarca)) return cacheVeiculos.get(codigoMarca);

        try {
            Thread.sleep(DELAY_MS);
            String url = UriComponentsBuilder
                    .fromHttpUrl(URL_FIPE_BASE)
                    .pathSegment(codigoMarca.toString(), "modelos")
                    .toUriString();

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null || !response.containsKey("modelos")) return Collections.emptyList();

            List<Map<String, Object>> modelos = (List<Map<String, Object>>) response.get("modelos");
            List<VeiculoDTO> veiculos = new ArrayList<>();
            for (Map<String, Object> m : modelos) {
                Object codigo = m.get("codigo");
                Object nome = m.get("nome");
                Object observacoes = m.get("observacoes");
                if (codigo != null && nome != null) {
                    String observacoesStr = observacoes != null ? observacoes.toString() : null;
                    veiculos.add(new VeiculoDTO(codigo.toString(), nome.toString(), observacoesStr));
                }
            }
            cacheVeiculos.put(codigoMarca, veiculos);
            log.info("{} veículos carregados para a marca {}", veiculos.size(), codigoMarca);
            return veiculos;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Marca {} não encontrada (404)", codigoMarca);
            } else {
                log.error("Erro HTTP FIPE marca {}: {} {}", codigoMarca, e.getStatusCode(), e.getStatusText());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Erro inesperado FIPE marca {}: {}", codigoMarca, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
