package com.example.amand.projetointegrador.model;

/**
 * Created by amanda on 06/04/17.
 */

public class ContatoUsuario {

    private long id;
    private Usuario usuario;
    private String fotoPerfil; //fb:public_profile - picture |
    private String faceUser; //fb:public_profile - link
    private String whatsapp;
    private String telefone;

    public ContatoUsuario(long id, Usuario usuario, String fotoPerfil, String faceUser, String whatsapp, String telefone) {
        this.id = id;
        this.usuario = usuario;
        this.fotoPerfil = fotoPerfil;
        this.faceUser = faceUser;
        this.whatsapp = whatsapp;
        this.telefone = telefone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFaceUser() {
        return faceUser;
    }

    public void setFaceUser(String faceUser) {
        this.faceUser = faceUser;
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
}
