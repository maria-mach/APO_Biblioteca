package com.apo2.biblioteca.model.entity;

/**
 * Classe que representa a entidade Usuario no sistema.
 * Concentra os dados cadastrais, autenticacao e controle de status de acesso.
 * 
 * @version 1.0
 */
public class Usuario {
    
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String tokenAtivacao;
    private boolean ativo;
    private TipoUsuario tipoUsuario;

    /**
     * Construtor padrao da classe Usuario.
     */
    public Usuario() {
    }

    /**
     * Construtor completo da classe Usuario.
     * 
     * @param id            Identificador unico do usuario.
     * @param nome          Nome completo do usuario.
     * @param email         Endereco de e-mail (usado para login).
     * @param senha         Hash da senha criptografada.
     * @param tokenAtivacao Token para ativacao da conta via e-mail.
     * @param ativo         Indica se o usuario esta ativo para acessar o sistema.
     * @param tipoUsuario   Perfil de acesso do usuario (ADMIN ou CLIENTE).
     */
    public Usuario(int id, String nome, String email, String senha, String tokenAtivacao, boolean ativo, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tokenAtivacao = tokenAtivacao;
        this.ativo = ativo;
        this.tipoUsuario = tipoUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTokenAtivacao() {
        return tokenAtivacao;
    }

    public void setTokenAtivacao(String tokenAtivacao) {
        this.tokenAtivacao = tokenAtivacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Metodo de autoverificacao rico (Domain Logic)
     * Verifica se o usuario possui privilegios de administrador.
     * 
     * @return true se for ADMIN, false caso contrario
     */
    public boolean ehAdministrador() {
        return this.tipoUsuario == TipoUsuario.ADMIN;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", ativo=" + ativo +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}

