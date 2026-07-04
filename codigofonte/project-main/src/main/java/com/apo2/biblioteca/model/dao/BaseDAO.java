package com.apo2.biblioteca.model.dao;

import com.apo2.biblioteca.util.ConexaoFactory;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe base para todos os DAOs do sistema.
 * Provê métodos comuns de acesso e gerenciamento de conexões com o banco de dados.
 * 
 * @version 1.0
 */
public class BaseDAO {

    /**
     * Obtém uma conexão ativa com o banco de dados MySQL a partir da Connection Factory.
     * 
     * @return Connection ativa
     * @throws SQLException caso ocorra erro ao tentar se conectar ao banco de dados
     */
    protected Connection getConexao() throws SQLException {
        return ConexaoFactory.getConexao();
    }
}

