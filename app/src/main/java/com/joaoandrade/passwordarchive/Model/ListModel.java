package com.joaoandrade.passwordarchive.Model;

public class ListModel {

    public ListModel(){

    }

    private String id;

    private String conta;
    private String usuario;
    private String senha;
    private String logo;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
