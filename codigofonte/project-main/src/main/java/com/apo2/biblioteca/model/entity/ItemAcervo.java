package com.apo2.biblioteca.model.entity;

/**
 * Classe abstrata que serve como base para todos os itens do acervo da biblioteca.
 * Implementa os principios do SOLID (Liskov Substitution Principle - LSP).
 * 
 * @version 1.0
 */
public abstract class ItemAcervo {
    
    protected int id;
    protected String titulo;
    protected int anoPublicacao;

    /**
     * Metodo abstrato para processar a regra de emprestimo especifica de cada tipo de item.
     */
    public abstract void processarRegraEmprestimo();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }
}

