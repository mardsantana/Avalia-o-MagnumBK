package br.com.testmagnumbk.magnum_bk_teste.v1.infra.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMarcaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "marcas-topic";

    public void enviarProdutoParaFila(Map<String, Object> marca) {
        log.debug("Publicando no tópico [{}]: {}", TOPIC, marca);
        kafkaTemplate.send(TOPIC, marca);
    }
}

