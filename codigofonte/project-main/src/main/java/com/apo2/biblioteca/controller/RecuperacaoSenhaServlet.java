package com.apo2.biblioteca.controller;

import com.apo2.biblioteca.model.dao.UsuarioDAO;
import com.apo2.biblioteca.model.entity.Usuario;
import com.apo2.biblioteca.util.MailSender;
import com.apo2.biblioteca.util.PasswordHasher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;

/**
 * Endpoint publico para recuperacao de senha via e-mail.
 */
@WebServlet("/api/recuperar-senha")
public class RecuperacaoSenhaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final SecureRandom random = new SecureRandom();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"sucesso\": false, \"erro\": \"Informe o e-mail cadastrado.\"}");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(email.trim());

            if (usuario != null && usuario.isAtivo()) {
                String senhaTemporaria = gerarSenhaTemporaria();
                usuario.setSenha(PasswordHasher.hash(senhaTemporaria));
                usuarioDAO.atualizar(usuario);

                String corpoEmail = "<h2>Recuperação de Senha - Biblioteca MARC21</h2>"
                        + "<p>Olá, " + usuario.getNome() + ".</p>"
                        + "<p>Sua senha temporária é: <strong>" + senhaTemporaria + "</strong></p>"
                        + "<p>Após entrar, atualize sua senha no painel do usuário.</p>";

                MailSender.enviarEmail(usuario.getEmail(), "Recuperação de Senha - Biblioteca MARC21", corpoEmail);
            }

            response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Se o e-mail estiver cadastrado e ativo, enviaremos uma senha temporária.\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"sucesso\": false, \"erro\": \"Erro ao processar recuperação de senha.\"}");
        }
    }

    private String gerarSenhaTemporaria() {
        StringBuilder senha = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            senha.append(ALFABETO.charAt(random.nextInt(ALFABETO.length())));
        }
        return senha.toString();
    }
}
