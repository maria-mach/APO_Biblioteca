package com.apo2.biblioteca.model.entity;

/**
 * Classe que representa a entidade ConfiguracaoGlobal no sistema.
 * Contem os parametros unicos do sistema para gerenciar taxas de multas e limites de renovacao.
 * 
 * @version 1.0
 */
public class ConfiguracaoGlobal {

    private int id;
    private double multaDiaria;
    private int limiteRenovacoes;

    /**
     * Construtor padrao da classe ConfiguracaoGlobal.
     */
    public ConfiguracaoGlobal() {
    }

    /**
     * Construtor completo da classe ConfiguracaoGlobal.
     * 
     * @param id               Identificador unico da configuracao.
     * @param multaDiaria      Coeficiente cobrado por dia de atraso.
     * @param limiteRenovacoes Quantidade maxima consecutiva de renovacoes permitidas por emprestimo.
     */
    public ConfiguracaoGlobal(int id, double multaDiaria, int limiteRenovacoes) {
        this.id = id;
        this.multaDiaria = multaDiaria;
        this.limiteRenovacoes = limiteRenovacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMultaDiaria() {
        return multaDiaria;
    }

    public void setMultaDiaria(double multaDiaria) {
        this.multaDiaria = multaDiaria;
    }

    public int getLimiteRenovacoes() {
        return limiteRenovacoes;
    }

    public void setLimiteRenovacoes(int limiteRenovacoes) {
        this.limiteRenovacoes = limiteRenovacoes;
    }

    @Override
    public String toString() {
        return "ConfiguracaoGlobal{" +
                "id=" + id +
                ", multaDiaria=" + multaDiaria +
                ", limiteRenovacoes=" + limiteRenovacoes +
                '}';
    }
}

