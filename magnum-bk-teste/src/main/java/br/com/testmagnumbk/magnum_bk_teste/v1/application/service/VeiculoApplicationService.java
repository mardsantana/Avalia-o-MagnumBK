package br.com.testmagnumbk.magnum_bk_teste.v1.application.service;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.records.AtualizaVeiculoRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.MarcaResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.VeiculoResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.model.Marca;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.model.Veiculo;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.repository.MarcaRepository;
import br.com.testmagnumbk.magnum_bk_teste.v1.domain.repository.VeiculoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class VeiculoApplicationService implements VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final MarcaRepository marcaRepository;

    @Override
    @Cacheable(cacheNames = "marcasCache")
    public List<MarcaResponse> listarMarcas() {
        log.info("[start] VeiculoApplicationService - listarMarcas");
        List<Marca> marcas = marcaRepository.findAll();
        log.info("[finish] VeiculoApplicationService - listarMarcas");
        return marcas.stream()
                .map(m -> new MarcaResponse(m.getIdMarca(), m.getNome()))
                .toList();
    }

    @Override
    @Cacheable(value = "veiculosCache", key = "#idMarca")
    public List<VeiculoResponse> listarPorMarca(UUID idMarca) {
        log.info("[start] VeiculoApplicationService - listarPorMarca {}", idMarca);
        List<Veiculo> veiculos = veiculoRepository.findByMarcaIdMarca(idMarca);
        log.info("[finish] VeiculoApplicationService - listarPorMarca {}", idMarca);
        return veiculos.stream()
                .map(v -> new VeiculoResponse(
                        v.getIdVeiculo(),
                        v.getCodigo(),
                        v.getModelo(),
                        v.getObservacoes(),
                        v.getMarca().getIdMarca()))
                .toList();
    }

    @Override
    @CacheEvict(cacheNames = {"veiculosCache", "marcasCache"}, allEntries = true)
    public VeiculoResponse atualizar(UUID idVeiculo, AtualizaVeiculoRequest request) {
        log.info("[start] VeiculoApplicationService - atualizar {}", idVeiculo);

        Veiculo veiculo = veiculoRepository.findById(idVeiculo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Veículo não encontrado"));

        veiculo.atualizar(request);
        veiculoRepository.save(veiculo);

        log.info("[finish] VeiculoApplicationService - atualizar {}", idVeiculo);
        return new VeiculoResponse(
                veiculo.getIdVeiculo(),
                veiculo.getCodigo(),
                veiculo.getModelo(),
                veiculo.getObservacoes(),
                veiculo.getMarca().getIdMarca());
    }
}