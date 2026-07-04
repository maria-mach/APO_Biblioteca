package com.apo2.biblioteca.controller;

import com.apo2.biblioteca.model.dao.UsuarioDAO;
import com.apo2.biblioteca.model.entity.TipoUsuario;
import com.apo2.biblioteca.model.entity.Usuario;
import com.apo2.biblioteca.util.PasswordHasher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Endpoints de perfil autenticado e administracao de usuarios.
 */
@WebServlet({"/api/perfil", "/api/admin/usuarios"})
public class UsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        String uri = request.getRequestURI();

        try {
            if (uri.endsWith("/api/perfil")) {
                response.getWriter().write(usuarioParaJson(usuarioLogado));
                return;
            }

            if (!usuarioLogado.ehAdministrador()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"erro\": \"Acesso restrito a administradores.\"}");
                return;
            }

            List<Usuario> usuarios = usuarioDAO.listarTodos();
            response.getWriter().write(listaUsuariosParaJson(usuarios));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro ao consultar usuários.\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        String uri = request.getRequestURI();

        try {
            if (uri.endsWith("/api/perfil")) {
                atualizarPerfil(request, response, usuarioLogado);
                return;
            }

            if (!usuarioLogado.ehAdministrador()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"erro\": \"Acesso restrito a administradores.\"}");
                return;
            }

            atualizarUsuarioAdmin(request, response, usuarioLogado);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro ao atualizar usuário.\"}");
        }
    }

    private void atualizarPerfil(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado)
            throws SQLException, IOException {

        String nome = request.getParameter("nome");
        String senha = request.getParameter("senha");

        if (nome == null || nome.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"O nome é obrigatório.\"}");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorId(usuarioLogado.getId());
        usuario.setNome(nome.trim());
        if (senha != null && !senha.trim().isEmpty()) {
            if (senha.trim().length() < 6) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"erro\": \"A nova senha deve ter pelo menos 6 caracteres.\"}");
                return;
            }
            usuario.setSenha(PasswordHasher.hash(senha.trim()));
        }

        usuarioDAO.atualizar(usuario);
        request.getSession().setAttribute("usuarioLogado", usuario);
        response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Dados atualizados com sucesso.\", \"usuario\": " + usuarioParaJson(usuario) + "}");
    }

    private void atualizarUsuarioAdmin(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado)
            throws SQLException, IOException {

        String idStr = request.getParameter("id");
        String tipo = request.getParameter("tipoUsuario");
        String ativoStr = request.getParameter("ativo");

        if (idStr == null || tipo == null || ativoStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"ID, perfil e status são obrigatórios.\"}");
            return;
        }

        int id;
        TipoUsuario tipoUsuario;
        try {
            id = Integer.parseInt(idStr);
            tipoUsuario = TipoUsuario.valueOf(tipo);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"ID ou perfil de usuário inválido.\"}");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"erro\": \"Usuário não encontrado.\"}");
            return;
        }

        if (usuario.getId() == usuarioLogado.getId() && "false".equalsIgnoreCase(ativoStr)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"O administrador logado não pode inativar a própria conta.\"}");
            return;
        }

        usuario.setTipoUsuario(tipoUsuario);
        usuario.setAtivo(Boolean.parseBoolean(ativoStr));
        usuarioDAO.atualizar(usuario);
        response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Usuário atualizado com sucesso.\"}");
    }

    private String listaUsuariosParaJson(List<Usuario> usuarios) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < usuarios.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(usuarioParaJson(usuarios.get(i)));
        }
        json.append("]");
        return json.toString();
    }

    private String usuarioParaJson(Usuario usuario) {
        return "{"
                + "\"id\":" + usuario.getId() + ","
                + "\"nome\":\"" + escapar(usuario.getNome()) + "\","
                + "\"email\":\"" + escapar(usuario.getEmail()) + "\","
                + "\"ativo\":" + usuario.isAtivo() + ","
                + "\"tipoUsuario\":\"" + usuario.getTipoUsuario().name() + "\""
                + "}";
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}
