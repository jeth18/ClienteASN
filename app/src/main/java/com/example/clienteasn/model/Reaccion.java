package com.example.clienteasn.model;

public class Reaccion {
    private String tipo;
    private String usuarioPropietario;
    private String id;
    private String nombreUsuarioPropietario;

    public Reaccion(){}

    public Reaccion(String tipo, String id) {
        this.tipo = tipo;
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
