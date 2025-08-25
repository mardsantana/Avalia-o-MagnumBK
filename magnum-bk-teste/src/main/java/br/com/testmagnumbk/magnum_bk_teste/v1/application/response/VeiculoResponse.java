package br.com.testmagnumbk.magnum_bk_teste.v1.application.response;

import java.util.UUID;

public record VeiculoResponse(
        UUID idVeiculo,
        String codigo,
        String modelo,
        String observacoes,
        UUID idMarca
) {}
