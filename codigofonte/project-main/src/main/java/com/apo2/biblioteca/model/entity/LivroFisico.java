package com.apo2.biblioteca.model.entity;

/**
 * Representa um Livro Fisico no acervo da biblioteca.
 * Estende a classe abstrata ItemAcervo.
 * Mapeia os dados da tabela 'livros' do banco de dados MySQL.
 * 
 * @version 1.0
 */
public class LivroFisico extends ItemAcervo {
    
    private String isbn;
    private String autor;
    private int qtdTotal;
    private int qtdDisponivel;

    /**
     * Construtor padrao.
     */
    public LivroFisico() {
    }

    /**
     * Construtor completo.
     */
    public LivroFisico(int id, String isbn, String titulo, String autor, int qtdTotal, int qtdDisponivel) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.qtdTotal = qtdTotal;
        this.qtdDisponivel = qtdDisponivel;
    }

    @Override
    public void processarRegraEmprestimo() {
        // Lógica descrita no PDF: Reduz estoque fisico e aplica prazo de 7 dias
        if (qtdDisponivel > 0) {
            qtdDisponivel--;
            System.out.println("Processando regra de emprestimo para Livro Fisico: Reduzindo estoque e aplicando prazo de 7 dias.");
        } else {
            throw new IllegalStateException("Nao ha exemplares disponiveis para emprestimo.");
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getQtdTotal() {
        return qtdTotal;
    }

    public void setQtdTotal(int qtdTotal) {
        this.qtdTotal = qtdTotal;
    }

    public int getQtdDisponivel() {
        return qtdDisponivel;
    }

    public void setQtdDisponivel(int qtdDisponivel) {
        this.qtdDisponivel = qtdDisponivel;
    }

    @Override
    public String toString() {
        return "LivroFisico{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", qtdTotal=" + qtdTotal +
                ", qtdDisponivel=" + qtdDisponivel +
                '}';
    }
}

