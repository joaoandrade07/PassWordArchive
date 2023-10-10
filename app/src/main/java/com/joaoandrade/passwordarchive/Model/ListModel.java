package com.joaoandrade.passwordarchive.Model;

public class ListModel {

    public ListModel(){

    }

    private Long id;

    private String conta, usuario, senha;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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
}
