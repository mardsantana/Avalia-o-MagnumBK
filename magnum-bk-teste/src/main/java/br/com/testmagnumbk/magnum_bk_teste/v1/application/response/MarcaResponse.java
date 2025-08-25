package br.com.testmagnumbk.magnum_bk_teste.v1.application.response;


import java.util.UUID;

public record MarcaResponse(
        UUID idMarca,
        String nome
) {}
