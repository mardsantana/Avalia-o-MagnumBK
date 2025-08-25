package br.com.magnumbk2.magnum_bk_t2.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarcaKafkaConsumer {

    private final MarcaService marcaService;

    @KafkaListener(topics = "marcas-topic", groupId = "api2-consumer-group")
    public void processarMensagemRecebida(Map<String, Object> payload) {
        try {
            log.info("Mensagem recebida do Kafka: {}", payload);

            Integer codigoFipe = Integer.parseInt(payload.get("codigo").toString());
            String nome = payload.get("nome").toString();

            MarcaDTO marcaDTO = new MarcaDTO(codigoFipe, nome);

            marcaService.processarMarca(marcaDTO);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem do Kafka: {}", e.getMessage(), e);
        }
    }
}