-- Migração para bancos já criados antes do fluxo de aprovação de empréstimos.
-- Execute uma vez no MySQL/phpMyAdmin se você não recriar o banco do zero.

ALTER TABLE emprestimos
    ADD COLUMN status ENUM('SOLICITADO', 'ATIVO', 'DEVOLVIDO', 'RECUSADO') NOT NULL DEFAULT 'ATIVO'
    AFTER livro_id;

UPDATE emprestimos
SET status = 'DEVOLVIDO'
WHERE data_devolucao_efetiva IS NOT NULL;

UPDATE emprestimos
SET status = 'ATIVO'
WHERE data_devolucao_efetiva IS NULL;

CREATE INDEX idx_emprestimos_status ON emprestimos (status);
