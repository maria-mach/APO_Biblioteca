package com.apo2.biblioteca.controller;

import com.apo2.biblioteca.model.dao.LivroFisicoDAO;
import com.apo2.biblioteca.model.entity.LivroFisico;
import com.apo2.biblioteca.model.entity.Usuario;
import com.apo2.biblioteca.util.JsonConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller responsável pela listagem, busca e cadastramento de livros físicos.
 * Mapeia requisições HTTP para a rota '/api/livros'.
 * Implementa controle de acesso (RBAC) para operações administrativas.
 * 
 * @version 1.0
 */
@WebServlet("/api/livros")
public class LivroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final LivroFisicoDAO livroDAO = new LivroFisicoDAO();

    /**
     * Retorna a lista de livros cadastrados em formato JSON.
     * Suporta busca por tag MARC21 utilizando os parâmetros opcionais 'tag' e 'valor'.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String tag = request.getParameter("tag");
        String valor = request.getParameter("valor");
        String buscaGeral = request.getParameter("buscaGeral");

        try {
            List<LivroFisico> livros;
            if (tag != null && !tag.trim().isEmpty() && valor != null && !valor.trim().isEmpty()) {
                // Busca avançada utilizando indexação MARC21
                livros = livroDAO.buscarAvancadaMARC(tag, valor);
            } else if (buscaGeral != null && !buscaGeral.trim().isEmpty()) {
                livros = livroDAO.buscarGeral(buscaGeral.trim());
            } else {
                // Listagem geral padrão
                livros = livroDAO.listarTodos();
            }
            
            // Serializa a lista utilizando o Utilitário Gson configurado
            String jsonOutput = JsonConverter.toJson(livros);
            response.getWriter().write(jsonOutput);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro ao consultar acervo no banco de dados. Detalhe: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Cadastra um novo livro no sistema.
     * Operação restrita apenas a usuários ADMINISTRADORES.
     * Baseado na Listing 4 do Documento de Diretrizes Arquiteturais (pág. 8).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Recupera o usuário alocado na sessão (o filtro já valida se está nulo, mas fazemos dupla validação)
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");

        // Barreira de Segurança Baseada em Papel (RBAC) conforme exigido na arquitetura
        if (usuarioLogado == null || !usuarioLogado.ehAdministrador()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"erro\": \"Acesso restrito a administradores.\"}");
            return;
        }

        // Processamento assíncrono dos parâmetros recebidos via AJAX
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String isbn = request.getParameter("isbn");
        String qtdTotalStr = request.getParameter("qtdTotal");
        String acao = request.getParameter("acao");
        String idStr = request.getParameter("id");

        if (titulo == null || titulo.trim().isEmpty() || 
            autor == null || autor.trim().isEmpty() || 
            isbn == null || isbn.trim().isEmpty()) {
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"Todos os dados obrigatórios do livro (Título, Autor, ISBN) devem ser preenchidos.\"}");
            return;
        }

        int qtdTotal = 1;
        if (qtdTotalStr != null && !qtdTotalStr.trim().isEmpty()) {
            try {
                qtdTotal = Integer.parseInt(qtdTotalStr);
            } catch (NumberFormatException ignored) {}
        }

        try {
            boolean sucesso;
            LivroFisico livro;
            if ("atualizar".equalsIgnoreCase(acao)) {
                int id = Integer.parseInt(idStr);
                LivroFisico livroAtual = livroDAO.buscarPorId(id);
                if (livroAtual == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"erro\": \"Livro não encontrado no acervo.\"}");
                    return;
                }

                int emprestados = livroAtual.getQtdTotal() - livroAtual.getQtdDisponivel();
                int qtdDisponivel = Math.max(0, qtdTotal - emprestados);
                livro = new LivroFisico(id, isbn, titulo, autor, qtdTotal, qtdDisponivel);
                sucesso = livroDAO.atualizar(livro);
            } else {
                livro = new LivroFisico(0, isbn, titulo, autor, qtdTotal, qtdTotal);
                sucesso = livroDAO.inserir(livro);
            }

            if (sucesso) {
                String mensagem = "atualizar".equalsIgnoreCase(acao)
                        ? "Obra atualizada com sucesso!"
                        : "Obra catalogada com sucesso!";
                response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"" + mensagem + "\", \"id\": " + livro.getId() + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"erro\": \"Falha interna ao persistir os dados do livro no banco.\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"ID ou quantidade inválida para o livro.\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro de banco de dados ao tentar salvar o livro. Detalhe: " + e.getMessage() + "\"}");
        }
    }
}
