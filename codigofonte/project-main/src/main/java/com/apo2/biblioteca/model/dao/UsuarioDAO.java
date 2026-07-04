package com.apo2.biblioteca.model.dao;

import com.apo2.biblioteca.model.entity.TipoUsuario;
import com.apo2.biblioteca.model.entity.Usuario;
// import com.apo2.biblioteca.util.ConexaoFactory;
import com.apo2.biblioteca.util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para a entidade Usuario.
 * Realiza as operacoes CRUD e consultas adicionais no MySQL 8.
 * 
 * @version 1.0
 */
public class UsuarioDAO extends BaseDAO {

    /**
     * Insere um novo usuario no banco de dados.
     * 
     * @param usuario Objeto contendo os dados do usuario a ser inserido.
     * @return boolean true se inserido com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra algum erro no banco de dados.
     */
    public boolean inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, token_ativacao, ativo, tipo_usuario) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            // Criptografa a senha antes de persistir
            stmt.setString(3, PasswordHasher.hash(usuario.getSenha()));
            stmt.setString(4, usuario.getTokenAtivacao());
            stmt.setBoolean(5, usuario.isAtivo());
            stmt.setString(6, usuario.getTipoUsuario().name());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Busca um usuario pelo seu identificador unico (ID).
     * 
     * @param id Identificador do usuario.
     * @return Usuario objeto preenchido, ou null caso nao seja encontrado.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaUsuario(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca um usuario pelo seu e-mail cadastrado.
     * 
     * @param email Endereco de e-mail.
     * @return Usuario correspondente ou null caso nao encontrado.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaUsuario(rs);
                }
            }
        }
        return null;
    }

    /**
     * Atualiza as informacoes cadastrais de um usuario.
     * 
     * @param usuario Objeto usuario com os dados atualizados.
     * @return boolean true se atualizado com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, token_ativacao = ?, ativo = ?, tipo_usuario = ? WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha()); // Assume-se que a senha ja esta em hash no objeto
            stmt.setString(4, usuario.getTokenAtivacao());
            stmt.setBoolean(5, usuario.isAtivo());
            stmt.setString(6, usuario.getTipoUsuario().name());
            stmt.setInt(7, usuario.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Exclui um usuario do banco de dados pelo ID.
     * 
     * @param id Identificador do usuario.
     * @return boolean true se deletado com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todos os usuarios cadastrados na base de dados.
     * 
     * @return List de usuarios cadastrados.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearResultSetParaUsuario(rs));
            }
        }
        return usuarios;
    }

    /**
     * Valida as credenciais de login e devolve o usuario autenticado caso esteja
     * correto e ativo.
     * 
     * @param email Email do usuario.
     * @param senha Senha em formato limpo.
     * @return Usuario autenticado se for valido e ativo, ou null em caso de falha.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public Usuario validarLogin(String email, String senha) throws SQLException {
        Usuario usuario = buscarPorEmail(email);
        if (usuario != null && usuario.isAtivo() && PasswordHasher.verificar(senha, usuario.getSenha())) {
            return usuario;
        }
        return null;
    }

    private Usuario mapearResultSetParaUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("token_ativacao"),
                rs.getBoolean("ativo"),
                TipoUsuario.valueOf(rs.getString("tipo_usuario")));
    }
}

