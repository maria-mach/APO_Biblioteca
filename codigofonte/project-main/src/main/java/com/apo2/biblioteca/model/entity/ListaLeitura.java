package com.apo2.biblioteca.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa a entidade ListaLeitura no sistema.
 * Pasta ou colecao personalizada criada por um leitor para organizar e favoritar obras do acervo.
 * 
 * @version 1.0
 */
public class ListaLeitura {

    private int id;
    private int usuarioId;
    private String nome;
    private LocalDateTime dataCriacao;
    private List<LivroFisico> livros = new ArrayList<>();

    /**
     * Construtor padrao da classe ListaLeitura.
     */
    public ListaLeitura() {
    }

    /**
     * Construtor basico (sem a lista de livros carregada).
     * 
     * @param id          Identificador unico da lista de leitura.
     * @param usuarioId   Identificador do usuario dono da lista.
     * @param nome        Nome da pasta/lista.
     * @param dataCriacao Data e hora em que a pasta foi criada.
     */
    public ListaLeitura(int id, int usuarioId, String nome, LocalDateTime dataCriacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
    }

    /**
     * Construtor completo com lista de livros.
     * 
     * @param id          Identificador unico da lista de leitura.
     * @param usuarioId   Identificador do usuario dono da lista.
     * @param nome        Nome da pasta/lista.
     * @param dataCriacao Data e hora em que a pasta foi criada.
     * @param livros      Lista contendo os livros associados a essa colecao.
     */
    public ListaLeitura(int id, int usuarioId, String nome, LocalDateTime dataCriacao, List<LivroFisico> livros) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        this.livros = livros;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<LivroFisico> getLivros() {
        return livros;
    }

    public void setLivros(List<LivroFisico> livros) {
        this.livros = livros;
    }

    @Override
    public String toString() {
        return "ListaLeitura{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", nome='" + nome + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", totalLivros=" + (livros != null ? livros.size() : 0) +
                '}';
    }
}

