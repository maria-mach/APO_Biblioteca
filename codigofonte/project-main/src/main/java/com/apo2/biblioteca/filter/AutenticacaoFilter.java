package com.apo2.biblioteca.filter;

import com.apo2.biblioteca.model.entity.Usuario;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro de Servlet responsável pela autenticação e controle de privilégios de acesso (RBAC).
 * Intercepta páginas JSPs internas e rotas da API.
 * 
 * @version 1.0
 */
@WebFilter(filterName = "AutenticacaoFilter", urlPatterns = {"/dashboard.jsp", "/admin.jsp", "/api/*"})
public class AutenticacaoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();
        
        // Rotas públicas que não necessitam de filtro (ex: login e cadastro na API)
        // /api/livros é pública (GET) para exibir o catálogo na página inicial sem login
        if (requestURI.endsWith("/api/login") || requestURI.endsWith("/api/cadastro")
                || requestURI.endsWith("/api/ativar")
                || requestURI.endsWith("/api/recuperar-senha") || requestURI.endsWith("/api/livros")) {
            chain.doFilter(request, response);
            return;
        }

        // Verifica se existe o usuário autenticado na sessão
        Usuario usuarioLogado = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        if (usuarioLogado == null) {
            // Se o usuário não está autenticado
            if (requestURI.contains("/api/")) {
                // Se for chamada de API, responde JSON com status 401 (Unauthorized)
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.getWriter().write("{\"erro\": \"Sessão expirada ou não autenticado. Por favor, realize o login.\"}");
            } else {
                // Se for página JSP, redireciona para a tela de login
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            }
            return;
        }

        // Se está logado, valida barreira para Área Administrativa (ADMIN)
        if (requestURI.contains("admin.jsp") || requestURI.contains("/api/admin/")) {
            if (!usuarioLogado.ehAdministrador()) {
                if (requestURI.contains("/api/")) {
                    // Resposta HTTP 403 Forbidden para chamadas da API
                    httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    httpResponse.setContentType("application/json");
                    httpResponse.setCharacterEncoding("UTF-8");
                    httpResponse.getWriter().write("{\"erro\": \"Acesso restrito apenas para administradores.\"}");
                } else {
                    // Redireciona o cliente para o painel dele
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard.jsp");
                }
                return;
            }
        }

        // Usuário autenticado e com permissão válida, prossegue com a requisição
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Encerramento do filtro
    }
}
