package com.example.clienteasn.model;

public class Comentario {
    private String comentario;
    private String usuarioPropietario;
    private String id;

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
}
