package com.example.clienteasn.services.pojo;

public class LoginPOJO {
    private String token;
    private String cuenta;
    private String usuario;

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
}
