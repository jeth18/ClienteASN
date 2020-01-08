package com.example.clienteasn.model;

public class Mensaje {
    private String id;
    private String idUsuario;
    private String idChatG;
    private String textMensaje;
    private String nombreUsuario;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdChatG() {
        return idChatG;
    }

    public void setIdChatG(String idChatG) {
        this.idChatG = idChatG;
    }

    public String getTextMensaje() {
        return textMensaje;
    }

    public void setTextMensaje(String textMensaje) {
        this.textMensaje = textMensaje;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
