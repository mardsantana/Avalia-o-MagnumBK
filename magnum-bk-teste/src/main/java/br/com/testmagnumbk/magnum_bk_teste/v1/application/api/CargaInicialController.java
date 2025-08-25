package br.com.testmagnumbk.magnum_bk_teste.v1.application.api;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.service.CargaInicialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/veiculos")
@RequiredArgsConstructor
@Slf4j
public class CargaInicialController {

    private final CargaInicialService cargaInicialService;

    @PostMapping("/carga-inicial")
    public ResponseEntity<String> cargaInicial() {
        log.info("Requisição recebida para carga inicial de marcas FIPE");
        cargaInicialService.executarCargaInicial();
        return ResponseEntity.ok("Carga inicial enviada para processamento.");
    }
}
