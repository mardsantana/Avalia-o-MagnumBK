package br.com.testmagnumbk.magnum_bk_teste.v1.application.service;

import br.com.testmagnumbk.magnum_bk_teste.v1.application.records.AtualizaVeiculoRequest;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.MarcaResponse;
import br.com.testmagnumbk.magnum_bk_teste.v1.application.response.VeiculoResponse;

import java.util.List;
import java.util.UUID;

public interface VeiculoService {
    List<MarcaResponse> listarMarcas();
    List<VeiculoResponse> listarPorMarca(UUID idMarca);
    VeiculoResponse atualizar(UUID idVeiculo, AtualizaVeiculoRequest request);
}
