package com.apo2.biblioteca.model.dao;

import com.apo2.biblioteca.model.entity.ConfiguracaoGlobal;
// import com.apo2.biblioteca.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para a entidade ConfiguracaoGlobal.
 * Controla os parametros globais operacionais e tarifas do acervo no MySQL 8.
 * Implementa suporte para assegurar que sempre exista ao menos uma linha de
 * configuracao (padrao Singleton no banco).
 * 
 * @version 1.0
 */
public class ConfiguracaoDAO extends BaseDAO {

    /**
     * Insere uma nova configuracao global no banco de dados.
     * Suporta a definicao manual do ID (util para cargas e registro padrao) ou
     * geracao automatica.
     * 
     * @param config Objeto configuracao global a ser inserido.
     * @return boolean true se inserido, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean inserir(ConfiguracaoGlobal config) throws SQLException {
        String sql = "INSERT INTO configuracoes_globais (multa_diaria, limite_renovacoes) VALUES (?, ?)";
        if (config.getId() > 0) {
            sql = "INSERT INTO configuracoes_globais (id, multa_diaria, limite_renovacoes) VALUES (?, ?, ?)";
        }

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (config.getId() > 0) {
                stmt.setInt(1, config.getId());
                stmt.setDouble(2, config.getMultaDiaria());
                stmt.setInt(3, config.getLimiteRenovacoes());
            } else {
                stmt.setDouble(1, config.getMultaDiaria());
                stmt.setInt(2, config.getLimiteRenovacoes());
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        config.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Busca uma configuracao pelo seu identificador unico (ID).
     * 
     * @param id Identificador da configuracao.
     * @return ConfiguracaoGlobal preenchida, ou null caso nao encontrada.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public ConfiguracaoGlobal buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM configuracoes_globais WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ConfiguracaoGlobal(
                            rs.getInt("id"),
                            rs.getDouble("multa_diaria"),
                            rs.getInt("limite_renovacoes"));
                }
            }
        }
        return null;
    }

    /**
     * Atualiza as configuracoes globais de multas e limites de renovacao.
     * 
     * @param config Objeto configuracao contendo os novos parametros.
     * @return boolean true se atualizado, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean atualizar(ConfiguracaoGlobal config) throws SQLException {
        String sql = "UPDATE configuracoes_globais SET multa_diaria = ?, limite_renovacoes = ? WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, config.getMultaDiaria());
            stmt.setInt(2, config.getLimiteRenovacoes());
            stmt.setInt(3, config.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Atualiza as configuracoes globais de multas e limites de renovacao (Alias de
     * atualizar).
     * 
     * @param config Objeto configuracao contendo os novos parametros.
     * @return boolean true se atualizado, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean atualizarConfiguracao(ConfiguracaoGlobal config) throws SQLException {
        return atualizar(config);
    }

    /**
     * Exclui uma configuracao global do banco de dados pelo ID.
     * 
     * @param id Identificador da configuracao.
     * @return boolean true se excluido, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM configuracoes_globais WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todas as configuracoes globais gravadas na base.
     * 
     * @return List contendo todas as configuracoes globais.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<ConfiguracaoGlobal> listarTodos() throws SQLException {
        String sql = "SELECT * FROM configuracoes_globais ORDER BY id";
        List<ConfiguracaoGlobal> lista = new ArrayList<>();

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new ConfiguracaoGlobal(
                        rs.getInt("id"),
                        rs.getDouble("multa_diaria"),
                        rs.getInt("limite_renovacoes")));
            }
        }
        return lista;
    }

    /**
     * Obtem a unica instancia de configuracoes globais persistida no banco.
     * Caso o banco esteja vazio, insere e devolve uma configuracao padrao de
     * inicializacao.
     * 
     * @return ConfiguracaoGlobal preenchida.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public ConfiguracaoGlobal obterConfiguracao() throws SQLException {
        List<ConfiguracaoGlobal> todas = listarTodos();
        if (!todas.isEmpty()) {
            return todas.get(0);
        }

        // Se nao houver configuracao inserida no banco, insere o registro inicial
        // padrao (ID 1)
        ConfiguracaoGlobal defaultConfig = new ConfiguracaoGlobal(1, 1.00, 3);
        inserir(defaultConfig);
        return defaultConfig;
    }
}

