package com.joaoandrade.passwordarchive.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "Users")
public class UserModel {

    public UserModel(){
        
    }
    @Id
    private String id;

    @NotNull
    private String email, senha, nome;

    @NotNull
    private boolean biometria;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean getBiometria() {
        return biometria;
    }

    public void setBiometria(boolean biometria) {
        this.biometria = biometria;
    }

}
