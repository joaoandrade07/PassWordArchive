package com.joaoandrade.passwordarchive.Model;

public class No {

    // atributos
    private String conta;
    private String usuario;
    private String senha;
    private No esquerda;
    private No direita;

    // construtor
    public No(String conta, String usuario, String senha) {
        this.conta = conta;
        this.usuario = usuario;
        this.senha = senha;
        this.esquerda = null;
        this.direita = null;
    }

    // gets e sets
    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public No getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(No esquerda) {
        this.esquerda = esquerda;
    }

    public No getDireita() {
        return direita;
    }

    public void setDireita(No direita) {
        this.direita = direita;
    }
}
