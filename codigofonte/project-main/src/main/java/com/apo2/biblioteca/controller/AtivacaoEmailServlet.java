package com.apo2.biblioteca.controller;

import com.apo2.biblioteca.model.dao.UsuarioDAO;
import com.apo2.biblioteca.model.entity.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller responsável por receber o clique no link de ativação enviado por e-mail.
 * Mapeia requisições HTTP para a rota '/api/ativar' (GET).
 * 
 * @version 1.0
 */
@WebServlet("/api/ativar")
public class AtivacaoEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String token = request.getParameter("token");

        if (email == null || email.trim().isEmpty() || token == null || token.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?erroAtivacao=invalid_params");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            
            if (usuario != null && !usuario.isAtivo() && token.equals(usuario.getTokenAtivacao())) {
                // Ativa a conta e limpa o token de segurança
                usuario.setAtivo(true);
                usuario.setTokenAtivacao(null);
                
                boolean atualizado = usuarioDAO.atualizar(usuario);
                
                if (atualizado) {
                    // Conta ativada com sucesso: redireciona informando a ativação
                    response.sendRedirect(request.getContextPath() + "/login.jsp?ativado=true");
                } else {
                    response.sendRedirect(request.getContextPath() + "/login.jsp?erroAtivacao=db_error");
                }
            } else {
                // Token inválido, e-mail inexistente ou usuário já ativo
                response.sendRedirect(request.getContextPath() + "/login.jsp?erroAtivacao=invalid_token");
            }
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?erroAtivacao=exception");
        }
    }
}

