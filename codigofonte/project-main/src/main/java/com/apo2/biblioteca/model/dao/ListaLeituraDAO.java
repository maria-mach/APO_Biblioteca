package com.apo2.biblioteca.model.dao;

import com.apo2.biblioteca.model.entity.LivroFisico;
import com.apo2.biblioteca.model.entity.ListaLeitura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para a entidade ListaLeitura.
 * Gerencia a criacao de colecoes e associacao N:M de livros de interesse no MySQL 8.
 * 
 * @version 1.0
 */
public class ListaLeituraDAO extends BaseDAO {

    /**
     * Cria uma nova lista de leitura no banco.
     * 
     * @param lista Objeto contendo os dados da lista de leitura.
     * @return boolean true se criada com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean inserir(ListaLeitura lista) throws SQLException {
        String sql = "INSERT INTO listas_leitura (usuario_id, nome, data_criacao) VALUES (?, ?, ?)";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, lista.getUsuarioId());
            stmt.setString(2, lista.getNome());
            stmt.setTimestamp(3, Timestamp.valueOf(lista.getDataCriacao() != null ? lista.getDataCriacao() : LocalDateTime.now()));
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        lista.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Busca uma lista de leitura pelo seu ID e carrega os livros nela contidos.
     * 
     * @param id Identificador da lista.
     * @return ListaLeitura preenchida com livros, ou null caso nao encontrada.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public ListaLeitura buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM listas_leitura WHERE id = ?";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ListaLeitura lista = mapearResultSetParaListaLeitura(rs);
                    lista.setLivros(buscarLivrosDaLista(id));
                    return lista;
                }
            }
        }
        return null;
    }

    /**
     * Atualiza o nome de uma lista de leitura.
     * 
     * @param lista Objeto da lista com nome atualizado.
     * @return boolean true se atualizada, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean atualizar(ListaLeitura lista) throws SQLException {
        String sql = "UPDATE listas_leitura SET nome = ? WHERE id = ?";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, lista.getNome());
            stmt.setInt(2, lista.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Remove uma lista de leitura. A remocao das linhas vinculadas na tabela pivot listas_leitura_livros
     * ocorre em cascata (ON DELETE CASCADE) conforme definido no banco.
     * 
     * @param id Identificador da lista.
     * @return boolean true se removida, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM listas_leitura WHERE id = ?";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todas as listas de leitura cadastradas na base de dados.
     * Carrega tambem a colecao de livros associados de cada lista.
     * 
     * @return List contendo todas as listas de leitura.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<ListaLeitura> listarTodos() throws SQLException {
        String sql = "SELECT * FROM listas_leitura ORDER BY data_criacao DESC";
        List<ListaLeitura> listas = new ArrayList<>();
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                listas.add(mapearResultSetParaListaLeitura(rs));
            }
        }
        
        for (ListaLeitura lista : listas) {
            lista.setLivros(buscarLivrosDaLista(lista.getId()));
        }
        
        return listas;
    }

    /**
     * Lista todas as pastas de listas de leitura de um usuario leitor.
     * 
     * @param usuarioId ID do usuario leitor.
     * @return List contendo as listas encontradas.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<ListaLeitura> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM listas_leitura WHERE usuario_id = ? ORDER BY data_criacao DESC";
        List<ListaLeitura> listas = new ArrayList<>();
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    listas.add(mapearResultSetParaListaLeitura(rs));
                }
            }
        }
        
        // Carrega os livros associados para cada lista mapeada
        for (ListaLeitura lista : listas) {
            lista.setLivros(buscarLivrosDaLista(lista.getId()));
        }
        
        return listas;
    }

    /**
     * Associa um livro a uma lista de leitura na tabela pivot listas_leitura_livros (N:M).
     * 
     * @param listaLeituraId Identificador da lista de leitura.
     * @param livroId        Identificador do livro.
     * @return boolean true se associado, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean adicionarLivroNaLista(int listaLeituraId, int livroId) throws SQLException {
        String sql = "INSERT INTO listas_leitura_livros (lista_leitura_id, livro_id) VALUES (?, ?)";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, listaLeituraId);
            stmt.setInt(2, livroId);
            
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Remove a associacao de um livro com uma determinada lista de leitura.
     * 
     * @param listaLeituraId Identificador da lista de leitura.
     * @param livroId        Identificador do livro.
     * @return boolean true se removida a associacao, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean removerLivroDaLista(int listaLeituraId, int livroId) throws SQLException {
        String sql = "DELETE FROM listas_leitura_livros WHERE lista_leitura_id = ? AND livro_id = ?";
        
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, listaLeituraId);
            stmt.setInt(2, livroId);
            
            return stmt.executeUpdate() > 0;
        }
    }

    private List<LivroFisico> buscarLivrosDaLista(int listaId) throws SQLException {
        String sql = "SELECT l.* FROM livros l " +
                     "JOIN listas_leitura_livros pl ON l.id = pl.livro_id " +
                     "WHERE pl.lista_leitura_id = ?";
        
        List<LivroFisico> livros = new ArrayList<>();
        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, listaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livros.add(new LivroFisico(
                            rs.getInt("id"),
                            rs.getString("isbn"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("qtd_total"),
                            rs.getInt("qtd_disponivel")
                    ));
                }
            }
        }
        return livros;
    }

    private ListaLeitura mapearResultSetParaListaLeitura(ResultSet rs) throws SQLException {
        return new ListaLeitura(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getString("nome"),
                rs.getTimestamp("data_criacao").toLocalDateTime()
        );
    }
}

