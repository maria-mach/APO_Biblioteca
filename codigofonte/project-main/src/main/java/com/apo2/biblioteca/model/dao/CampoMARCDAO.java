package com.apo2.biblioteca.model.dao;

import com.apo2.biblioteca.model.entity.CampoMARC;
// import com.apo2.biblioteca.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para a entidade CampoMARC.
 * Realiza as operacoes de persistência da tabela satélite campos_marc no MySQL
 * 8.
 * 
 * @version 1.0
 */
public class CampoMARCDAO extends BaseDAO {

    /**
     * Insere um metadado MARC21 associado a um livro.
     * 
     * @param campo Objeto contendo os dados do campo MARC.
     * @return boolean true se inserido, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean inserir(CampoMARC campo) throws SQLException {
        String sql = "INSERT INTO campos_marc (livro_id, tag, valor) VALUES (?, ?, ?)";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, campo.getLivroId());
            stmt.setString(2, campo.getTag());
            stmt.setString(3, campo.getValor());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        campo.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Busca um metadado MARC21 pelo seu identificador unico (ID).
     * 
     * @param id Identificador do metadado.
     * @return CampoMARC preenchido, ou null caso nao seja encontrado.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public CampoMARC buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM campos_marc WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaCampoMARC(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca todos os metadados MARC21 associados a um determinado livro.
     * 
     * @param livroId Identificador do livro.
     * @return List de metadados correspondentes.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<CampoMARC> buscarPorLivroId(int livroId) throws SQLException {
        String sql = "SELECT * FROM campos_marc WHERE livro_id = ?";
        List<CampoMARC> campos = new ArrayList<>();

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livroId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    campos.add(mapearResultSetParaCampoMARC(rs));
                }
            }
        }
        return campos;
    }

    /**
     * Atualiza as informacoes de um campo MARC21.
     * 
     * @param campo Objeto contendo os dados atualizados do campo.
     * @return boolean true se atualizado com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean atualizar(CampoMARC campo) throws SQLException {
        String sql = "UPDATE campos_marc SET livro_id = ?, tag = ?, valor = ? WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campo.getLivroId());
            stmt.setString(2, campo.getTag());
            stmt.setString(3, campo.getValor());
            stmt.setInt(4, campo.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Remove um metadado MARC21 pelo seu ID.
     * 
     * @param id Identificador do metadado.
     * @return boolean true se deletado, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM campos_marc WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Exclui todos os metadados MARC21 vinculados a um determinado livro.
     * 
     * @param livroId Identificador do livro.
     * @return boolean true se os metadados foram excluidos, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean excluirPorLivroId(int livroId) throws SQLException {
        String sql = "DELETE FROM campos_marc WHERE livro_id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livroId);
            return stmt.executeUpdate() > 0;
        }
    }

    private CampoMARC mapearResultSetParaCampoMARC(ResultSet rs) throws SQLException {
        return new CampoMARC(
                rs.getInt("id"),
                rs.getInt("livro_id"),
                rs.getString("tag"),
                rs.getString("valor"));
    }
}

