package com.apo2.biblioteca.controller;

import com.apo2.biblioteca.model.dao.ConfiguracaoDAO;
import com.apo2.biblioteca.model.dao.EmprestimoDAO;
import com.apo2.biblioteca.model.dao.LivroFisicoDAO;
import com.apo2.biblioteca.model.dao.UsuarioDAO;
import com.apo2.biblioteca.model.entity.ConfiguracaoGlobal;
import com.apo2.biblioteca.model.entity.Emprestimo;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Controller responsável pelas operações de Locação, Renovação e Devolução de livros físicos.
 * Mapeia requisições HTTP para a rota '/api/emprestimos'.
 * 
 * @version 1.0
 */
@WebServlet("/api/emprestimos")
public class EmprestimoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final LivroFisicoDAO livroDAO = new LivroFisicoDAO();
    private final ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Retorna a lista de empréstimos do usuário autenticado em formato JSON.
     * Implementa o carregamento tardio (Lazy Loading) do histórico.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");

        try {
            List<Emprestimo> historico;
            if (usuarioLogado.ehAdministrador()) {
                // Admin visualiza todos os empréstimos ativos no sistema
                historico = emprestimoDAO.listarTodos();
            } else {
                // Cliente visualiza apenas seu próprio histórico (Lazy Loading)
                historico = emprestimoDAO.listarHistoricoPorUsuario(usuarioLogado);
            }

            // Atualiza dinamicamente as multas acumuladas dos empréstimos ativos em atraso
            ConfiguracaoGlobal config = configuracaoDAO.obterConfiguracao();
            double multaDiaria = config.getMultaDiaria();
            
            for (Emprestimo emp : historico) {
                if ("ATIVO".equals(emp.getStatus()) && emp.getDataDevolucaoEfetiva() == null && LocalDate.now().isAfter(emp.getDataDevolucaoPrevista())) {
                    long diasAtraso = ChronoUnit.DAYS.between(emp.getDataDevolucaoPrevista(), LocalDate.now());
                    emp.setMultaAcumulada(diasAtraso * multaDiaria);
                }
            }

            response.getWriter().write(JsonConverter.toJson(historico));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro ao carregar histórico de empréstimos. Detalhe: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Executa operações de registrar empréstimo, renovar prazo ou registrar devolução.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        String acao = request.getParameter("acao");

        if (acao == null || acao.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"Parâmetro 'acao' não informado.\"}");
            return;
        }

        try {
            switch (acao) {
                case "cadastrar":
                    processarNovoEmprestimo(request, response, usuarioLogado);
                    break;
                case "renovar":
                    processarRenovacao(request, response, usuarioLogado);
                    break;
                case "devolver":
                    processarDevolucao(request, response, usuarioLogado);
                    break;
                case "aprovar":
                    processarAprovacao(request, response, usuarioLogado);
                    break;
                case "recusar":
                    processarRecusa(request, response, usuarioLogado);
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"erro\": \"Ação desconhecida.\"}");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro interno de banco de dados: " + e.getMessage() + "\"}");
        }
    }

    private void processarNovoEmprestimo(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado) 
            throws SQLException, IOException {
        
        // Solicitações podem ser iniciadas pelo leitor ou cadastradas pelo admin para outro leitor
        int usuarioId = usuarioLogado.getId();
        if (usuarioLogado.ehAdministrador()) {
            String cliIdStr = request.getParameter("usuarioId");
            if (cliIdStr != null && !cliIdStr.trim().isEmpty()) {
                usuarioId = Integer.parseInt(cliIdStr);
            }
        }

        Usuario usuarioOperacao = usuarioLogado;
        if (usuarioId != usuarioLogado.getId()) {
            usuarioOperacao = usuarioDAO.buscarPorId(usuarioId);
            if (usuarioOperacao == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"erro\": \"Leitor informado não foi encontrado.\"}");
                return;
            }
        }

        String livroIdStr = request.getParameter("livroId");
        if (livroIdStr == null || livroIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"ID do livro deve ser informado.\"}");
            return;
        }
        int livroId = Integer.parseInt(livroIdStr);

        // Verifica regras de negócio: disponibilidade do livro
        LivroFisico livro = livroDAO.buscarPorId(livroId);
        if (livro == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"erro\": \"Livro físico não encontrado no acervo.\"}");
            return;
        }

        if (livro.getQtdDisponivel() <= 0) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Não existem exemplares físicos disponíveis em estante para este livro.\"}");
            return;
        }

        // Bloqueio por Inadimplência (RNF3): verifica se o leitor tem multas ou atrasos ativos
        List<Emprestimo> emprestimosLeitor = emprestimoDAO.listarHistoricoPorUsuario(usuarioOperacao);
        for (Emprestimo emp : emprestimosLeitor) {
            if ("ATIVO".equals(emp.getStatus()) && emp.getDataDevolucaoEfetiva() == null) {
                if (LocalDate.now().isAfter(emp.getDataDevolucaoPrevista()) || emp.getMultaAcumulada() > 0) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"erro\": \"A solicitação foi bloqueada pois você possui multas em aberto ou livros com devolução atrasada.\"}");
                    return;
                }
            }
        }

        // Registra apenas a solicitação. O estoque só é decrementado após aprovação do admin.
        LocalDate hoje = LocalDate.now();
        LocalDate dataPrevista = hoje.plusDays(7);

        Emprestimo novoEmp = new Emprestimo(0, usuarioId, livroId, hoje, dataPrevista, null, 0.00, 0, "SOLICITADO");
        boolean registrado = emprestimoDAO.registrarEmprestimo(novoEmp);

        if (registrado) {
            response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Solicitação enviada com sucesso! Aguarde a aprovação do administrador para retirar o livro.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Erro ao salvar solicitação no banco.\"}");
        }
    }

    private void processarRenovacao(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado) 
            throws SQLException, IOException {
        
        String empIdStr = request.getParameter("emprestimoId");
        if (empIdStr == null || empIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"ID do empréstimo deve ser informado.\"}");
            return;
        }
        int empId = Integer.parseInt(empIdStr);

        Emprestimo emp = emprestimoDAO.buscarPorId(empId);
        if (emp == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"erro\": \"Registro de empréstimo não encontrado.\"}");
            return;
        }

        // Validação de segurança: apenas o próprio leitor ou um admin pode renovar
        if (!usuarioLogado.ehAdministrador() && emp.getUsuarioId() != usuarioLogado.getId()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"erro\": \"Você não tem permissão para renovar este empréstimo.\"}");
            return;
        }

        if (!"ATIVO".equals(emp.getStatus())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Somente empréstimos aprovados e ativos podem ser renovados.\"}");
            return;
        }

        // Validação de atraso: não permite renovação se estiver atrasado (RNF3)
        if (LocalDate.now().isAfter(emp.getDataDevolucaoPrevista())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Não é permitido renovar livros com data de devolução vencida. Favor regularizar o empréstimo.\"}");
            return;
        }

        // Valida limites de renovações dinâmicos gravados nas Configurações Globais (RF11 / RF18)
        ConfiguracaoGlobal config = configuracaoDAO.obterConfiguracao();
        if (emp.getRenovacoesDecorrentes() >= config.getLimiteRenovacoes()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Limite máximo de renovações consecutivas atingido (" + config.getLimiteRenovacoes() + ").\"}");
            return;
        }

        // Efetiva a renovação por +7 dias corridos a partir da data de vencimento anterior
        LocalDate novoVencimento = emp.getDataDevolucaoPrevista().plusDays(7);
        int novosRenovacoesCount = emp.getRenovacoesDecorrentes() + 1;

        boolean renovado = emprestimoDAO.renovarEmprestimo(empId, novoVencimento, novosRenovacoesCount);
        if (renovado) {
            response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Renovação efetuada com sucesso! Nova data de vencimento: " + novoVencimento + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Falha interna ao gravar a renovação no banco.\"}");
        }
    }

    private void processarDevolucao(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado) 
            throws SQLException, IOException {
        
        // Devoluções devem ser processadas apenas por administradores (Baixa Operacional - RF15)
        if (!usuarioLogado.ehAdministrador()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"erro\": \"A baixa operacional de empréstimos é exclusiva para administradores.\"}");
            return;
        }

        String empIdStr = request.getParameter("emprestimoId");
        if (empIdStr == null || empIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"ID do empréstimo é obrigatório.\"}");
            return;
        }
        int empId = Integer.parseInt(empIdStr);

        Emprestimo emp = emprestimoDAO.buscarPorId(empId);
        if (emp == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"erro\": \"Registro de empréstimo não encontrado.\"}");
            return;
        }

        if (emp.getDataDevolucaoEfetiva() != null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Esta devolução já foi registrada anteriormente.\"}");
            return;
        }

        if (!"ATIVO".equals(emp.getStatus())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Somente empréstimos aprovados e ativos podem receber baixa de devolução.\"}");
            return;
        }

        // Efetiva a devolução no dia de hoje
        LocalDate hoje = LocalDate.now();
        double multaAcumulada = 0.00;

        // Calcula a sanção de multa se devolvido após a data limite (RNF4)
        if (hoje.isAfter(emp.getDataDevolucaoPrevista())) {
            long diasAtraso = ChronoUnit.DAYS.between(emp.getDataDevolucaoPrevista(), hoje);
            ConfiguracaoGlobal config = configuracaoDAO.obterConfiguracao();
            multaAcumulada = diasAtraso * config.getMultaDiaria();
        }

        // Atualiza a devolução no banco de dados
        boolean devolvido = emprestimoDAO.registrarDevolucao(empId, hoje, multaAcumulada);

        if (devolvido) {
            // Efetua a devolução lógica no estoque do livro físico: incrementa a quantidade disponível
            LivroFisico livro = livroDAO.buscarPorId(emp.getLivroId());
            if (livro != null) {
                livro.setQtdDisponivel(livro.getQtdDisponivel() + 1);
                livroDAO.atualizar(livro);
            }
            
            String msg = "Devolução efetuada com sucesso!";
            if (multaAcumulada > 0) {
                msg += " Multa gerada por atraso: R$ " + String.format("%.2f", multaAcumulada);
            }
            response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"" + msg + "\", \"multa\": " + multaAcumulada + "}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Falha interna ao gravar registro de devolução.\"}");
        }
    }

    private void processarAprovacao(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado)
            throws SQLException, IOException {

        if (!usuarioLogado.ehAdministrador()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"erro\": \"A aprovação de solicitações é exclusiva para administradores.\"}");
            return;
        }

        String empIdStr = request.getParameter("emprestimoId");
        if (empIdStr == null || empIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"ID da solicitação é obrigatório.\"}");
            return;
        }

        int empId = Integer.parseInt(empIdStr);
        Emprestimo emp = emprestimoDAO.buscarPorId(empId);
        if (emp == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"erro\": \"Solicitação não encontrada.\"}");
            return;
        }

        if (!"SOLICITADO".equals(emp.getStatus())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Esta solicitação não está pendente de aprovação.\"}");
            return;
        }

        LivroFisico livro = livroDAO.buscarPorId(emp.getLivroId());
        if (livro == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"erro\": \"Livro físico não encontrado no acervo.\"}");
            return;
        }

        if (livro.getQtdDisponivel() <= 0) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Não há exemplar disponível para aprovar esta solicitação.\"}");
            return;
        }

        LocalDate hoje = LocalDate.now();
        LocalDate dataPrevista = hoje.plusDays(7);
        boolean aprovado = emprestimoDAO.aprovarSolicitacao(empId, hoje, dataPrevista);

        if (aprovado) {
            livro.processarRegraEmprestimo();
            livroDAO.atualizar(livro);
            response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Solicitação aprovada. O empréstimo foi iniciado e a devolução ficou prevista para " + dataPrevista + ".\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Falha ao aprovar a solicitação.\"}");
        }
    }

    private void processarRecusa(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado)
            throws SQLException, IOException {

        if (!usuarioLogado.ehAdministrador()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"erro\": \"A recusa de solicitações é exclusiva para administradores.\"}");
            return;
        }

        String empIdStr = request.getParameter("emprestimoId");
        if (empIdStr == null || empIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"ID da solicitação é obrigatório.\"}");
            return;
        }

        int empId = Integer.parseInt(empIdStr);
        Emprestimo emp = emprestimoDAO.buscarPorId(empId);
        if (emp == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"erro\": \"Solicitação não encontrada.\"}");
            return;
        }

        if (!"SOLICITADO".equals(emp.getStatus())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"erro\": \"Esta solicitação não está pendente de aprovação.\"}");
            return;
        }

        boolean recusado = emprestimoDAO.recusarSolicitacao(empId);
        if (recusado) {
            response.getWriter().write("{\"sucesso\": true, \"mensagem\": \"Solicitação recusada sem alterar o estoque.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"erro\": \"Falha ao recusar a solicitação.\"}");
        }
    }
}
