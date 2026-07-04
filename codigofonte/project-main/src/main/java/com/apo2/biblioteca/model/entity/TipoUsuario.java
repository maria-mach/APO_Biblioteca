package com.apo2.biblioteca.model.entity;

/**
 * enum que define os tipos de usuario do sistema (ADMIN e CLIENTE)
 * utilizado para controle de perfis de acesso na aplicacao.
 * 
 * @version 1.0
 */
public enum TipoUsuario {
    /** Papel administrativo com acesso total ao gerenciamento do sistema. */
    ADMIN,
    
    /** Papel de cliente leitor com permissao de consulta e solicitacoes de emprestimo. */
    CLIENTE
}

