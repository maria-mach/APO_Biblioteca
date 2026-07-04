<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Biblioteca - Painel do Leitor</title>
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
            background: linear-gradient(135deg, #818cf8, #6366f1);
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
            transform: translateY(-1px);
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

        .book-item-card {
            background: rgba(15, 23, 42, 0.72);
            border: 1px solid var(--glass-border);
            border-radius: 12px;
            padding: 20px;
            height: 100%;
            transition: all 0.3s ease;
            color: #f8fafc;
        }

        .book-item-card:hover {
            border-color: rgba(99, 102, 241, 0.2);
            background: rgba(15, 23, 42, 0.86);
        }

        .book-card-author {
            color: #d7def0;
            font-size: 0.9rem;
        }

        .book-card-meta {
            color: #cbd5e1;
            font-size: 0.84rem;
            border-top: 1px solid rgba(255, 255, 255, 0.12);
            padding-top: 10px;
        }

        .book-card-meta strong {
            color: #f8fafc;
            font-weight: 600;
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
            color: var(--text-muted);
            font-weight: 500;
            border: none;
            padding: 12px 18px;
            background: transparent;
        }

        .table-custom td {
            border: none;
            padding: 18px;
            vertical-align: middle;
            background: rgba(15, 23, 42, 0.4);
            color: var(--text-main);
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

        #searchBookInput {
            background-color: rgba(15, 23, 42, 0.92) !important;
            border-color: rgba(148, 163, 184, 0.35) !important;
            color: var(--text-main) !important;
            caret-color: var(--text-main);
        }

        #searchBookInput::placeholder {
            color: #94a3b8 !important;
            opacity: 1;
        }

        #searchBookInput:focus {
            background-color: rgba(15, 23, 42, 1) !important;
            border-color: var(--accent) !important;
            color: var(--text-main) !important;
            box-shadow: 0 0 0 0.2rem rgba(99, 102, 241, 0.2);
        }

        #searchBookInput:-webkit-autofill,
        #searchBookInput:-webkit-autofill:hover,
        #searchBookInput:-webkit-autofill:focus {
            -webkit-text-fill-color: #f8fafc;
            box-shadow: 0 0 0 1000px #0f172a inset;
            transition: background-color 9999s ease-in-out 0s;
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
                        <span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle px-3 py-2 rounded-pill"><i class="fa-solid fa-circle-user me-1 text-accent"></i>Leitor</span>
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
                    <button class="tab-btn active" id="tabCatalog"><i class="fa-solid fa-book-open me-2"></i>Catálogo de Livros</button>
                    <button class="tab-btn" id="tabLoans"><i class="fa-solid fa-handshake me-2"></i>Meus Empréstimos</button>
                    <button class="tab-btn" id="tabProfile"><i class="fa-solid fa-user-gear me-2"></i>Meu Perfil</button>
                </div>
            </div>
        </div>

        <!-- ABA 1: CATÁLOGO -->
        <div id="catalogSection" class="tab-section">
            <div class="card-custom">
                <div class="row align-items-center justify-content-between mb-4">
                    <div class="col-md-6">
                        <h2 class="h5 font-weight-bold mb-0">Pesquisa e Reserva de Acervo</h2>
                    </div>
                    <div class="col-md-6 mt-3 mt-md-0">
                        <div class="input-group">
                            <input type="text" id="searchBookInput" class="form-control bg-dark border-secondary text-white" placeholder="Pesquisar por título, autor, ISBN...">
                        </div>
                    </div>
                </div>

                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4" id="booksGrid">
                    <!-- Livros carregados via AJAX -->
                </div>
            </div>
        </div>

        <!-- ABA 2: MEUS EMPRÉSTIMOS -->
        <div id="loansSection" class="tab-section d-none">
            <div class="card-custom">
                <h2 class="h5 font-weight-bold mb-4">Seu Histórico de Empréstimos</h2>
                <div id="regularizationNotice" class="alert alert-warning d-none" role="alert">
                    <i class="fa-solid fa-circle-exclamation me-2"></i>
                    Você possui empréstimo com atraso ou multa pendente. Procure o administrador da biblioteca para registrar a devolução e regularizar a pendência.
                </div>
                <div class="table-responsive">
                    <table class="table table-custom">
                        <thead>
                            <tr>
                                <th>Livro</th>
                                <th>Data Empréstimo</th>
                                <th>Data Prevista</th>
                                <th>Status</th>
                                <th>Multa</th>
                                <th>Renovações</th>
                                <th class="text-end">Ações</th>
                            </tr>
                        </thead>
                        <tbody id="loansTableBody">
                            <!-- Empréstimos carregados via AJAX -->
                        </tbody>
                    </table>
                </div>
                <div id="noLoansMessage" class="text-center py-5 d-none">
                    <i class="fa-solid fa-handshake-slash fa-3x text-muted mb-3"></i>
                    <p class="text-muted">Você ainda não realizou nenhum empréstimo.</p>
                </div>
            </div>
        </div>

        <!-- ABA 3: PERFIL -->
        <div id="profileSection" class="tab-section d-none">
            <div class="card-custom">
                <h2 class="h5 font-weight-bold mb-4">Atualização de Dados</h2>
                <form id="profileForm" class="row g-3">
                    <div class="col-md-6">
                        <label for="perfilNome" class="form-label">Nome</label>
                        <input type="text" id="perfilNome" name="nome" class="form-control bg-dark border-secondary text-white" value="<c:out value='${sessionScope.usuarioLogado.nome}'/>" required>
                    </div>
                    <div class="col-md-6">
                        <label for="perfilEmail" class="form-label">E-mail</label>
                        <input type="email" id="perfilEmail" class="form-control bg-dark border-secondary text-white" value="<c:out value='${sessionScope.usuarioLogado.email}'/>" disabled>
                    </div>
                    <div class="col-md-6">
                        <label for="perfilSenha" class="form-label">Nova senha</label>
                        <input type="password" id="perfilSenha" name="senha" class="form-control bg-dark border-secondary text-white" minlength="6" placeholder="Preencha apenas se quiser alterar">
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-accent"><i class="fa-solid fa-floppy-disk me-2"></i>Salvar Dados</button>
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
            $("#tabCatalog").on("click", function() {
                $(".tab-btn").removeClass("active");
                $(this).addClass("active");
                $(".tab-section").addClass("d-none");
                $("#catalogSection").removeClass("d-none");
                carregarCatalogo("");
            });

            $("#tabLoans").on("click", function() {
                $(".tab-btn").removeClass("active");
                $(this).addClass("active");
                $(".tab-section").addClass("d-none");
                $("#loansSection").removeClass("d-none");
                carregarEmprestimos();
            });

            $("#tabProfile").on("click", function() {
                $(".tab-btn").removeClass("active");
                $(this).addClass("active");
                $(".tab-section").addClass("d-none");
                $("#profileSection").removeClass("d-none");
            });

            // Carregamento inicial do catálogo
            carregarCatalogo("");

            // Pesquisa dinâmica com debounce
            let timeout = null;
            $("#searchBookInput").on("input", function() {
                clearTimeout(timeout);
                let query = $(this).val();
                timeout = setTimeout(function() {
                    carregarCatalogo(query);
                }, 300);
            });

            $("#profileForm").on("submit", function(e) {
                e.preventDefault();
                $.ajax({
                    url: "api/perfil",
                    type: "POST",
                    data: $(this).serialize(),
                    dataType: "json",
                    success: function(response) {
                        $("#perfilSenha").val("");
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
                            text: xhr.responseJSON ? xhr.responseJSON.erro : "Não foi possível atualizar seus dados.",
                            background: '#1e293b',
                            color: '#f8fafc',
                            confirmButtonColor: '#6366f1'
                        });
                    }
                });
            });
        });

        // 1. Carrega o Catálogo de Livros via AJAX
        function carregarCatalogo(query) {
            $.ajax({
                url: "api/livros",
                type: "GET",
                dataType: "json",
                success: function(livros) {
                    let grid = $("#booksGrid");
                    grid.empty();

                    if (query.trim() !== "") {
                        let termo = query.toLowerCase();
                        livros = livros.filter(function(livro) {
                            return livro.titulo.toLowerCase().includes(termo) || 
                                   livro.autor.toLowerCase().includes(termo) || 
                                   livro.isbn.toLowerCase().includes(termo);
                        });
                    }

                    if (livros.length === 0) {
                        grid.append('<div class="col-12 text-center py-5"><p class="text-muted">Nenhum livro encontrado.</p></div>');
                        return;
                    }

                    livros.forEach(function(livro) {
                        let botaoSolicitar = livro.qtdDisponivel > 0
                            ? '<button class="btn btn-accent w-100 mt-3" onclick="solicitarEmprestimo(' + livro.id + ')"><i class="fa-solid fa-hand-holding-hand me-2"></i>Solicitar Empréstimo</button>'
                            : '<button class="btn btn-outline-custom w-100 mt-3" disabled><i class="fa-solid fa-ban me-2"></i>Indisponível</button>';

                        let badgeEstoque = livro.qtdDisponivel > 0
                            ? '<span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill">Disponível</span>'
                            : '<span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill">Esgotado</span>';

                        let card = '<div class="col">' +
                            '<div class="book-item-card d-flex flex-column justify-content-between">' +
                                '<div>' +
                                    '<div class="d-flex justify-content-between align-items-center mb-2">' +
                                        '<span class="text-accent font-weight-bold" style="font-size: 0.8rem;">MARC 020</span>' +
                                        badgeEstoque +
                                    '</div>' +
                                    '<h3 class="h6 text-white font-weight-bold mb-1">' + livro.titulo + '</h3>' +
                                    '<p class="book-card-author">Por: ' + livro.autor + '</p>' +
                                '</div>' +
                                '<div>' +
                                    '<div class="book-card-meta d-flex justify-content-between align-items-center">' +
                                        '<span>ISBN</span>' +
                                        '<strong>' + livro.isbn + '</strong>' +
                                    '</div>' +
                                    '<div class="book-card-meta d-flex justify-content-between align-items-center mt-1" style="border-top: none; padding-top: 0;">' +
                                        '<span>Estoque</span>' +
                                        '<strong>' + livro.qtdDisponivel + ' de ' + livro.qtdTotal + '</strong>' +
                                    '</div>' +
                                    botaoSolicitar +
                                '</div>' +
                            '</div>' +
                        '</div>';
                        grid.append(card);
                    });
                }
            });
        }

        // 2. Solicita Novo Empréstimo via AJAX
        function solicitarEmprestimo(livroId) {
            Swal.fire({
                title: 'Solicitar Empréstimo',
                text: "Sua solicitação será enviada ao administrador. O livro só entra como empréstimo depois da aprovação.",
                icon: 'question',
                showCancelButton: true,
                background: '#1e293b',
                color: '#f8fafc',
                confirmButtonColor: '#6366f1',
                cancelButtonColor: '#475569',
                confirmButtonText: 'Solicitar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: "api/emprestimos",
                        type: "POST",
                        data: {
                            acao: "cadastrar",
                            livroId: livroId
                        },
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
                            carregarCatalogo("");
                            carregarEmprestimos();
                        },
                        error: function(xhr) {
                            let erro = "Falha ao solicitar empréstimo.";
                            if (xhr.responseJSON && xhr.responseJSON.erro) {
                                erro = xhr.responseJSON.erro;
                            }
                            Swal.fire({
                                icon: 'error',
                                title: 'Não Permitido',
                                text: erro,
                                background: '#1e293b',
                                color: '#f8fafc',
                                confirmButtonColor: '#6366f1'
                            });
                        }
                    });
                }
            });
        }

        // 3. Carrega Empréstimos do Leitor via AJAX
        function carregarEmprestimos() {
            $.ajax({
                url: "api/emprestimos",
                type: "GET",
                dataType: "json",
                success: function(emprestimos) {
                    let tbody = $("#loansTableBody");
                    tbody.empty();

                    // Para carregar os dados textuais dos livros, fazemos um mapeamento
                    // ou trazemos dinamicamente. Para facilitar, o Servlet retorna o livro correspondente.
                    // No banco, o empréstimo tem livro_id.
                    // Para que o frontend mostre o TÍTULO do livro, vamos primeiro buscar a lista de livros
                    // e criar um mapa de ID -> Título.
                    $.ajax({
                        url: "api/livros",
                        type: "GET",
                        dataType: "json",
                        success: function(livros) {
                            const livroMap = {};
                            livros.forEach(l => { livroMap[l.id] = l.titulo; });

                            if (emprestimos.length === 0) {
                                $("#noLoansMessage").removeClass("d-none");
                                return;
                            }
                            $("#noLoansMessage").addClass("d-none");

                            let temPendencia = false;
                            emprestimos.forEach(function(emp) {
                                let statusBadge = "";
                                let estaAtrasado = false;
                                let status = emp.status || (emp.dataDevolucaoEfetiva ? "DEVOLVIDO" : "ATIVO");

                                if (status === "SOLICITADO") {
                                    statusBadge = '<span class="badge bg-info-subtle text-info border border-info-subtle rounded-pill">Aguardando aprovação</span>';
                                } else if (status === "RECUSADO") {
                                    statusBadge = '<span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle rounded-pill">Recusado</span>';
                                } else if (status === "DEVOLVIDO" || emp.dataDevolucaoEfetiva) {
                                    statusBadge = '<span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill">Devolvido</span>';
                                } else {
                                    let hoje = new Date();
                                    hoje.setHours(0, 0, 0, 0);
                                    // Divide a string "yyyy-MM-dd" em partes
                                    let partes = emp.dataDevolucaoPrevista.split("-");
                                    let vencimento = new Date(partes[0], partes[1] - 1, partes[2]);

                                    if (hoje > vencimento) {
                                        estaAtrasado = true;
                                        statusBadge = '<span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill">Atrasado</span>';
                                    } else {
                                        statusBadge = '<span class="badge bg-warning-subtle text-warning border border-warning-subtle rounded-pill">Ativo</span>';
                                    }
                                }

                                let btnRenovar = '';
                                if (status === "SOLICITADO") {
                                    btnRenovar = '<button class="btn btn-sm btn-outline-custom" disabled><i class="fa-solid fa-hourglass-half me-1"></i>Aguardando</button>';
                                } else if (status === "RECUSADO") {
                                    btnRenovar = '<button class="btn btn-sm btn-outline-custom" disabled><i class="fa-solid fa-ban me-1"></i>Recusado</button>';
                                } else if (status === "DEVOLVIDO" || emp.dataDevolucaoEfetiva) {
                                    btnRenovar = '<button class="btn btn-sm btn-outline-custom" disabled><i class="fa-solid fa-check me-1"></i>Concluído</button>';
                                } else if (estaAtrasado || emp.multaAcumulada > 0) {
                                    temPendencia = true;
                                    btnRenovar = '<button class="btn btn-sm btn-outline-custom" disabled title="Procure o administrador da biblioteca"><i class="fa-solid fa-circle-exclamation me-1"></i>Falar com admin</button>';
                                } else {
                                    btnRenovar = '<button class="btn btn-sm btn-accent" onclick="renovarEmprestimo(' + emp.id + ')"><i class="fa-solid fa-rotate me-1"></i>Renovar</button>';
                                }

                                let multaText = '<span class="text-light">Isento</span>';
                                if (status === "SOLICITADO" || status === "RECUSADO") {
                                    multaText = '<span class="text-light">-</span>';
                                } else if (emp.multaAcumulada > 0 && emp.dataDevolucaoEfetiva) {
                                    multaText = '<span class="text-success">Regularizada: R$ ' + emp.multaAcumulada.toFixed(2) + '</span>';
                                } else if (emp.multaAcumulada > 0) {
                                    multaText = '<span class="text-danger font-weight-bold">Pendente: R$ ' + emp.multaAcumulada.toFixed(2) + '</span>';
                                }

                                let tituloLivro = livroMap[emp.livroId] || ('Livro Desconhecido (ID ' + emp.livroId + ')');

                                let row = '<tr>' +
                                    '<td class="font-weight-bold text-white">' + tituloLivro + '</td>' +
                                    '<td>' + emp.dataEmprestimo + '</td>' +
                                    '<td>' + (status === "SOLICITADO" || status === "RECUSADO" ? '-' : emp.dataDevolucaoPrevista) + '</td>' +
                                    '<td>' + statusBadge + '</td>' +
                                    '<td>' + multaText + '</td>' +
                                    '<td>' + emp.renovacoesDecorrentes + '</td>' +
                                    '<td class="text-end">' + btnRenovar + '</td>' +
                                    '</tr>';
                                tbody.append(row);
                            });

                            $("#regularizationNotice").toggleClass("d-none", !temPendencia);
                        }
                    });
                }
            });
        }

        // 4. Renova Empréstimo via AJAX
        function renovarEmprestimo(emprestimoId) {
            $.ajax({
                url: "api/emprestimos",
                type: "POST",
                data: {
                    acao: "renovar",
                    emprestimoId: emprestimoId
                },
                dataType: "json",
                success: function(response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Renovado!',
                        text: response.mensagem,
                        background: '#1e293b',
                        color: '#f8fafc',
                        confirmButtonColor: '#6366f1'
                    });
                    carregarEmprestimos();
                },
                error: function(xhr) {
                    let erro = "Não foi possível renovar o livro.";
                    if (xhr.responseJSON && xhr.responseJSON.erro) {
                        erro = xhr.responseJSON.erro;
                    }
                    Swal.fire({
                        icon: 'error',
                        title: 'Falha na Renovação',
                        text: erro,
                        background: '#1e293b',
                        color: '#f8fafc',
                        confirmButtonColor: '#6366f1'
                    });
                }
            });
        }
    </script>
</body>
</html>
