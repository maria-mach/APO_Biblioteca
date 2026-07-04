package com.apo2.biblioteca.model.dao;

import com.apo2.biblioteca.model.entity.Emprestimo;
import com.apo2.biblioteca.model.entity.Usuario;
// import com.apo2.biblioteca.util.ConexaoFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para a entidade Emprestimo.
 * Responsavel pelo controle de transacoes de locacao fisica de livros no MySQL
 * 8.
 * 
 * @version 1.0
 */
public class EmprestimoDAO extends BaseDAO {

    /**
     * Insere um novo emprestimo no banco de dados (Alias de registrarEmprestimo).
     * 
     * @param emprestimo Objeto contendo os dados do emprestimo a ser inserido.
     * @return boolean true se inserido com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean inserir(Emprestimo emprestimo) throws SQLException {
        return registrarEmprestimo(emprestimo);
    }

    /**
     * Registra um novo emprestimo no banco de dados.
     * 
     * @param emprestimo Objeto contendo os dados do emprestimo.
     * @return boolean true se inserido com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean registrarEmprestimo(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimos (usuario_id, livro_id, status, data_emprestimo, data_devolucao_prevista, data_devolucao_efetiva, multa_acumulada, renovacoes_decorrentes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, emprestimo.getUsuarioId());
            stmt.setInt(2, emprestimo.getLivroId());
            stmt.setString(3, emprestimo.getStatus());
            stmt.setDate(4, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(5, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));

            if (emprestimo.getDataDevolucaoEfetiva() != null) {
                stmt.setDate(6, Date.valueOf(emprestimo.getDataDevolucaoEfetiva()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setDouble(7, emprestimo.getMultaAcumulada());
            stmt.setInt(8, emprestimo.getRenovacoesDecorrentes());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        emprestimo.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Busca um emprestimo pelo seu ID.
     * 
     * @param id Identificador do emprestimo.
     * @return Emprestimo preenchido, ou null caso nao encontrado.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public Emprestimo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaEmprestimo(rs);
                }
            }
        }
        return null;
    }

    /**
     * Atualiza as informacoes gerais de um emprestimo.
     * 
     * @param emprestimo Objeto emprestimo com novos dados.
     * @return boolean true se atualizado, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean atualizar(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE emprestimos SET usuario_id = ?, livro_id = ?, status = ?, data_emprestimo = ?, data_devolucao_prevista = ?, data_devolucao_efetiva = ?, multa_acumulada = ?, renovacoes_decorrentes = ? WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, emprestimo.getUsuarioId());
            stmt.setInt(2, emprestimo.getLivroId());
            stmt.setString(3, emprestimo.getStatus());
            stmt.setDate(4, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(5, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));

            if (emprestimo.getDataDevolucaoEfetiva() != null) {
                stmt.setDate(6, Date.valueOf(emprestimo.getDataDevolucaoEfetiva()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setDouble(7, emprestimo.getMultaAcumulada());
            stmt.setInt(8, emprestimo.getRenovacoesDecorrentes());
            stmt.setInt(9, emprestimo.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Registra a devolucao efetiva de um livro com calculo de multa associada.
     * 
     * @param id                   ID do emprestimo.
     * @param dataDevolucaoEfetiva Data real em que o livro foi devolvido.
     * @param multaAcumulada       Valor acumulado da multa por atraso.
     * @return boolean true se registrado com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean registrarDevolucao(int id, LocalDate dataDevolucaoEfetiva, double multaAcumulada)
            throws SQLException {
        String sql = "UPDATE emprestimos SET status = 'DEVOLVIDO', data_devolucao_efetiva = ?, multa_acumulada = ? WHERE id = ? AND status = 'ATIVO'";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataDevolucaoEfetiva));
            stmt.setDouble(2, multaAcumulada);
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Registra a renovacao online do prazo de devolucao de um livro.
     * 
     * @param id                   ID do emprestimo.
     * @param novaDataPrevisao     Nova data calculada para a entrega.
     * @param novasRenovacoesCount Novo numero de renovacoes decorrentes acumuladas.
     * @return boolean true se renovado com sucesso, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean renovarEmprestimo(int id, LocalDate novaDataPrevisao, int novasRenovacoesCount) throws SQLException {
        String sql = "UPDATE emprestimos SET data_devolucao_prevista = ?, renovacoes_decorrentes = ? WHERE id = ? AND status = 'ATIVO'";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(novaDataPrevisao));
            stmt.setInt(2, novasRenovacoesCount);
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Exclui um registro de emprestimo pelo ID.
     * 
     * @param id Identificador do emprestimo.
     * @return boolean true se excluido, false caso contrario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM emprestimos WHERE id = ?";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean aprovarSolicitacao(int id, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) throws SQLException {
        String sql = "UPDATE emprestimos SET status = 'ATIVO', data_emprestimo = ?, data_devolucao_prevista = ?, data_devolucao_efetiva = NULL, multa_acumulada = 0.00, renovacoes_decorrentes = 0 WHERE id = ? AND status = 'SOLICITADO'";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataEmprestimo));
            stmt.setDate(2, Date.valueOf(dataDevolucaoPrevista));
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean recusarSolicitacao(int id) throws SQLException {
        String sql = "UPDATE emprestimos SET status = 'RECUSADO' WHERE id = ? AND status = 'SOLICITADO'";

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todos os emprestimos registrados na base de dados.
     * 
     * @return List de emprestimos.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<Emprestimo> listarTodos() throws SQLException {
        String sql = "SELECT * FROM emprestimos";
        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emprestimos.add(mapearResultSetParaEmprestimo(rs));
            }
        }
        return emprestimos;
    }

    /**
     * Retorna a lista de todos os emprestimos ativos (nao devolvidos) do sistema.
     * 
     * @return List de emprestimos ativos.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<Emprestimo> listarEmprestimosAtivos() throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE status IN ('SOLICITADO', 'ATIVO')";
        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emprestimos.add(mapearResultSetParaEmprestimo(rs));
            }
        }
        return emprestimos;
    }

    /**
     * Retorna o historico de emprestimos de um usuario.
     * Implementa Lazy Loading conforme a Secao 3.3 da arquitetura.
     * 
     * @param usuario Objeto Usuario rico.
     * @return List de emprestimos do usuario.
     * @throws SQLException Caso ocorra erro no banco de dados.
     */
    public List<Emprestimo> listarHistoricoPorUsuario(Usuario usuario) throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE usuario_id = ?";
        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprestimos.add(mapearResultSetParaEmprestimo(rs));
                }
            }
        }
        return emprestimos;
    }

    private Emprestimo mapearResultSetParaEmprestimo(ResultSet rs) throws SQLException {
        Date dataDevEfetiva = rs.getDate("data_devolucao_efetiva");
        LocalDate localDateDevEfetiva = (dataDevEfetiva != null) ? dataDevEfetiva.toLocalDate() : null;

        return new Emprestimo(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getInt("livro_id"),
                rs.getDate("data_emprestimo").toLocalDate(),
                rs.getDate("data_devolucao_prevista").toLocalDate(),
                localDateDevEfetiva,
                rs.getDouble("multa_acumulada"),
                rs.getInt("renovacoes_decorrentes"),
                rs.getString("status"));
    }
}
