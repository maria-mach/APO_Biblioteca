-- Carga na tabela configuracoes_globais
INSERT INTO configuracoes_globais (multa_diaria, limite_renovacoes) 
VALUES (1.50, 3);

-- Carga na tabela usuarios
-- As senhas inseridas representam hashes de teste fictícios
INSERT INTO usuarios (nome, email, senha, token_ativacao, ativo, tipo_usuario) VALUES 
('Renato Silva (Administrador)', 'admin@biblioteca.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', NULL, TRUE, 'ADMIN'),
('Mariana Souza (Leitora)', 'mariana@email.com', 'daf6669f00f3e4a88c264cf0de5928f81b42bcd292e3ca6737757ed32d996f98', NULL, TRUE, 'CLIENTE'),
('Carlos Santos (Leitor Pendente)', 'carlos@email.com', 'ac9c2c34c9f7ad52528c3422af40a66e2e24aaf2a727831255413c9470158984', 'TOKEN123XYZ', FALSE, 'CLIENTE');

-- Carga na tabela livros (Dados essenciais do acervo)
INSERT INTO livros (isbn, titulo, autor, qtd_total, qtd_disponivel) VALUES 
('9788575226315', 'Código Limpo: Habilidades Práticas do Agile Software', 'Robert C. Martin', 4, 3),
('9788575227244', 'Arquitetura Limpa: O Guia do Artesão para Estrutura e Design', 'Robert C. Martin', 3, 3),
('9788573076103', 'Padrões de Projetos: Soluções Reutilizáveis de Software', 'Erich Gamma', 2, 0);

-- Carga na tabela campos_marc (Metadados MARC21 híbridos chave-valor para os livros acima)
INSERT INTO campos_marc (livro_id, tag, valor) VALUES 
-- Metadados de 'Código Limpo' (ID 1)
(1, '020', '9788575226315'), -- ISBN
(1, '100', 'Martin, Robert C.'), -- Autor Principal
(1, '245', 'Código Limpo: Habilidades Práticas do Agile Software / Robert C. Martin; tradução de Samantha Batista.'), -- Título Completo e Responsabilidade
(1, '250', '1ª ed.'), -- Edição
(1, '260', 'Novatec Editora, 2018.'), -- Publicação
(1, '650', 'Engenharia de Software'), -- Assunto
(1, '650', 'Desenvolvimento Ágil (Computação)'), -- Assunto Secundário

-- Metadados de 'Arquitetura Limpa' (ID 2)
(2, '020', '9788575227244'),
(2, '100', 'Martin, Robert C.'),
(2, '245', 'Arquitetura Limpa: O Guia do Artesão para Estrutura e Design de Software.'),
(2, '650', 'Arquitetura de Computadores / Sistemas'),
(2, '500', 'Inclui bibliografia e índice.'), -- Notas Gerais

-- Metadados de 'Padrões de Projetos' (ID 3)
(3, '020', '9788573076103'),
(3, '100', 'Gamma, Erich'),
(3, '245', 'Padrões de Projetos: Soluções Reutilizáveis de Software Orientado a Objetos.'),
(3, '700', 'Helm, Richard'), -- Coautor
(3, '700', 'Johnson, Ralph'), -- Coautor
(3, '700', 'Vlissides, John'), -- Coautor (Membros do GoF)
(3, '650', 'Programação Orientada a Objetos');

-- Carga na tabela emprestimos
-- Cenário 1: Empréstimo Finalizado sem multa
INSERT INTO emprestimos (usuario_id, livro_id, status, data_emprestimo, data_devolucao_prevista, data_devolucao_efetiva, multa_acumulada, renovacoes_decorrentes) 
VALUES (2, 1, 'DEVOLVIDO', '2026-05-01', '2026-05-08', '2026-05-07', 0.00, 0);

-- Cenário 2: Empréstimo Ativo em dia
INSERT INTO emprestimos (usuario_id, livro_id, status, data_emprestimo, data_devolucao_prevista, data_devolucao_efetiva, multa_acumulada, renovacoes_decorrentes) 
VALUES (2, 1, 'ATIVO', '2026-06-10', '2026-06-17', NULL, 0.00, 0);

-- Cenário 3: Empréstimo Ativo Atrasado (data atual de teste considerada como 2026-06-11)
-- Data prevista era 2026-06-05 (6 dias de atraso. Multa acumulada = 6 * 1.50 = 9.00)
INSERT INTO emprestimos (usuario_id, livro_id, status, data_emprestimo, data_devolucao_prevista, data_devolucao_efetiva, multa_acumulada, renovacoes_decorrentes) 
VALUES (2, 3, 'ATIVO', '2026-05-29', '2026-06-05', NULL, 9.00, 1);

-- Carga na tabela listas_leitura
INSERT INTO listas_leitura (usuario_id, nome) VALUES 
(2, 'Leituras de Engenharia 2026');

-- Carga na tabela pivot listas_leitura_livros
INSERT INTO listas_leitura_livros (lista_leitura_id, livro_id) VALUES 
(1, 1),
(1, 2);
