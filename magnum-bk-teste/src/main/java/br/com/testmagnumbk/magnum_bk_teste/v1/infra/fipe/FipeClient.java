package br.com.testmagnumbk.magnum_bk_teste.v1.infra.fipe;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.MarcaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Slf4j
public class FipeClient {

    private static final String URL_FIPE = "https://parallelum.com.br/fipe/api/v1/carros/marcas";

    public List<Map<String, Object>> buscarMarcas() {
        log.info("Consumindo API FIPE: {}", URL_FIPE);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object>[] response = restTemplate.getForObject(URL_FIPE, Map[].class);
        return Arrays.asList(response);
    }

}
