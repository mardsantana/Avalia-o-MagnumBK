package br.com.testmagnumbk.magnum_bk_teste.v1.application.api;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.records.AtualizaVeiculoRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.MarcaResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.VeiculoResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/veiculos")
@RequiredArgsConstructor
@Log4j2
public class VeiculoController {

    private final VeiculoService service;

    @GetMapping("/marcas")
    public ResponseEntity<List<MarcaResponse>> listarMarcas() {
        log.debug("[start] VeiculoController - listarMarcas");
        List<MarcaResponse> response = service.listarMarcas();
        log.debug("[finish] VeiculoController - listarMarcas");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idMarca}")
    public ResponseEntity<List<VeiculoResponse>> listarPorMarca(@PathVariable UUID idMarca) {
        log.debug("[start] VeiculoController - listarPorMarca {}", idMarca);
        List<VeiculoResponse> response = service.listarPorMarca(idMarca);
        log.debug("[finish] VeiculoController - listarPorMarca {}", idMarca);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idVeiculo}")
    public ResponseEntity<VeiculoResponse> atualizar(@Valid @PathVariable UUID idVeiculo,
                                                     @RequestBody AtualizaVeiculoRequest request) {
        log.debug("[start] VeiculoController - atualizar {}", idVeiculo);
        VeiculoResponse response = service.atualizar(idVeiculo, request);
        log.debug("[finish] VeiculoController - atualizar {}", idVeiculo);
        return ResponseEntity.ok(response);
    }
}
