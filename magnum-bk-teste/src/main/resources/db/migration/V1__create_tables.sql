-- Criação de extensões necessárias
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ========================
-- Tabela de Marcas
-- ========================
CREATE TABLE IF NOT EXISTS marca (
    id_marca UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ========================
-- Tabela de Veículos
-- ========================
CREATE TABLE IF NOT EXISTS veiculo (
    id_veiculo UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(255) UNIQUE,
    modelo VARCHAR(255) NOT NULL,
    observacoes VARCHAR(500),
    marca_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_veiculo_marca FOREIGN KEY (marca_id)
        REFERENCES marca (id_marca)
        ON DELETE CASCADE
);