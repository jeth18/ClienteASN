package com.example.clienteasn.model;

import java.util.ArrayList;

public class Publicacion {
    private ArrayList<Comentario> comentarios;
    private ArrayList<Reaccion> reacciones;
    private String fotoURL;
    private String fechaCarga;
    private String descripcion;
    private String id;
    private String usuarioPropietario;
    private String nombreUsuarioPropietario;
    private boolean iLike;

    public Publicacion(){}

    public Publicacion(ArrayList<Comentario> comentarios, ArrayList<Reaccion> reacciones, String fotoURL, String fechaCarga, String descripcion) {
        this.comentarios = comentarios;
        this.reacciones = reacciones;
        this.fotoURL = fotoURL;
        this.fechaCarga = fechaCarga;
        this.descripcion = descripcion;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public ArrayList<Reaccion> getReacciones() {
        return reacciones;
    }

    public void setReacciones(ArrayList<Reaccion> reacciones) {
        this.reacciones = reacciones;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public boolean isiLike() {
        return iLike;
    }

    public void setiLike(boolean iLike) {
        this.iLike = iLike;
    }
}
