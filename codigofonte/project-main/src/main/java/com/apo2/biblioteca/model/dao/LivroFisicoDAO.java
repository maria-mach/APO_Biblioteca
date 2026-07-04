package com.apo2.biblioteca.model.dao;

import com.apo2.biblioteca.model.entity.LivroFisico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para a entidade LivroFisico.
 * Gerencia a inserção, edição, exclusão e buscas no acervo físico no MySQL 8.
 * Estende a classe BaseDAO.
 * 
 * @version 1.0
 */
public class LivroFisicoDAO extends BaseDAO {

    /**
     * Insere um novo livro físico no acervo.
     * 
     * @param livro Objeto contendo os dados do livro físico.
     * @return boolean true se inserido, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean inserir(LivroFisico livro) throws SQLException {
        String sql = "INSERT INTO livros (isbn, titulo, autor, qtd_total, qtd_disponivel) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, livro.getIsbn());
            stmt.setString(2, livro.getTitulo());
            stmt.setString(3, livro.getAutor());
            stmt.setInt(4, livro.getQtdTotal());
            stmt.setInt(5, livro.getQtdDisponivel());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        livro.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Busca um livro físico pelo seu identificador único (ID).
     * 
     * @param id Identificador do livro.
     * @return LivroFisico preenchido, ou null caso nao seja encontrado.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public LivroFisico buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM livros WHERE id = ?";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaLivroFisico(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca um livro físico pelo seu ISBN de registro.
     * 
     * @param isbn Registro ISBN.
     * @return LivroFisico correspondente ou null caso nao encontrado.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public LivroFisico buscarPorIsbn(String isbn) throws SQLException {
        String sql = "SELECT * FROM livros WHERE isbn = ?";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaLivroFisico(rs);
                }
            }
        }
        return null;
    }

    /**
     * Atualiza os dados cadastrais e de estoque de um livro físico.
     * 
     * @param livro Objeto LivroFisico com os novos dados.
     * @return boolean true se atualizado, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean atualizar(LivroFisico livro) throws SQLException {
        String sql = "UPDATE livros SET isbn = ?, titulo = ?, autor = ?, qtd_total = ?, qtd_disponivel = ? WHERE id = ?";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, livro.getIsbn());
            stmt.setString(2, livro.getTitulo());
            stmt.setString(3, livro.getAutor());
            stmt.setInt(4, livro.getQtdTotal());
            stmt.setInt(5, livro.getQtdDisponivel());
            stmt.setInt(6, livro.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Remove um livro físico do acervo pelo ID.
     * 
     * @param id Identificador do livro.
     * @return boolean true se removido, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM livros WHERE id = ?";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todos os livros físicos do acervo.
     * 
     * @return List de LivroFisico.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<LivroFisico> listarTodos() throws SQLException {
        String sql = "SELECT * FROM livros";
        List<LivroFisico> livros = new ArrayList<>();
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                livros.add(mapearResultSetParaLivroFisico(rs));
            }
        }
        return livros;
    }

    /**
     * Busca livros por titulo, autor, ISBN ou campos MARC21 (tag e valor).
     *
     * @param termo Termo geral digitado pelo usuario.
     * @return List de LivroFisico correspondentes.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<LivroFisico> buscarGeral(String termo) throws SQLException {
        String sql = "SELECT DISTINCT l.* FROM livros l " +
                     "LEFT JOIN campos_marc c ON l.id = c.livro_id " +
                     "WHERE l.titulo LIKE ? " +
                     "OR l.autor LIKE ? " +
                     "OR l.isbn LIKE ? " +
                     "OR c.tag LIKE ? " +
                     "OR c.valor LIKE ?";

        List<LivroFisico> livros = new ArrayList<>();
        String termoLike = "%" + termo + "%";
        String digitos = termo.replaceAll("\\D", "");
        String tagLike = digitos.length() == 3 ? "%" + digitos + "%" : termoLike;

        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, termoLike);
            stmt.setString(2, termoLike);
            stmt.setString(3, termoLike);
            stmt.setString(4, tagLike);
            stmt.setString(5, termoLike);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livros.add(mapearResultSetParaLivroFisico(rs));
                }
            }
        }
        return livros;
    }

    /**
     * Busca livros usando filtros do padrão MARC21 na tabela satélite de campos.
     * 
     * @param tag   Número de identificação da tag MARC21 (ex: '650' para assunto).
     * @param valor Termo a ser pesquisado (suporta busca parcial via LIKE).
     * @return List de LivroFisico correspondentes.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<LivroFisico> buscarAvancadaMARC(String tag, String valor) throws SQLException {
        String sql = "SELECT DISTINCT l.* FROM livros l " +
                     "JOIN campos_marc c ON l.id = c.livro_id " +
                     "WHERE c.tag = ? AND c.valor LIKE ?";
        
        List<LivroFisico> livros = new ArrayList<>();
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tag);
            stmt.setString(2, "%" + valor + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livros.add(mapearResultSetParaLivroFisico(rs));
                }
            }
        }
        return livros;
    }

    private LivroFisico mapearResultSetParaLivroFisico(ResultSet rs) throws SQLException {
        return new LivroFisico(
                rs.getInt("id"),
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("qtd_total"),
                rs.getInt("qtd_disponivel")
        );
    }
}
