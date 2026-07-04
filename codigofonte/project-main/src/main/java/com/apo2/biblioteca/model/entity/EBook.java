package com.apo2.biblioteca.model.entity;

/**
 * Representa um EBook (Livro Digital) no acervo da biblioteca.
 * Estende a classe abstrata ItemAcervo.
 * 
 * @version 1.0
 */
public class EBook extends ItemAcervo {
    
    private String urlDownload;
    private double tamanhoArquivoMB;

    @Override
    public void processarRegraEmprestimo() {
        // Gera link criptografado temporario sem impactar estoque fisico
        System.out.println("Processando regra de emprestimo para EBook: Disponibilizando link temporario.");
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public double getTamanhoArquivoMB() {
        return tamanhoArquivoMB;
    }

    public void setTamanhoArquivoMB(double tamanhoArquivoMB) {
        this.tamanhoArquivoMB = tamanhoArquivoMB;
    }
}

