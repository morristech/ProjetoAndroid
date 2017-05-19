package com.example.amand.projetointegrador.model;

/**
 * Created by amanda on 06/04/17.
 */

public class PerfilUsuario {

    private long id;
    private String fotoPerfil; //fb:public_profile - picture |
    private String faceUser; //fb:public_profile - link
    private String whatsapp;
    private String telefone;
    private String celular;

    public PerfilUsuario(long id, String fotoPerfil, String faceUser, String whatsapp, String telefone) {
        this.id = id;
        this.fotoPerfil = fotoPerfil;
        this.faceUser = faceUser;
        this.whatsapp = whatsapp;
        this.telefone = telefone;
    }

    public PerfilUsuario() {
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
