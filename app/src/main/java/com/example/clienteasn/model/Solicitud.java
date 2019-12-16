package com.example.clienteasn.model;

public class Solicitud {
    private String id;
    private String usuarioEnvia;
    private String usuarioRecibe;
    private String estadoSolicitud;

    public Solicitud(String usuarioEnvia, String usuarioRecibe, String estadoSolicitud) {
        this.usuarioEnvia = usuarioEnvia;
        this.usuarioRecibe = usuarioRecibe;
        this.estadoSolicitud = estadoSolicitud;
    }

    public String getUsuarioEnvia() {
        return usuarioEnvia;
    }

    public void setUsuarioEnvia(String usuarioEnvia) {
        this.usuarioEnvia = usuarioEnvia;
    }

    public String getUsuarioRecibe() {
        return usuarioRecibe;
    }

    public void setUsuarioRecibe(String usuarioRecibe) {
        this.usuarioRecibe = usuarioRecibe;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
