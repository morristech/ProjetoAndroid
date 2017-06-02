package com.example.amand.projetointegrador.model;

import java.util.Date;
import java.util.List;

/**
 * Created by amanda on 30/03/17.
 */

public abstract class Anuncio {

    private long id;

    private List<String> imgAnuncio;

    private String descricao;

    private String tipo;

    private String cor;

    private String porte;

    private String sexo;

    private Usuario usuario;

    private Date dataPublicacao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getImgAnucio() {
        return imgAnuncio;
    }

    public void setImgAnucio(List<String> imgAnucio) {
        this.imgAnuncio = imgAnucio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(Date dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
}
