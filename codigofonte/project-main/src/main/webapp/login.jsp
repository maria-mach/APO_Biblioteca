<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Biblioteca MARC21 - Login</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/css/bootstrap.min.css" rel="stylesheet" onerror="this.onerror=null;this.href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css';">
    <!-- FontAwesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- SweetAlert2 (Para Alertas Premium) -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    
    <style>
        :root {
            --bg-primary: #0f172a;
            --bg-secondary: #1e293b;
            --accent: #6366f1;
            --accent-hover: #4f46e5;
            --text-main: #f8fafc;
            --text-muted: #94a3b8;
            --glass-bg: rgba(30, 41, 59, 0.75);
            --glass-border: rgba(255, 255, 255, 0.08);
        }

        body {
            font-family: 'Outfit', sans-serif;
            background-color: var(--bg-primary);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: radial-gradient(circle at 10% 10%, rgba(99, 102, 241, 0.1) 0%, transparent 40%),
                        radial-gradient(circle at 90% 90%, rgba(99, 102, 241, 0.1) 0%, transparent 40%);
        }

        .login-card {
            background: var(--glass-bg);
            border: 1px solid var(--glass-border);
            border-radius: 20px;
            padding: 40px;
            width: 100%;
            max-width: 440px;
            backdrop-filter: blur(16px);
            box-shadow: 0 16px 36px rgba(0, 0, 0, 0.4);
            transition: all 0.3s ease;
        }

        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .logo-icon {
            font-size: 2.5rem;
            color: var(--accent);
            margin-bottom: 15px;
        }

        .login-title {
            font-size: 1.75rem;
            font-weight: 700;
            margin-bottom: 5px;
        }

        .login-subtitle {
            color: var(--text-muted);
            font-size: 0.9rem;
        }

        .form-label {
            font-weight: 500;
            font-size: 0.9rem;
            color: var(--text-main);
            margin-bottom: 8px;
        }

        .input-group-custom {
            background: rgba(15, 23, 42, 0.5);
            border: 1px solid var(--glass-border);
            border-radius: 10px;
            display: flex;
            align-items: center;
            padding: 2px 14px;
            transition: all 0.3s ease;
        }

        .input-group-custom:focus-within {
            border-color: var(--accent);
            box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
        }

        .input-icon {
            color: var(--text-muted);
            font-size: 1rem;
            margin-right: 12px;
        }

        .form-control-custom {
            background: transparent;
            border: none;
            color: var(--text-main);
            padding: 10px 0;
            font-size: 0.95rem;
            width: 100%;
        }

        .form-control-custom:focus {
            outline: none;
            box-shadow: none;
            color: var(--text-main);
        }

        .form-control-custom::placeholder {
            color: var(--text-muted);
        }

        .btn-submit {
            background-color: var(--accent);
            color: white;
            font-weight: 600;
            border-radius: 10px;
            padding: 12px;
            width: 100%;
            border: none;
            transition: all 0.3s ease;
            margin-top: 15px;
        }

        .btn-submit:hover {
            background-color: var(--accent-hover);
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
        }

        .btn-submit:disabled {
            background-color: var(--text-muted);
            transform: none;
            box-shadow: none;
        }

        .link-forgot {
            color: var(--accent);
            text-decoration: none;
            font-size: 0.85rem;
            font-weight: 500;
            transition: color 0.3s ease;
        }

        .link-forgot:hover {
            color: var(--accent-hover);
            text-decoration: underline;
        }

        .register-text {
            text-align: center;
            margin-top: 25px;
            font-size: 0.9rem;
            color: var(--text-muted);
        }

        .register-text a {
            color: var(--accent);
            text-decoration: none;
            font-weight: 600;
        }

        .register-text a:hover {
            text-decoration: underline;
        }
        
        .back-home {
            position: absolute;
            top: 20px;
            left: 20px;
            color: var(--text-muted);
            text-decoration: none;
            font-weight: 500;
            font-size: 0.9rem;
            transition: color 0.3s ease;
        }
        
        .back-home:hover {
            color: var(--text-main);
        }
    </style>
</head>
<body>

    <!-- Botão voltar -->
    <a href="index.jsp" class="back-home"><i class="fa-solid fa-arrow-left me-2"></i>Voltar ao Início</a>

    <div class="login-card">
        <div class="login-header">
            <div class="logo-icon"><i class="fa-solid fa-book-bookmark"></i></div>
            <h2 class="login-title">Acesse sua Conta</h2>
            <p class="login-subtitle">Biblioteca MARC21 - Portal Acadêmico</p>
        </div>

        <form id="loginForm">
            <div class="mb-3">
                <label for="email" class="form-label">E-mail Corporativo</label>
                <div class="input-group-custom">
                    <i class="fa-solid fa-envelope input-icon"></i>
                    <input type="email" id="email" name="email" class="form-control-custom" placeholder="exemplo@email.com" required>
                </div>
            </div>

            <div class="mb-3">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <label for="senha" class="form-label mb-0">Senha de Acesso</label>
                    <a href="recuperar-senha.jsp" class="link-forgot">Esqueceu a senha?</a>
                </div>
                <div class="input-group-custom">
                    <i class="fa-solid fa-lock input-icon"></i>
                    <input type="password" id="senha" name="senha" class="form-control-custom" placeholder="Sua senha secreta" required>
                </div>
            </div>

            <button type="submit" id="btnSubmit" class="btn btn-submit">
                <span id="btnText">Entrar</span>
                <span id="btnSpinner" class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
            </button>
        </form>

        <p class="register-text">Não possui cadastro? <a href="cadastro.jsp">Cadastre-se</a></p>
    </div>

    <!-- jQuery, Bootstrap & SweetAlert2 JS -->
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script>
        $(document).ready(function() {
            // Verifica os parâmetros da URL para alertas
            const urlParams = new URLSearchParams(window.location.search);
            
            if (urlParams.get('ativado') === 'true') {
                Swal.fire({
                    icon: 'success',
                    title: 'Conta Ativada!',
                    text: 'Seu e-mail foi validado e sua conta está pronta. Realize o login abaixo.',
                    background: '#1e293b',
                    color: '#f8fafc',
                    confirmButtonColor: '#6366f1'
                });
            } else if (urlParams.get('erroAtivacao')) {
                let msg = "Token inválido, expirado ou usuário já ativo.";
                if (urlParams.get('erroAtivacao') === 'db_error') msg = "Erro ao gravar ativação no banco.";
                Swal.fire({
                    icon: 'error',
                    title: 'Falha na Ativação',
                    text: msg,
                    background: '#1e293b',
                    color: '#f8fafc',
                    confirmButtonColor: '#6366f1'
                });
            }

            // Submissão assíncrona do formulário de Login (AJAX)
            $("#loginForm").on("submit", function(e) {
                e.preventDefault();
                
                // Animação de Loading
                $("#btnText").addClass("d-none");
                $("#btnSpinner").removeClass("d-none");
                $("#btnSubmit").prop("disabled", true);

                let email = $("#email").val();
                let senha = $("#senha").val();

                $.ajax({
                    url: "api/login",
                    type: "POST",
                    data: {
                        email: email,
                        senha: senha
                    },
                    dataType: "json",
                    success: function(response) {
                        // Sucesso: Redireciona para o painel correspondente (retornado pelo servlet)
                        window.location.href = response.redirect;
                    },
                    error: function(xhr, status, error) {
                        // Reseta o botão de loading
                        $("#btnText").removeClass("d-none");
                        $("#btnSpinner").addClass("d-none");
                        $("#btnSubmit").prop("disabled", false);

                        let erroMsg = "E-mail ou senha incorretos, ou a conta ainda não foi ativada por e-mail.";
                        if (xhr.responseJSON && xhr.responseJSON.erro) {
                            erroMsg = xhr.responseJSON.erro;
                        }

                        Swal.fire({
                            icon: 'error',
                            title: 'Erro de Login',
                            text: erroMsg,
                            background: '#1e293b',
                            color: '#f8fafc',
                            confirmButtonColor: '#6366f1'
                        });
                    }
                });
            });
        });
    </script>
</body>
</html>
