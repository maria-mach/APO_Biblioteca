-- Criação do banco de dados (Opcional, descomente se necessário)
-- CREATE DATABASE IF NOT EXISTS biblioteca_marc21 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE biblioteca_marc21;

-- -----------------------------------------------------------------------------
-- Tabela: usuarios
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS listas_leitura_livros;
DROP TABLE IF EXISTS listas_leitura;
DROP TABLE IF EXISTS campos_marc;
DROP TABLE IF EXISTS emprestimos;
DROP TABLE IF EXISTS livros;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS configuracoes_globais;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    token_ativacao VARCHAR(100) NULL,
    ativo BOOLEAN DEFAULT FALSE,
    tipo_usuario ENUM('ADMIN', 'CLIENTE') NOT NULL,
    
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: livros
-- -----------------------------------------------------------------------------
CREATE TABLE livros (
    id INT AUTO_INCREMENT,
    isbn VARCHAR(20) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    qtd_total INT NOT NULL DEFAULT 1,
    qtd_disponivel INT NOT NULL DEFAULT 1,
    
    CONSTRAINT pk_livros PRIMARY KEY (id),
    CONSTRAINT uk_livros_isbn UNIQUE (isbn),
    CONSTRAINT chk_qtd_total_positiva CHECK (qtd_total >= 0),
    CONSTRAINT chk_qtd_disponivel_positiva CHECK (qtd_disponivel >= 0),
    CONSTRAINT chk_estoque_coerente CHECK (qtd_disponivel <= qtd_total)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: campos_marc
-- -----------------------------------------------------------------------------
CREATE TABLE campos_marc (
    id INT AUTO_INCREMENT,
    livro_id INT NOT NULL,
    tag VARCHAR(3) NOT NULL,
    valor TEXT NOT NULL,
    
    CONSTRAINT pk_campos_marc PRIMARY KEY (id),
    CONSTRAINT fk_marc_livros FOREIGN KEY (livro_id) 
        REFERENCES livros (id) 
        ON DELETE CASCADE,
    CONSTRAINT chk_tag_formato CHECK (tag REGEXP '^[0-9]{3}$')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: emprestimos
-- -----------------------------------------------------------------------------
CREATE TABLE emprestimos (
    id INT AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    livro_id INT NOT NULL,
    status ENUM('SOLICITADO', 'ATIVO', 'DEVOLVIDO', 'RECUSADO') NOT NULL DEFAULT 'ATIVO',
    data_emprestimo DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao_efetiva DATE NULL,
    multa_acumulada DECIMAL(10, 2) DEFAULT 0.00,
    renovacoes_decorrentes INT DEFAULT 0,
    
    CONSTRAINT pk_emprestimos PRIMARY KEY (id),
    CONSTRAINT fk_emprestimos_usuarios FOREIGN KEY (usuario_id) 
        REFERENCES usuarios (id) 
        ON DELETE RESTRICT,
    CONSTRAINT fk_emprestimos_livros FOREIGN KEY (livro_id) 
        REFERENCES livros (id) 
        ON DELETE RESTRICT,
    CONSTRAINT chk_data_previsao CHECK (data_devolucao_prevista >= data_emprestimo),
    CONSTRAINT chk_data_efetiva CHECK (data_devolucao_efetiva IS NULL OR data_devolucao_efetiva >= data_emprestimo),
    CONSTRAINT chk_multa_positiva CHECK (multa_acumulada >= 0.00),
    CONSTRAINT chk_renovacoes_positivas CHECK (renovacoes_decorrentes >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: listas_leitura
-- -----------------------------------------------------------------------------
CREATE TABLE listas_leitura (
    id INT AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_listas_leitura PRIMARY KEY (id),
    CONSTRAINT fk_listas_usuarios FOREIGN KEY (usuario_id) 
        REFERENCES usuarios (id) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: listas_leitura_livros
-- -----------------------------------------------------------------------------
CREATE TABLE listas_leitura_livros (
    lista_leitura_id INT NOT NULL,
    livro_id INT NOT NULL,
    
    CONSTRAINT pk_listas_livros PRIMARY KEY (lista_leitura_id, livro_id),
    CONSTRAINT fk_listas_leitura FOREIGN KEY (lista_leitura_id) 
        REFERENCES listas_leitura (id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_livros_listas FOREIGN KEY (livro_id) 
        REFERENCES livros (id) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: configuracoes_globais
-- -----------------------------------------------------------------------------
CREATE TABLE configuracoes_globais (
    id INT AUTO_INCREMENT,
    multa_diaria DECIMAL(10, 2) NOT NULL DEFAULT 1.00,
    limite_renovacoes INT NOT NULL DEFAULT 3,
    
    CONSTRAINT pk_configuracoes PRIMARY KEY (id),
    CONSTRAINT chk_multa_config CHECK (multa_diaria >= 0.00),
    CONSTRAINT chk_limite_renovacoes CHECK (limite_renovacoes >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Criação de Índices para Otimização
-- -----------------------------------------------------------------------------
-- Índice composto para acelerar buscas de login
CREATE INDEX idx_usuarios_login ON usuarios (email, senha);

-- Índice composto para busca textual rápida no acervo por Título e Autor
CREATE INDEX idx_livros_busca_rapida ON livros (titulo, autor);

-- Índice para listar rapidamente solicitações pendentes e empréstimos ativos
CREATE INDEX idx_emprestimos_status ON emprestimos (status);

-- Índice parcial na tabela satélite MARC21 para acelerar buscas chave-valor
CREATE INDEX idx_marc_tag_valor ON campos_marc (tag, valor(50));
