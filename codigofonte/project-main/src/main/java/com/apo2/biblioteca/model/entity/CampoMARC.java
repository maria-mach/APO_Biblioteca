package com.apo2.biblioteca.model.entity;

/**
 * Classe que representa a entidade CampoMARC no sistema.
 * Utilizada na modelagem hibrida chave-valor para guardar metadados do padrao MARC21.
 * 
 * @version 1.0
 */
public class CampoMARC {

    private int id;
    private int livroId;
    private String tag;
    private String valor;

    /**
     * Construtor padrao da classe CampoMARC.
     */
    public CampoMARC() {
    }

    /**
     * Construtor completo da classe CampoMARC.
     * 
     * @param id      Identificador unico da tag MARC associada.
     * @param livroId Chave estrangeira que referencia a obra (Livro).
     * @param tag     Codigo identificador numerico da tag de 3 digitos (Ex: 500, 650).
     * @param valor   Conteudo textual indexado associado a tag.
     */
    public CampoMARC(int id, int livroId, String tag, String valor) {
        this.id = id;
        this.livroId = livroId;
        this.tag = tag;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivroId() {
        return livroId;
    }

    public void setLivroId(int livroId) {
        this.livroId = livroId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "CampoMARC{" +
                "id=" + id +
                ", livroId=" + livroId +
                ", tag='" + tag + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}

