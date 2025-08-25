package br.com.testmagnumbk.magnum_bk_teste.v1.application.service;

import br.com.testmagnumbk.magnum_bk_teste.v1.infra.fipe.FipeClient;
import br.com.testmagnumbk.magnum_bk_teste.v1.infra.messaging.KafkaMarcaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CargaInicialApplicationService implements CargaInicialService{

    private final FipeClient fipeClient;
    private final KafkaMarcaProducer kafkaMarcaProducer;

    public void executarCargaInicial() {
        log.info("Iniciando carga inicial de marcas da FIPE...");
        List<Map<String, Object>> marcas = fipeClient.buscarMarcas();
        log.info("Total de {} marcas encontradas na FIPE", marcas.size());
        marcas.forEach(marca -> {
            log.info("Enviando marca para Kafka -> {}", marca);
            kafkaMarcaProducer.enviarMarca(marca);
        });
        log.info("Carga inicial finalizada com sucesso.");
    }
}
