package com.apo2.biblioteca.model.entity;

import java.time.LocalDate;

/**
 * Classe que representa a entidade Emprestimo no sistema.
 * Gerencia as transacoes de locacao fisica de livros, calculo de multas por atraso
 * e limite de renovacoes efetuadas pelo usuario.
 * 
 * @version 1.0
 */
public class Emprestimo {

    private int id;
    private int usuarioId;
    private int livroId;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoEfetiva;
    private double multaAcumulada;
    private int renovacoesDecorrentes;
    private String status;

    /**
     * Construtor padrao da classe Emprestimo.
     */
    public Emprestimo() {
    }

    /**
     * Construtor completo da classe Emprestimo.
     * 
     * @param id                     Identificador unico da transacao de emprestimo.
     * @param usuarioId              Chave estrangeira do usuario responsavel pela retirada.
     * @param livroId                Chave estrangeira do livro fisico retirado.
     * @param dataEmprestimo         Data da realizacao do emprestimo.
     * @param dataDevolucaoPrevista  Prazo previsto de entrega regulamentar.
     * @param dataDevolucaoEfetiva   Data real de devolucao registrada pelo administrador.
     * @param multaAcumulada         Valor monetario das multas acumuladas por atraso.
     * @param renovacoesDecorrentes  Quantidade de renovacoes consecutivas efetuadas.
     */
    public Emprestimo(int id, int usuarioId, int livroId, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoEfetiva, double multaAcumulada, int renovacoesDecorrentes) {
        this(id, usuarioId, livroId, dataEmprestimo, dataDevolucaoPrevista, dataDevolucaoEfetiva, multaAcumulada, renovacoesDecorrentes, "ATIVO");
    }

    public Emprestimo(int id, int usuarioId, int livroId, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoEfetiva, double multaAcumulada, int renovacoesDecorrentes, String status) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.livroId = livroId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
        this.multaAcumulada = multaAcumulada;
        this.renovacoesDecorrentes = renovacoesDecorrentes;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getLivroId() {
        return livroId;
    }

    public void setLivroId(int livroId) {
        this.livroId = livroId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoEfetiva() {
        return dataDevolucaoEfetiva;
    }

    public void setDataDevolucaoEfetiva(LocalDate dataDevolucaoEfetiva) {
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }

    public double getMultaAcumulada() {
        return multaAcumulada;
    }

    public void setMultaAcumulada(double multaAcumulada) {
        this.multaAcumulada = multaAcumulada;
    }

    public int getRenovacoesDecorrentes() {
        return renovacoesDecorrentes;
    }

    public void setRenovacoesDecorrentes(int renovacoesDecorrentes) {
        this.renovacoesDecorrentes = renovacoesDecorrentes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", livroId=" + livroId +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucaoPrevista=" + dataDevolucaoPrevista +
                ", dataDevolucaoEfetiva=" + dataDevolucaoEfetiva +
                ", multaAcumulada=" + multaAcumulada +
                ", renovacoesDecorrentes=" + renovacoesDecorrentes +
                ", status='" + status + '\'' +
                '}';
    }
}
