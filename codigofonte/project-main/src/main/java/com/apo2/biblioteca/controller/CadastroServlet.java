package com.apo2.biblioteca.controller;

import com.apo2.biblioteca.model.dao.UsuarioDAO;
import com.apo2.biblioteca.model.entity.TipoUsuario;
import com.apo2.biblioteca.model.entity.Usuario;
import com.apo2.biblioteca.util.MailSender;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Controller responsável pelo auto-cadastro de novos leitores/clientes no sistema.
 * Realiza validações de duplicidade de e-mail e gera token de ativação.
 * Mapeia requisições HTTP para a rota '/api/cadastro'.
 * 
 * @version 1.0
 */
@WebServlet("/api/cadastro")
public class CadastroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (nome == null || nome.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            senha == null || senha.trim().isEmpty()) {
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"sucesso\": false, \"erro\": \"Todos os campos (Nome, E-mail e Senha) são de preenchimento obrigatório.\"}");
            return;
        }

        try {
            // Verifica se o e-mail já está em uso
            Usuario usuarioExistente = usuarioDAO.buscarPorEmail(email);
            if (usuarioExistente != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"sucesso\": false, \"erro\": \"Este endereço de e-mail já está sendo utilizado por outro usuário.\"}");
                return;
            }

            // Gera token temporário único para validação de e-mail (Mailtrap/SMTP)
            String tokenAtivacao = UUID.randomUUID().toString();
            
            // Instancia o novo leitor (inicia inativo: ativo = false)
            Usuario novoUsuario = new Usuario(
                    0, 
                    nome, 
                    email, 
                    senha, // A senha será encriptada dentro do UsuarioDAO.inserir()
                    tokenAtivacao, 
                    false, // Necessita de ativação por e-mail
                    TipoUsuario.CLIENTE
            );

            // Persiste no banco de dados
            boolean inserido = usuarioDAO.inserir(novoUsuario);

            if (inserido) {
                // Monta o link para ativação no servidor local
                String linkAtivacao = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
                        + request.getContextPath() + "/api/ativar?email=" + email + "&token=" + tokenAtivacao;
                
                // Constrói o corpo do e-mail
                String corpoEmail = "<h2>Olá, " + nome + "!</h2>"
                        + "<p>Agradecemos por se cadastrar no Sistema de Gestão de Biblioteca MARC21.</p>"
                        + "<p>Para concluir seu cadastro e ativar a sua conta, por favor clique no link abaixo:</p>"
                        + "<p><a href='" + linkAtivacao + "' target='_blank'><b>Ativar Minha Conta</b></a></p>"
                        + "<br/><p>Caso o link acima não funcione, copie e cole o endereço abaixo no seu navegador:</p>"
                        + "<p>" + linkAtivacao + "</p>"
                        + "<br/><p>Atenciosamente,<br/>Equipe de Biblioteca APO2</p>";

                // Envia o e-mail em segundo plano
                boolean emailEnviado = MailSender.enviarEmail(email, "Ativação de Conta - Biblioteca MARC21", corpoEmail);

                if (emailEnviado) {
                    response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Cadastro realizado com sucesso! Um e-mail com as instruções de ativação foi enviado para " + email + ".\"}");
                } else {
                    response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Cadastro realizado, mas houve uma falha de SMTP ao enviar o e-mail de ativação. Entre em contato com o administrador.\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"sucesso\": false, \"erro\": \"Falha interna ao registrar os dados cadastrais no banco.\"}");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"sucesso\": false, \"erro\": \"Erro de banco de dados ao tentar processar o cadastro. Detalhe: " + e.getMessage() + "\"}");
        }
    }
}

