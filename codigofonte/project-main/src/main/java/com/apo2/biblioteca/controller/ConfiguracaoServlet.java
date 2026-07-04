package com.apo2.biblioteca.controller;

import com.apo2.biblioteca.model.dao.ConfiguracaoDAO;
import com.apo2.biblioteca.model.entity.ConfiguracaoGlobal;
import com.apo2.biblioteca.model.entity.Usuario;
import com.apo2.biblioteca.util.JsonConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller responsável por gerenciar as configurações gerais e regras operacionais do sistema.
 * Mapeia requisições HTTP para a rota '/api/admin/configuracoes'.
 * Operações restritas apenas a usuários ADMINISTRADORES.
 * 
 * @version 1.0
 */
@WebServlet("/api/admin/configuracoes")
public class ConfiguracaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();

    /**
     * Retorna os valores atuais de multas e limites em formato JSON.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            ConfiguracaoGlobal config = configuracaoDAO.obterConfiguracao();
            response.getWriter().write(JsonConverter.toJson(config));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro ao consultar as configurações no banco. Detalhe: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Atualiza os limites de renovações e valor diário de multa.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");

        // Validação extra de segurança para a Área Admin
        if (usuarioLogado == null || !usuarioLogado.ehAdministrador()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"erro\": \"Acesso exclusivo para administradores.\"}");
            return;
        }

        String multaDiariaStr = request.getParameter("multaDiaria");
        String limiteRenovacoesStr = request.getParameter("limiteRenovacoes");

        if (multaDiariaStr == null || multaDiariaStr.trim().isEmpty() ||
            limiteRenovacoesStr == null || limiteRenovacoesStr.trim().isEmpty()) {
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"Todos os campos de configuração devem ser preenchidos.\"}");
            return;
        }

        try {
            double multaDiaria = Double.parseDouble(multaDiariaStr);
            int limiteRenovacoes = Integer.parseInt(limiteRenovacoesStr);

            if (multaDiaria < 0.00 || limiteRenovacoes < 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"erro\": \"Os valores de multa e limites devem ser números positivos.\"}");
                return;
            }

            // Recupera a instância existente e atualiza os valores
            ConfiguracaoGlobal config = configuracaoDAO.obterConfiguracao();
            config.setMultaDiaria(multaDiaria);
            config.setLimiteRenovacoes(limiteRenovacoes);

            boolean sucesso = configuracaoDAO.atualizar(config);

            if (sucesso) {
                response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Parâmetros operacionais do sistema atualizados com sucesso!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"erro\": \"Falha interna ao gravar os novos parâmetros no banco.\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"Os formatos digitados para os campos de configuração são inválidos.\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro de banco de dados ao salvar as configurações. Detalhe: " + e.getMessage() + "\"}");
        }
    }
}

