package br.com.magnumbk2.magnum_bk_t2.v2;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarcaApplicationService implements MarcaService {

    private final FipeClient fipeClient;
    private final VeiculoRepository veiculoRepository;
    private final MarcaRepository marcaRepository;

    @Override
    @Transactional
    public void processarMarca(MarcaDTO marcaDTO) {
        log.info("Processando marca: {} ({})", marcaDTO.nome(), marcaDTO.codigoFipe());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Erro ao esperar entre chamadas FIPE", e);
            return;
        }

        List<VeiculoDTO> veiculosDTO = fipeClient.buscarVeiculosPorMarca(marcaDTO.codigoFipe());
        
        log.info("{} veículos encontrados para marca {}", veiculosDTO.size(), marcaDTO.nome());

        Marca marcaEntity = marcaRepository.findByCodigoFipe(marcaDTO.codigoFipe())
                .orElseGet(() -> {
                    Marca novaMarca = Marca.builder()
                            .nome(marcaDTO.nome())
                            .codigoFipe(marcaDTO.codigoFipe())
                            .veiculos(new ArrayList<>())
                            .build();
                    log.info("Marca não encontrada. Criando nova: {}", marcaDTO.nome());
                    return marcaRepository.save(novaMarca);
                });

        List<Veiculo> veiculos = new ArrayList<>();
        for (VeiculoDTO v : veiculosDTO) {
            Veiculo veiculo = Veiculo.builder()
                    .codigo(v.codigo())
                    .modelo(v.modelo())
                    .observacoes(v.observacoes())
                    .marca(marcaEntity)
                    .build();
            veiculos.add(veiculo);
        }

        if (!veiculos.isEmpty()) {
            log.info("Salvando {} veículos no banco...", veiculos.size());
            List<Veiculo> veiculosSalvos = veiculoRepository.saveAll(veiculos);

            marcaEntity.getVeiculos().addAll(veiculosSalvos);
            marcaRepository.save(marcaEntity);
            
            log.info("{} veículos salvos para a marca {}", veiculosSalvos.size(), marcaDTO.nome());
        } else {
            log.warn("Nenhum veículo retornado para a marca {}", marcaDTO.nome());
        }

        log.info("Processamento finalizado para marca: {} ({})", marcaDTO.nome(), marcaDTO.codigoFipe());
    }
}
