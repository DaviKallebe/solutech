package com.example.bruno.myapplication.commons;

import java.util.Date;

public class PerfilOpcoes {
    private String nome;
    private String valor;
    private Date dateValor;
    private Integer tipo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public Date getDateValor() {
        return dateValor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setDateValor(Date dateValor) {
        this.dateValor = dateValor;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public PerfilOpcoes(String nome, String valor, Integer tipo) {
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
    }

    public PerfilOpcoes(String nome, Date valor, Integer tipo) {
        this.nome = nome;
        this.dateValor = valor;
        this.tipo = tipo;
    }
}
