package com.example.clienteasn.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Comentario implements Serializable {
    private String comentario;
    private String usuarioPropietario;
    private String id;
    private String nombreUsuarioPropietario;

    public Comentario(){}

    public Comentario(String comentario, String id) {
        this.comentario = comentario;
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioPropietario() {
        return usuarioPropietario;
    }

    public void setUsuarioPropietario(String usuarioPropietario) {
        this.usuarioPropietario = usuarioPropietario;
    }

    public String getNombreUsuarioPropietario() {
        return nombreUsuarioPropietario;
    }

    public void setNombreUsuarioPropietario(String nombreUsuarioPropietario) {
        this.nombreUsuarioPropietario = nombreUsuarioPropietario;
    }
}
