package com.example.amand.projetointegrador.model;

/**
 * Created by amanda on 18/05/17.
 */

public class AnuncioEncontrado extends Anuncio {

    private String titulo;

    private String latitude;

    private String longitude;

    private boolean resgatado;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setResgatado(boolean resgatado) {
        this.resgatado = resgatado;
    }
    public boolean isResgatado() {
        return resgatado;
    }
}
