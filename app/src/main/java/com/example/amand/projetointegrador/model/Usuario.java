package com.example.amand.projetointegrador.model;

/**
 * Created by amanda on 30/03/17.
 */

public class Usuario {

    private long id;
    private String nome; //fb:public_profile - name |
    private String email; //fb:email |
    private String whatsapp;
    private String telefone;
    private String senha;
    private String fotoPerfil; //fb:public_profile - picture |
    private String faceUser; //fb:public_profile - link

    public String getFaceUser() {
        return faceUser;
    }

    public void setFaceUser(String faceUser) {
        this.faceUser = faceUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Usuario(long id, String nome, String email, String whatsapp, String telefone, String senha, String fotoPerfil, String faceUser) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.whatsapp = whatsapp;
        this.telefone = telefone;
        this.senha = senha;
        this.fotoPerfil = fotoPerfil;
        this.faceUser = faceUser;
    }

    public Usuario() {
    }
}
