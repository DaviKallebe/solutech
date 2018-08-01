package com.example.bruno.myapplication.retrofit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"createdAt", "updatedAt"})
public class Comentario {
    private Integer destinatario;
    private Integer remetente;
    private String imagem;
    private String primeiroNome;
    private String ultimoNome;
    private String comentario;
    private Double ponto;

    public Integer getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Integer destinatario) {
        this.destinatario = destinatario;
    }

    public Integer getRemetente() {
        return remetente;
    }

    public void setRemetente(Integer remetente) {
        this.remetente = remetente;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public String getFullName() {
        return this.primeiroNome + ' ' + this.ultimoNome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Double getPonto() {
        return ponto;
    }

    public void setPonto(Double ponto) {
        this.ponto = ponto;
    }
}
