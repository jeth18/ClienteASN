package com.example.clienteasn.services.pojo;

public class LoginPOJO {
    private String token;
    private String cuenta;
    private String usuario;
    private String username;
    private boolean isModerador;

    public String getToken(){
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isModerador() {
        return isModerador;
    }

    public void setModerador(boolean moderador) {
        isModerador = moderador;
    }
}
