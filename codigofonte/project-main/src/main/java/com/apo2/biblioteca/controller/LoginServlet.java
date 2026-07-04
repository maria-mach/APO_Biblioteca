package com.apo2.biblioteca.controller;

import com.apo2.biblioteca.model.dao.UsuarioDAO;
import com.apo2.biblioteca.model.entity.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller responsável pela autenticação de usuários na plataforma.
 * Mapeia requisições HTTP para a rota '/api/login'.
 * 
 * @version 1.0
 */
@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"sucesso\": false, \"erro\": \"Os campos de e-mail e senha são obrigatórios.\"}");
            return;
        }

        try {
            // Invoca a verificação no banco de dados com hash de senhas
            Usuario usuario = usuarioDAO.validarLogin(email, senha);
            
            if (usuario != null) {
                // Autenticação bem-sucedida: cria ou recupera a sessão do usuário
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioLogado", usuario);
                
                // Retorna sucesso e a rota de redirecionamento com base no nível do usuário
                String redirectUrl = usuario.ehAdministrador() ? "admin.jsp" : "dashboard.jsp";
                response.getWriter().write("{\"sucesso\": true, \"redirect\": \"" + redirectUrl + "\"}");
            } else {
                // Falha na autenticação (credenciais inválidas ou conta inativa)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"sucesso\": false, \"erro\": \"E-mail ou senha incorretos, ou a conta ainda não foi ativada.\"}");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"sucesso\": false, \"erro\": \"Erro interno no servidor ao tentar realizar o login. Detalhe: " + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Realiza o logout de forma assíncrona ou por requisição GET
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}

