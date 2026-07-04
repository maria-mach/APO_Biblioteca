<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Biblioteca MARC21 - Recuperar Senha</title>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    <style>
        :root {
            --bg-primary: #0f172a;
            --bg-secondary: #1e293b;
            --accent: #6366f1;
            --accent-hover: #4f46e5;
            --text-main: #f8fafc;
            --text-muted: #94a3b8;
            --glass-border: rgba(255, 255, 255, 0.08);
        }

        body {
            font-family: 'Outfit', sans-serif;
            background: var(--bg-primary);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 16px;
        }

        .login-card {
            background: rgba(30, 41, 59, 0.88);
            border: 1px solid var(--glass-border);
            border-radius: 20px;
            padding: 40px;
            width: 100%;
            max-width: 460px;
            box-shadow: 0 16px 36px rgba(0, 0, 0, 0.4);
        }

        .logo-icon {
            color: var(--accent);
            font-size: 2.4rem;
            text-align: center;
            margin-bottom: 16px;
        }

        .login-title {
            text-align: center;
            font-size: 1.75rem;
            font-weight: 700;
        }

        .login-subtitle {
            color: var(--text-muted);
            text-align: center;
            font-size: 0.95rem;
            margin-bottom: 24px;
        }

        .input-group-custom {
            background: rgba(15, 23, 42, 0.5);
            border: 1px solid var(--glass-border);
            border-radius: 10px;
            display: flex;
            align-items: center;
            padding: 2px 14px;
        }

        .input-icon {
            color: var(--text-muted);
            margin-right: 12px;
        }

        .form-control-custom {
            background: transparent;
            border: none;
            color: var(--text-main);
            padding: 12px 0;
            width: 100%;
        }

        .form-control-custom:focus {
            outline: none;
        }

        .btn-submit {
            background-color: var(--accent);
            color: white;
            font-weight: 600;
            border-radius: 10px;
            padding: 12px;
            width: 100%;
            border: none;
            margin-top: 18px;
        }

        .btn-submit:hover {
            background-color: var(--accent-hover);
        }

        .back-home {
            position: absolute;
            top: 20px;
            left: 20px;
            color: var(--text-muted);
            text-decoration: none;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <a href="login.jsp" class="back-home"><i class="fa-solid fa-arrow-left me-2"></i>Voltar ao Login</a>

    <div class="login-card">
        <div class="logo-icon"><i class="fa-solid fa-key"></i></div>
        <h2 class="login-title">Recuperar Senha</h2>
        <p class="login-subtitle">Informe seu e-mail para receber uma senha temporária.</p>

        <form id="recuperarSenhaForm">
            <label for="email" class="form-label">E-mail cadastrado</label>
            <div class="input-group-custom">
                <i class="fa-solid fa-envelope input-icon"></i>
                <input type="email" id="email" name="email" class="form-control-custom" placeholder="exemplo@email.com" required>
            </div>

            <button type="submit" id="btnSubmit" class="btn btn-submit">
                <span id="btnText">Enviar Instruções</span>
                <span id="btnSpinner" class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
            </button>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        $("#recuperarSenhaForm").on("submit", function(e) {
            e.preventDefault();
            $("#btnText").addClass("d-none");
            $("#btnSpinner").removeClass("d-none");
            $("#btnSubmit").prop("disabled", true);

            $.ajax({
                url: "api/recuperar-senha",
                type: "POST",
                data: $(this).serialize(),
                dataType: "json",
                success: function(response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Solicitação Recebida',
                        text: response.mensagem,
                        background: '#1e293b',
                        color: '#f8fafc',
                        confirmButtonColor: '#6366f1'
                    }).then(() => window.location.href = "login.jsp");
                },
                error: function(xhr) {
                    $("#btnText").removeClass("d-none");
                    $("#btnSpinner").addClass("d-none");
                    $("#btnSubmit").prop("disabled", false);
                    Swal.fire({
                        icon: 'error',
                        title: 'Falha na Recuperação',
                        text: xhr.responseJSON ? xhr.responseJSON.erro : "Não foi possível processar a solicitação.",
                        background: '#1e293b',
                        color: '#f8fafc',
                        confirmButtonColor: '#6366f1'
                    });
                }
            });
        });
    </script>
</body>
</html>
