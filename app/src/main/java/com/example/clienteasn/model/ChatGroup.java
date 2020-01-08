package com.example.clienteasn.model;

public class ChatGroup {
    private String idChatG;
    private String idUsuario1;
    private String idUsuario2;
    private String nombreUsuario1;
    private String nombreUsuario2;

    public String getIdChatG() {
        return idChatG;
    }

    public void setIdChatG(String idChatG) {
        this.idChatG = idChatG;
    }
    public String getIdUsuario1() {
        return idUsuario1;
    }

    public void setIdUsuario1(String idUsuario1) {
        this.idUsuario1 = idUsuario1;
    }

    public String getIdUsuario2() {
        return idUsuario2;
    }

    public void setIdUsuario2(String idUsuario2) {
        this.idUsuario2 = idUsuario2;
    }

    public String getNombreUsuario1() {
        return nombreUsuario1;
    }

    public void setNombreUsuario1(String nombreUsuario1) {
        this.nombreUsuario1 = nombreUsuario1;
    }

    public String getNombreUsuario2() {
        return nombreUsuario2;
    }

    public void setNombreUsuario2(String nombreUsuario2) {
        this.nombreUsuario2 = nombreUsuario2;
    }
}
