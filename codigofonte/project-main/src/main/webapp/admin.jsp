<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Biblioteca - Painel do Administrador</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/css/bootstrap.min.css" rel="stylesheet" onerror="this.onerror=null;this.href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css';">
    <!-- FontAwesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- SweetAlert2 -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    
    <style>
        :root {
            --bg-primary: #0f172a;
            --bg-secondary: #1e293b;
            --accent: #6366f1;
            --accent-hover: #4f46e5;
            --text-main: #f8fafc;
            --text-muted: #94a3b8;
            --glass-bg: rgba(30, 41, 59, 0.7);
            --glass-border: rgba(255, 255, 255, 0.08);
        }

        body {
            font-family: 'Outfit', sans-serif;
            background-color: var(--bg-primary);
            color: var(--text-main);
            min-height: 100vh;
        }

        .navbar {
            background-color: rgba(15, 23, 42, 0.8) !important;
            backdrop-filter: blur(12px);
            border-bottom: 1px solid var(--glass-border);
        }

        .navbar-brand {
            font-weight: 700;
            color: var(--text-main) !important;
        }

        .navbar-brand span {
            color: var(--accent);
        }

        .nav-link {
            color: var(--text-muted) !important;
            font-weight: 500;
        }

        .nav-link:hover {
            color: var(--text-main) !important;
        }

        .user-avatar {
            width: 38px;
            height: 38px;
            background: linear-gradient(135deg, #fbbf24, #f59e0b);
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            font-size: 0.95rem;
        }

        .card-custom {
            background: var(--glass-bg);
            border: 1px solid var(--glass-border);
            border-radius: 16px;
            backdrop-filter: blur(10px);
            padding: 24px;
            margin-bottom: 30px;
        }

        .btn-accent {
            background-color: var(--accent);
            color: white;
            font-weight: 600;
            border-radius: 8px;
            padding: 8px 18px;
            border: none;
            transition: all 0.3s ease;
        }

        .btn-accent:hover {
            background-color: var(--accent-hover);
        }

        .btn-outline-custom {
            border: 1px solid var(--glass-border);
            color: var(--text-main);
            font-weight: 600;
            border-radius: 8px;
            background: transparent;
            transition: all 0.3s ease;
        }

        .btn-outline-custom:hover {
            background: rgba(255, 255, 255, 0.05);
            color: var(--text-main);
        }

        .table-custom {
            color: var(--text-main);
            border-collapse: separate;
            border-spacing: 0 10px;
            --bs-table-bg: transparent;
            --bs-table-color: var(--text-main);
        }

        .table-custom tbody tr {
            background: rgba(15, 23, 42, 0.4);
            transition: all 0.3s ease;
        }

        .table-custom tbody tr:hover {
            background: rgba(15, 23, 42, 0.6);
        }

        .table-custom th {
            color: #d7def0 !important;
            font-weight: 500;
            border: none;
            padding: 12px 18px;
            background: transparent !important;
        }

        .table-custom td {
            border: none;
            padding: 18px;
            vertical-align: middle;
            background: rgba(15, 23, 42, 0.72) !important;
            color: var(--text-main) !important;
        }

        .table-custom .text-white,
        .table-custom .font-weight-bold {
            color: #f8fafc !important;
        }

        .table-custom td:first-child, .table-custom th:first-child {
            border-top-left-radius: 12px;
            border-bottom-left-radius: 12px;
        }

        .table-custom td:last-child, .table-custom th:last-child {
            border-top-right-radius: 12px;
            border-bottom-right-radius: 12px;
        }

        .tab-btn {
            color: var(--text-muted);
            font-weight: 600;
            padding: 10px 20px;
            border-radius: 8px;
            transition: all 0.3s ease;
            border: none;
            background: transparent;
            margin-right: 10px;
        }

        .tab-btn.active {
            background-color: rgba(99, 102, 241, 0.1);
            color: #818cf8;
        }

        .modal-content-custom {
            background-color: var(--bg-secondary);
            border: 1px solid var(--glass-border);
            color: var(--text-main);
            border-radius: 16px;
        }

        .modal-header-custom {
            border-bottom: 1px solid var(--glass-border);
            padding: 20px;
        }

        .modal-footer-custom {
            border-top: 1px solid var(--glass-border);
            padding: 20px;
        }

        .form-control-custom {
            background-color: rgba(15, 23, 42, 0.78);
            border: 1px solid var(--glass-border);
            color: var(--text-main);
            border-radius: 8px;
            padding: 10px 14px;
        }

        .form-control-custom:focus {
            background-color: rgba(15, 23, 42, 0.92);
            border-color: var(--accent);
            color: var(--text-main);
            box-shadow: none;
        }

        .form-control-custom::placeholder {
            color: #cbd5e1;
            opacity: 1;
        }

        .form-control-custom::-webkit-input-placeholder {
            color: #cbd5e1;
        }
    </style>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg fixed-top py-3">
        <div class="container">
            <a class="navbar-brand" href="#"><i class="fa-solid fa-book-bookmark me-2"></i>BIBLIO<span>MARC21</span></a>
            <button class="navbar-toggler border-0 text-white" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <c:set var="nomeExibicao" value="${sessionScope.usuarioLogado.nome}" />
                <c:set var="nomeExibicao" value="${fn:replace(nomeExibicao, ' (Administrador)', '')}" />
                <c:set var="nomeExibicao" value="${fn:replace(nomeExibicao, ' (Leitora)', '')}" />
                <c:set var="nomeExibicao" value="${fn:replace(nomeExibicao, ' (Leitor)', '')}" />
                <c:set var="nomeExibicao" value="${fn:replace(nomeExibicao, ' (Leitor Pendente)', '')}" />
                <ul class="navbar-nav ms-auto align-items-center gap-3">
                    <li class="nav-item">
                        <span class="badge bg-warning-subtle text-warning border border-warning-subtle px-3 py-2 rounded-pill"><i class="fa-solid fa-user-shield me-1"></i>Administrador</span>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link d-flex align-items-center gap-2" href="#" role="button" data-bs-toggle="dropdown">
                            <div class="user-avatar"><c:out value="${nomeExibicao.substring(0,1).toUpperCase()}"/></div>
                            <span>Olá, <b><c:out value="${nomeExibicao}"/></b></span>
                            <i class="fa-solid fa-chevron-down ms-1" style="font-size: 0.8rem;"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end dropdown-menu-dark">
                            <li><a class="dropdown-item" href="api/login"><i class="fa-solid fa-right-from-bracket me-2 text-danger"></i>Sair</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Dashboard Content -->
    <div class="container mt-5 pt-5 pb-5">
        <div class="row mb-4">
            <div class="col-md-12">
                <div class="d-flex justify-content-start align-items-center gap-2 mb-4">
                    <button class="tab-btn active" id="tabBooks"><i class="fa-solid fa-book me-2"></i>Gerenciar Acervo</button>
                    <button class="tab-btn" id="tabReturns"><i class="fa-solid fa-hand-holding-hand me-2"></i>Baixa de Empréstimos</button>
                    <button class="tab-btn" id="tabUsers"><i class="fa-solid fa-users-gear me-2"></i>Usuários</button>
                    <button class="tab-btn" id="tabConfigs"><i class="fa-solid fa-sliders me-2"></i>Configurações Gerais</button>
                    <button class="tab-btn" id="tabProfileAdmin"><i class="fa-solid fa-user-gear me-2"></i>Meu Perfil</button>
                </div>
            </div>
        </div>

        <!-- ABA 1: GERENCIAR ACERVO -->
        <div id="booksSection" class="tab-section">
            <div class="card-custom">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2 class="h5 font-weight-bold mb-0">Livros Físicos Catalogados</h2>
                    <button class="btn btn-accent" onclick="abrirModalCadastroLivro()"><i class="fa-solid fa-plus me-2"></i>Cadastrar Livro</button>
                </div>

                <div class="table-responsive">
                    <table class="table table-custom">
                        <thead>
                            <tr>
                                <th>Código/ID</th>
                                <th>ISBN (Tag 020)</th>
                                <th>Título Principal (Tag 245)</th>
                                <th>Autor Principal (Tag 100)</th>
                                <th>Estoque Físico</th>
                                <th class="text-end">Ações</th>
                            </tr>
                        </thead>
                        <tbody id="adminBooksTableBody">
                            <!-- Livros carregados via AJAX -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- ABA 2: BAIXA DE EMPRÉSTIMOS -->
        <div id="returnsSection" class="tab-section d-none">
            <div class="card-custom">
                <h2 class="h5 font-weight-bold mb-4">Solicitações e Empréstimos em Andamento</h2>
                <div class="table-responsive">
                    <table class="table table-custom">
                        <thead>
                            <tr>
                                <th>Leitor</th>
                                <th>Livro</th>
                                <th>Data Solicitação/Retirada</th>
                                <th>Devolução Prevista</th>
                                <th>Status</th>
                                <th>Multa Acumulada</th>
                                <th class="text-end">Ações</th>
                            </tr>
                        </thead>
                        <tbody id="adminLoansTableBody">
                            <!-- Empréstimos carregados via AJAX -->
                        </tbody>
                    </table>
                </div>
                <div id="noActiveLoansMessage" class="text-center py-5 d-none">
                    <i class="fa-solid fa-check-double fa-3x mb-3" style="color: #cbd5e1;"></i>
                    <p class="fw-semibold mb-1" style="color: #f8fafc;">Não há solicitações pendentes nem empréstimos ativos.</p>
                    <p class="mb-0" style="color: #cbd5e1;">Quando um leitor solicitar ou retirar um livro, o registro aparecerá aqui.</p>
                </div>
            </div>
        </div>

        <!-- ABA 3: USUÁRIOS -->
        <div id="usersSection" class="tab-section d-none">
            <div class="card-custom">
                <h2 class="h5 font-weight-bold mb-4">Controle de Acesso e Níveis</h2>
                <div class="table-responsive">
                    <table class="table table-custom">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>E-mail</th>
                                <th>Perfil</th>
                                <th>Status</th>
                                <th class="text-end">Ações</th>
                            </tr>
                        </thead>
                        <tbody id="usersTableBody">
                            <!-- Usuários carregados via AJAX -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- ABA 4: CONFIGURAÇÕES GERAIS -->
        <div id="configsSection" class="tab-section d-none">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card-custom">
                        <h2 class="h5 font-weight-bold mb-4"><i class="fa-solid fa-circle-info me-2 text-accent"></i>Parâmetros Dinâmicos</h2>
                        
                        <form id="configForm">
                            <div class="mb-3">
                                <label for="multaDiaria" class="form-label">Multa Diária por Atraso (R$)</label>
                                <input type="number" step="0.01" min="0.00" id="multaDiaria" name="multaDiaria" class="form-control form-control-custom" required>
                            </div>
                            
                            <div class="mb-4">
                                <label for="limiteRenovacoes" class="form-label">Limite Máximo de Renovações</label>
                                <input type="number" min="0" id="limiteRenovacoes" name="limiteRenovacoes" class="form-control form-control-custom" required>
                            </div>

                            <button type="submit" class="btn btn-accent w-100 py-2">Salvar Configurações</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- ABA 5: PERFIL DO ADMINISTRADOR -->
        <div id="profileAdminSection" class="tab-section d-none">
            <div class="card-custom">
                <h2 class="h5 font-weight-bold mb-4">Atualização de Dados do Administrador</h2>
                <form id="profileAdminForm" class="row g-3">
                    <div class="col-md-6">
                        <label for="adminPerfilNome" class="form-label">Nome</label>
                        <input type="text" id="adminPerfilNome" name="nome" class="form-control bg-dark border-secondary text-white" value="<c:out value='${sessionScope.usuarioLogado.nome}'/>" required>
                    </div>
                    <div class="col-md-6">
                        <label for="adminPerfilEmail" class="form-label">E-mail</label>
                        <input type="email" id="adminPerfilEmail" class="form-control bg-dark border-secondary text-white" value="<c:out value='${sessionScope.usuarioLogado.email}'/>" disabled>
                    </div>
                    <div class="col-md-6">
                        <label for="adminPerfilSenha" class="form-label">Nova senha</label>
                        <input type="password" id="adminPerfilSenha" name="senha" class="form-control bg-dark border-secondary text-white" minlength="6" placeholder="Preencha apenas se quiser alterar">
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-accent"><i class="fa-solid fa-floppy-disk me-2"></i>Salvar Dados</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- MODAL: CADASTRAR LIVRO -->
    <div class="modal fade" id="addBookModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content modal-content-custom">
                <div class="modal-header modal-header-custom">
                    <h5 class="modal-title font-weight-bold" id="bookModalTitle"><i class="fa-solid fa-book-medical me-2 text-accent"></i>Novo Livro no Acervo</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form id="addBookForm">
                    <input type="hidden" id="bookAcao" name="acao" value="cadastrar">
                    <input type="hidden" id="bookId" name="id">
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label for="isbn" class="form-label">ISBN (MARC Tag 020)</label>
                            <input type="text" id="isbn" name="isbn" class="form-control form-control-custom" placeholder="ex: 978-85-359-0277-7" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="titulo" class="form-label">Título da Obra (MARC Tag 245)</label>
                            <input type="text" id="titulo" name="titulo" class="form-control form-control-custom" placeholder="ex: Dom Casmurro" required>
                        </div>

                        <div class="mb-3">
                            <label for="autor" class="form-label">Autor Principal (MARC Tag 100)</label>
                            <input type="text" id="autor" name="autor" class="form-control form-control-custom" placeholder="ex: Machado de Assis" required>
                        </div>

                        <div class="mb-3">
                            <label for="qtdTotal" class="form-label">Quantidade de Exemplares Adquiridos</label>
                            <input type="number" min="1" id="qtdTotal" name="qtdTotal" class="form-control form-control-custom" value="1" required>
                        </div>
                    </div>
                    <div class="modal-footer modal-footer-custom">
                        <button type="button" class="btn btn-outline-custom" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-accent" id="bookSubmitButton">Cadastrar Livro</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- jQuery, Bootstrap & SweetAlert2 JS -->
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script>
        $(document).ready(function() {
            // Alternância de Abas
            $("#tabBooks").on("click", function() {
                $(".tab-btn").removeClass("active");
                $(this).addClass("active");
                $(".tab-section").addClass("d-none");
                $("#booksSection").removeClass("d-none");
                carregarAcervoAdmin();
            });

            $("#tabReturns").on("click", function() {
                $(".tab-btn").removeClass("active");
                $(this).addClass("active");
                $(".tab-section").addClass("d-none");
                $("#returnsSection").removeClass("d-none");
                carregarEmprestimosAdmin();
            });

            $("#tabConfigs").on("click", function() {
                $(".tab-btn").removeClass("active");
                $(this).addClass("active");
                $(".tab-section").addClass("d-none");
                $("#configsSection").removeClass("d-none");
                carregarConfiguracoes();
            });

            $("#tabUsers").on("click", function() {
                $(".tab-btn").removeClass("active");
                $(this).addClass("active");
                $(".tab-section").addClass("d-none");
                $("#usersSection").removeClass("d-none");
                carregarUsuarios();
            });

            $("#tabProfileAdmin").on("click", function() {
                $(".tab-btn").removeClass("active");
                $(this).addClass("active");
                $(".tab-section").addClass("d-none");
                $("#profileAdminSection").removeClass("d-none");
            });

            $("#profileAdminForm").on("submit", function(e) {
                e.preventDefault();
                $.ajax({
                    url: "api/perfil",
                    type: "POST",
                    data: $(this).serialize(),
                    dataType: "json",
                    success: function(response) {
                        $("#adminPerfilSenha").val("");
                        Swal.fire({
                            icon: 'success',
                            title: 'Perfil Atualizado',
                            text: response.mensagem,
                            background: '#1e293b',
                            color: '#f8fafc',
                            confirmButtonColor: '#6366f1'
                        });
                    },
                    error: function(xhr) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Erro ao Atualizar',
                            text: xhr.responseJSON ? xhr.responseJSON.erro : "Não foi possível atualizar os dados.",
                            background: '#1e293b',
                            color: '#f8fafc',
                            confirmButtonColor: '#6366f1'
                        });
                    }
                });
            });

            // Carregamento inicial do acervo
            carregarAcervoAdmin();

            // Envio de formulário de cadastro de Livro
            $("#addBookForm").on("submit", function(e) {
                e.preventDefault();

                $.ajax({
                    url: "api/livros",
                    type: "POST",
                    data: $(this).serialize(),
                    dataType: "json",
                    success: function(response) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Sucesso!',
                            text: response.mensagem,
                            background: '#1e293b',
                            color: '#f8fafc',
                            confirmButtonColor: '#6366f1'
                        });
                        // Fecha o modal e recarrega
                        $("#addBookModal").modal('hide');
                        $("#addBookForm")[0].reset();
                        abrirModalCadastroLivro(false);
                        carregarAcervoAdmin();
                    },
                    error: function(xhr) {
                        let erro = "Falha ao cadastrar livro no acervo.";
                        if (xhr.responseJSON && xhr.responseJSON.erro) {
                            erro = xhr.responseJSON.erro;
                        }
                        Swal.fire({
                            icon: 'error',
                            title: 'Erro de Validação',
                            text: erro,
                            background: '#1e293b',
                            color: '#f8fafc',
                            confirmButtonColor: '#6366f1'
                        });
                    }
                });
            });

            // Envio do formulário de Configurações
            $("#configForm").on("submit", function(e) {
                e.preventDefault();

                $.ajax({
                    url: "api/admin/configuracoes",
                    type: "POST",
                    data: $(this).serialize(),
                    dataType: "json",
                    success: function(response) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Atualizado!',
                            text: response.mensagem,
                            background: '#1e293b',
                            color: '#f8fafc',
                            confirmButtonColor: '#6366f1'
                        });
                    },
                    error: function(xhr) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Falha na Gravação',
                            text: xhr.responseJSON ? xhr.responseJSON.erro : "Erro desconhecido.",
                            background: '#1e293b',
                            color: '#f8fafc',
                            confirmButtonColor: '#6366f1'
                        });
                    }
                });
            });
        });

        function abrirModalCadastroLivro(exibir = true) {
            $("#addBookForm")[0].reset();
            $("#bookAcao").val("cadastrar");
            $("#bookId").val("");
            $("#qtdTotal").val(1);
            $("#bookModalTitle").html('<i class="fa-solid fa-book-medical me-2 text-accent"></i>Novo Livro no Acervo');
            $("#bookSubmitButton").text("Cadastrar Livro");
            if (exibir) {
                $("#addBookModal").modal("show");
            }
        }

        function abrirModalEdicaoLivro(id, isbn, titulo, autor, qtdTotal) {
            $("#bookAcao").val("atualizar");
            $("#bookId").val(id);
            $("#isbn").val(isbn);
            $("#titulo").val(titulo);
            $("#autor").val(autor);
            $("#qtdTotal").val(qtdTotal);
            $("#bookModalTitle").html('<i class="fa-solid fa-pen-to-square me-2 text-accent"></i>Editar Livro do Acervo');
            $("#bookSubmitButton").text("Salvar Alterações");
            $("#addBookModal").modal("show");
        }

        function textoSeguro(valor) {
            return String(valor || "")
                .replace(/\\/g, "\\\\")
                .replace(/'/g, "\\'")
                .replace(/"/g, "&quot;");
        }

        function nomeSemRotulo(nome) {
            return String(nome || "").replace(/\s*\((Administrador|Admin|Leitor|Leitora|Cliente|Leitor Pendente|Cliente Pendente)\)\s*$/i, "");
        }

        // 1. Carrega Acervo no Painel Admin via AJAX
        function carregarAcervoAdmin() {
            $.ajax({
                url: "api/livros",
                type: "GET",
                dataType: "json",
                success: function(livros) {
                    let tbody = $("#adminBooksTableBody");
                    tbody.empty();

                    livros.forEach(function(livro) {
                        let row = '<tr>' +
                            '<td><b># ' + livro.id + '</b></td>' +
                            '<td class="text-accent font-weight-bold">' + livro.isbn + '</td>' +
                            '<td class="font-weight-bold text-white">' + livro.titulo + '</td>' +
                            '<td>' + livro.autor + '</td>' +
                            '<td><span class="badge bg-secondary px-3 py-2 rounded">' + livro.qtdDisponivel + ' de ' + livro.qtdTotal + ' unidades</span></td>' +
                            '<td class="text-end"><button class="btn btn-sm btn-outline-custom" onclick="abrirModalEdicaoLivro(' + livro.id + ', \'' + textoSeguro(livro.isbn) + '\', \'' + textoSeguro(livro.titulo) + '\', \'' + textoSeguro(livro.autor) + '\', ' + livro.qtdTotal + ')"><i class="fa-solid fa-pen-to-square me-1"></i>Editar</button></td>' +
                            '</tr>';
                        tbody.append(row);
                    });
                }
            });
        }

        // 2. Carrega solicitações pendentes e empréstimos ativos para operação via AJAX
        function carregarEmprestimosAdmin() {
            $.ajax({
                url: "api/emprestimos",
                type: "GET",
                dataType: "json",
                success: function(emprestimos) {
                    let tbody = $("#adminLoansTableBody");
                    tbody.empty();

                    let ativos = emprestimos.filter(e => {
                        let status = e.status || (e.dataDevolucaoEfetiva ? "DEVOLVIDO" : "ATIVO");
                        return status === "SOLICITADO" || status === "ATIVO";
                    });

                    if (ativos.length === 0) {
                        $("#noActiveLoansMessage").removeClass("d-none");
                        return;
                    }
                    $("#noActiveLoansMessage").addClass("d-none");

                    $.when(
                        $.ajax({ url: "api/livros", type: "GET", dataType: "json" }),
                        $.ajax({ url: "api/admin/usuarios", type: "GET", dataType: "json" })
                    ).done(function(livrosResponse, usuariosResponse) {
                            const livros = livrosResponse[0];
                            const usuarios = usuariosResponse[0];
                            const livroMap = {};
                            const usuarioMap = {};
                            livros.forEach(l => { livroMap[l.id] = l.titulo; });
                            usuarios.forEach(u => { usuarioMap[u.id] = nomeSemRotulo(u.nome); });

                            ativos.forEach(function(emp) {
                                let status = emp.status || (emp.dataDevolucaoEfetiva ? "DEVOLVIDO" : "ATIVO");
                                let multaText = emp.multaAcumulada > 0
                                    ? '<span class="text-danger font-weight-bold">R$ ' + emp.multaAcumulada.toFixed(2) + '</span>'
                                    : '<span class="text-success">Em dia</span>';
                                if (status === "SOLICITADO") {
                                    multaText = '<span class="text-light">-</span>';
                                }

                                let statusBadge = status === "SOLICITADO"
                                    ? '<span class="badge bg-info-subtle text-info border border-info-subtle rounded-pill">Solicitado</span>'
                                    : '<span class="badge bg-warning-subtle text-warning border border-warning-subtle rounded-pill">Ativo</span>';

                                let acoes = status === "SOLICITADO"
                                    ? '<button class="btn btn-sm btn-accent me-2" onclick="aprovarSolicitacao(' + emp.id + ')"><i class="fa-solid fa-check me-1"></i>Aprovar</button>' +
                                      '<button class="btn btn-sm btn-outline-custom" onclick="recusarSolicitacao(' + emp.id + ')"><i class="fa-solid fa-xmark me-1"></i>Recusar</button>'
                                    : '<button class="btn btn-sm btn-accent" onclick="baixarDevolucao(' + emp.id + ')"><i class="fa-solid fa-clipboard-check me-1"></i>Registrar Devolução</button>';

                                let tituloLivro = livroMap[emp.livroId] || ('ID ' + emp.livroId);
                                let nomeLeitor = usuarioMap[emp.usuarioId] || ('Leitor ID #' + emp.usuarioId);

                                let row = '<tr>' +
                                    '<td class="font-weight-bold text-white">' + nomeLeitor + ' <span style="color: #cbd5e1;">#' + emp.usuarioId + '</span></td>' +
                                    '<td class="text-white font-weight-bold">' + tituloLivro + '</td>' +
                                    '<td>' + emp.dataEmprestimo + '</td>' +
                                    '<td>' + (status === "SOLICITADO" ? '-' : emp.dataDevolucaoPrevista) + '</td>' +
                                    '<td>' + statusBadge + '</td>' +
                                    '<td>' + multaText + '</td>' +
                                    '<td class="text-end">' +
                                        acoes +
                                    '</td>' +
                                    '</tr>';
                                tbody.append(row);
                            });
                    });
                }
            });
        }

        // 3. Carrega usuários para controle de acesso
        function carregarUsuarios() {
            $.ajax({
                url: "api/admin/usuarios",
                type: "GET",
                dataType: "json",
                success: function(usuarios) {
                    let tbody = $("#usersTableBody");
                    tbody.empty();

                    usuarios.forEach(function(usuario) {
                        let adminSelected = usuario.tipoUsuario === "ADMIN" ? "selected" : "";
                        let clienteSelected = usuario.tipoUsuario === "CLIENTE" ? "selected" : "";
                        let ativoChecked = usuario.ativo ? "checked" : "";
                        let row = '<tr>' +
                            '<td><b># ' + usuario.id + '</b></td>' +
                            '<td class="font-weight-bold text-white">' + nomeSemRotulo(usuario.nome) + '</td>' +
                            '<td>' + usuario.email + '</td>' +
                            '<td><select class="form-select form-select-sm bg-dark border-secondary text-white" id="tipoUsuario_' + usuario.id + '">' +
                                '<option value="CLIENTE" ' + clienteSelected + '>Leitor</option>' +
                                '<option value="ADMIN" ' + adminSelected + '>Administrador</option>' +
                            '</select></td>' +
                            '<td><div class="form-check form-switch">' +
                                '<input class="form-check-input" type="checkbox" id="ativo_' + usuario.id + '" ' + ativoChecked + '>' +
                            '</div></td>' +
                            '<td class="text-end"><button class="btn btn-sm btn-accent" onclick="salvarUsuario(' + usuario.id + ')"><i class="fa-solid fa-floppy-disk me-1"></i>Salvar</button></td>' +
                            '</tr>';
                        tbody.append(row);
                    });
                }
            });
        }

        function salvarUsuario(usuarioId) {
            $.ajax({
                url: "api/admin/usuarios",
                type: "POST",
                data: {
                    id: usuarioId,
                    tipoUsuario: $("#tipoUsuario_" + usuarioId).val(),
                    ativo: $("#ativo_" + usuarioId).is(":checked")
                },
                dataType: "json",
                success: function(response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Usuário Atualizado',
                        text: response.mensagem,
                        background: '#1e293b',
                        color: '#f8fafc',
                        confirmButtonColor: '#6366f1'
                    });
                },
                error: function(xhr) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Falha ao Atualizar',
                        text: xhr.responseJSON ? xhr.responseJSON.erro : "Erro desconhecido.",
                        background: '#1e293b',
                        color: '#f8fafc',
                        confirmButtonColor: '#6366f1'
                    });
                }
            });
        }

        function aprovarSolicitacao(emprestimoId) {
            Swal.fire({
                title: 'Aprovar Solicitação',
                text: "Deseja aprovar a retirada deste livro e iniciar o prazo de 7 dias?",
                icon: 'question',
                showCancelButton: true,
                background: '#1e293b',
                color: '#f8fafc',
                confirmButtonColor: '#6366f1',
                cancelButtonColor: '#475569',
                confirmButtonText: 'Aprovar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: "api/emprestimos",
                        type: "POST",
                        data: {
                            acao: "aprovar",
                            emprestimoId: emprestimoId
                        },
                        dataType: "json",
                        success: function(response) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Solicitação Aprovada',
                                text: response.mensagem,
                                background: '#1e293b',
                                color: '#f8fafc',
                                confirmButtonColor: '#6366f1'
                            });
                            carregarEmprestimosAdmin();
                        },
                        error: function(xhr) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Falha ao Aprovar',
                                text: xhr.responseJSON ? xhr.responseJSON.erro : "Erro desconhecido.",
                                background: '#1e293b',
                                color: '#f8fafc',
                                confirmButtonColor: '#6366f1'
                            });
                        }
                    });
                }
            });
        }

        function recusarSolicitacao(emprestimoId) {
            Swal.fire({
                title: 'Recusar Solicitação',
                text: "Deseja recusar esta solicitação sem alterar o estoque?",
                icon: 'warning',
                showCancelButton: true,
                background: '#1e293b',
                color: '#f8fafc',
                confirmButtonColor: '#6366f1',
                cancelButtonColor: '#475569',
                confirmButtonText: 'Recusar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: "api/emprestimos",
                        type: "POST",
                        data: {
                            acao: "recusar",
                            emprestimoId: emprestimoId
                        },
                        dataType: "json",
                        success: function(response) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Solicitação Recusada',
                                text: response.mensagem,
                                background: '#1e293b',
                                color: '#f8fafc',
                                confirmButtonColor: '#6366f1'
                            });
                            carregarEmprestimosAdmin();
                        },
                        error: function(xhr) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Falha ao Recusar',
                                text: xhr.responseJSON ? xhr.responseJSON.erro : "Erro desconhecido.",
                                background: '#1e293b',
                                color: '#f8fafc',
                                confirmButtonColor: '#6366f1'
                            });
                        }
                    });
                }
            });
        }

        // 4. Registra a Baixa de Devolução via AJAX
        function baixarDevolucao(emprestimoId) {
            Swal.fire({
                title: 'Confirmar Devolução',
                text: "Deseja registrar o retorno deste exemplar físico e liberar a vaga no estoque?",
                icon: 'warning',
                showCancelButton: true,
                background: '#1e293b',
                color: '#f8fafc',
                confirmButtonColor: '#6366f1',
                cancelButtonColor: '#475569',
                confirmButtonText: 'Registrar Devolução',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: "api/emprestimos",
                        type: "POST",
                        data: {
                            acao: "devolver",
                            emprestimoId: emprestimoId
                        },
                        dataType: "json",
                        success: function(response) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Devolução Registrada!',
                                text: response.mensagem,
                                background: '#1e293b',
                                color: '#f8fafc',
                                confirmButtonColor: '#6366f1'
                            });
                            carregarEmprestimosAdmin();
                        },
                        error: function(xhr) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Erro de Devolução',
                                text: xhr.responseJSON ? xhr.responseJSON.erro : "Erro desconhecido.",
                                background: '#1e293b',
                                color: '#f8fafc',
                                confirmButtonColor: '#6366f1'
                            });
                        }
                    });
                }
            });
        }

        // 5. Carrega configurações globais via AJAX
        function carregarConfiguracoes() {
            $.ajax({
                url: "api/admin/configuracoes",
                type: "GET",
                dataType: "json",
                success: function(config) {
                    $("#multaDiaria").val(config.multaDiaria);
                    $("#limiteRenovacoes").val(config.limiteRenovacoes);
                }
            });
        }
    </script>
</body>
</html>
