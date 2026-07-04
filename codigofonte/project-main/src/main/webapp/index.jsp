<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Biblioteca MARC21 - Início</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/css/bootstrap.min.css" rel="stylesheet" onerror="this.onerror=null;this.href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css';">
    <!-- FontAwesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    
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
            overflow-x: hidden;
        }

        .navbar {
            background-color: rgba(15, 23, 42, 0.8) !important;
            backdrop-filter: blur(12px);
            border-bottom: 1px solid var(--glass-border);
        }

        .navbar-brand {
            font-weight: 700;
            letter-spacing: 0.5px;
            color: var(--text-main) !important;
        }

        .navbar-brand span {
            color: var(--accent);
        }

        .nav-link {
            color: var(--text-muted) !important;
            font-weight: 500;
            transition: color 0.3s ease;
        }

        .nav-link:hover {
            color: var(--text-main) !important;
        }

        .btn-accent {
            background-color: var(--accent);
            color: white;
            font-weight: 600;
            border-radius: 8px;
            padding: 8px 20px;
            transition: all 0.3s ease;
            border: none;
        }

        .btn-accent:hover {
            background-color: var(--accent-hover);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
            color: white;
        }

        .hero-section {
            padding: 80px 0 60px;
            background: radial-gradient(circle at 80% 20%, rgba(99, 102, 241, 0.15) 0%, transparent 50%);
        }

        .hero-title {
            font-size: 3.5rem;
            font-weight: 800;
            line-height: 1.2;
            margin-bottom: 20px;
        }

        .hero-title span {
            background: linear-gradient(135deg, #818cf8, #a5b4fc);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .hero-subtitle {
            color: var(--text-muted);
            font-size: 1.2rem;
            margin-bottom: 30px;
        }

        .search-container {
            background: var(--glass-bg);
            border: 1px solid var(--glass-border);
            border-radius: 12px;
            padding: 8px;
            backdrop-filter: blur(10px);
            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
        }

        .search-input {
            background: rgba(15, 23, 42, 0.92) !important;
            border: none;
            color: var(--text-main) !important;
            padding: 12px 20px;
            font-size: 1rem;
            caret-color: var(--text-main);
        }

        .search-input:focus {
            outline: none;
            box-shadow: none;
            background: rgba(15, 23, 42, 1) !important;
            color: var(--text-main) !important;
        }

        .search-input::placeholder {
            color: var(--text-muted) !important;
            opacity: 1;
        }

        .search-input:-webkit-autofill,
        .search-input:-webkit-autofill:hover,
        .search-input:-webkit-autofill:focus {
            -webkit-text-fill-color: #f8fafc;
            box-shadow: 0 0 0 1000px #0f172a inset;
            transition: background-color 9999s ease-in-out 0s;
        }

        .card-book {
            background: var(--glass-bg);
            border: 1px solid var(--glass-border);
            border-radius: 16px;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            backdrop-filter: blur(10px);
            overflow: hidden;
            height: 100%;
        }

        .card-book:hover {
            transform: translateY(-5px);
            border-color: rgba(99, 102, 241, 0.3);
            box-shadow: 0 12px 24px rgba(0, 0, 0, 0.4);
        }

        .book-badge {
            background-color: rgba(99, 102, 241, 0.1);
            color: #818cf8;
            font-size: 0.75rem;
            font-weight: 600;
            padding: 4px 10px;
            border-radius: 20px;
            display: inline-block;
            margin-bottom: 12px;
        }

        .book-title {
            font-size: 1.25rem;
            font-weight: 700;
            color: var(--text-main);
            margin-bottom: 8px;
            line-height: 1.4;
        }

        .book-author {
            color: var(--text-muted);
            font-size: 0.9rem;
            margin-bottom: 15px;
        }

        .book-meta {
            border-top: 1px solid rgba(255, 255, 255, 0.05);
            padding-top: 15px;
            font-size: 0.85rem;
            color: var(--text-muted);
        }

        .book-meta span i {
            margin-right: 5px;
            color: var(--accent);
        }

        footer {
            border-top: 1px solid var(--glass-border);
            padding: 40px 0;
            background-color: var(--bg-secondary);
            color: var(--text-muted);
            font-size: 0.9rem;
        }
    </style>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg fixed-top py-3">
        <div class="container">
            <a class="navbar-brand" href="index.jsp"><i class="fa-solid fa-book-bookmark me-2"></i>BIBLIO<span>MARC21</span></a>
            <button class="navbar-expand-toggle navbar-toggler border-0 text-white" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto align-items-center gap-3">
                    <li class="nav-item">
                        <a class="nav-link active" href="index.jsp">Acervo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="login.jsp">Entrar</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-accent" href="cadastro.jsp">Cadastrar-se</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero & Search -->
    <header class="hero-section mt-5">
        <div class="container text-center pt-5">
            <h1 class="hero-title">Explore nosso <span>Acervo Bibliográfico</span></h1>
            <p class="hero-subtitle max-width-md mx-auto">Consulte livros físicos catalogados sob os mais rigorosos padrões da biblioteconomia internacional (MARC21).</p>
            
            <div class="row justify-content-center mt-4 mb-5">
                <div class="col-md-8">
                    <div class="search-container d-flex align-items-center">
                        <i class="fa-solid fa-magnifying-glass text-muted ms-3"></i>
                        <input type="text" id="searchInput" class="form-control search-input" placeholder="Pesquisar por título, autor, ISBN ou campo MARC21 (ex: Tag 650 para Assunto)...">
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- Acervo -->
    <main class="container mb-5 pb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="h4 font-weight-bold"><i class="fa-solid fa-list me-2"></i>Livros Disponíveis</h2>
            <div id="loader" class="spinner-border spinner-border-sm text-accent d-none" role="status"></div>
        </div>

        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4" id="livrosGrid">
            <!-- Os livros carregados via AJAX entrarão aqui -->
        </div>
        
        <div id="noResults" class="text-center py-5 d-none">
            <i class="fa-regular fa-folder-open fa-3x text-muted mb-3"></i>
            <p class="text-muted">Nenhum livro encontrado para os critérios digitados.</p>
        </div>
    </main>

    <!-- Footer -->
    <footer class="text-center">
        <div class="container">
            <p class="mb-2"><i class="fa-solid fa-graduation-cap me-2"></i>Projeto Acadêmico - Algoritmos e Programação Orientada a Objetos II (APO2)</p>
            <p class="mb-0 text-muted-opacity">© 2026 Biblioteca MARC21. Todos os direitos reservados.</p>
        </div>
    </footer>

    <!-- jQuery & Bootstrap 5 JS -->
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        $(document).ready(function() {
            // Carrega acervo inicial ao carregar a página
            carregarAcervo("");

            // Evento de busca (com debounce simplificado de 300ms)
            let timeout = null;
            $("#searchInput").on("input", function() {
                clearTimeout(timeout);
                let query = $(this).val();
                timeout = setTimeout(function() {
                    carregarAcervo(query);
                }, 300);
            });
        });

        function carregarAcervo(query) {
            $("#loader").removeClass("d-none");
            
            // Tratamento especial para busca por tags MARC21: se o usuário digitar "650:historia" ou "020:isbn"
            let url = "api/livros";
            let data = {};
            
            if (query.includes(":")) {
                let partes = query.split(":");
                data.tag = partes[0].trim();
                data.valor = partes[1].trim();
            } else if (query.trim() !== "") {
                // Busca textual simples (por título ou autor, tratada pelo Servlet)
                // Para simplificar a rota padrão do servlet, vamos mandar como busca de tag se o usuário digitar o formato correto,
                // caso contrário faremos o filtro no próprio Javascript a partir da lista geral (ou estendemos a API posteriormente)
                data.buscaGeral = query;
            }

            $.ajax({
                url: url,
                type: "GET",
                data: data,
                dataType: "json",
                success: function(livros) {
                    $("#loader").addClass("d-none");
                    let grid = $("#livrosGrid");
                    grid.empty();

                    // Se enviou busca textual comum, faz o filtro no client-side para não precisar de complexidade no banco
                    if (data.buscaGeral) {
                        let termo = data.buscaGeral.toLowerCase();
                        livros = livros.filter(function(livro) {
                            return livro.titulo.toLowerCase().includes(termo) || 
                                   livro.autor.toLowerCase().includes(termo) || 
                                   livro.isbn.toLowerCase().includes(termo);
                        });
                    }

                    if (livros.length === 0) {
                        $("#noResults").removeClass("d-none");
                        return;
                    }

                    $("#noResults").addClass("d-none");

                    // Renderiza os cards de livro com estética moderna
                    livros.forEach(function(livro) {
                        let badgeEstoque = livro.qtdDisponivel > 0
                            ? '<span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill">Disponível</span>'
                            : '<span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill">Esgotado</span>';

                        let card = '<div class="col">' +
                            '<div class="card-book p-4 d-flex flex-column justify-content-between">' +
                                '<div>' +
                                    '<div class="d-flex justify-content-between align-items-start mb-2">' +
                                        '<span class="book-badge">MARC Tag 245</span>' +
                                        badgeEstoque +
                                    '</div>' +
                                    '<h3 class="book-title">' + livro.titulo + '</h3>' +
                                    '<p class="book-author">Por <b>' + livro.autor + '</b></p>' +
                                '</div>' +
                                '<div class="book-meta">' +
                                    '<div class="d-flex justify-content-between align-items-center mb-1">' +
                                        '<span><i class="fa-solid fa-barcode"></i>ISBN (020)</span>' +
                                        '<span class="text-white">' + livro.isbn + '</span>' +
                                    '</div>' +
                                    '<div class="d-flex justify-content-between align-items-center">' +
                                        '<span><i class="fa-solid fa-cubes"></i>Exemplares</span>' +
                                        '<span class="text-white">' + livro.qtdDisponivel + ' de ' + livro.qtdTotal + '</span>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                        '</div>';
                        grid.append(card);
                    });
                },
                error: function(xhr, status, error) {
                    $("#loader").addClass("d-none");
                    console.error("Erro ao carregar acervo: ", error);
                }
            });
        }
    </script>
</body>
</html>
