package com.example.clienteasn.services.network;

public class ApiEndpoint {
    private static String host = "10.0.2.2:8080";
    private static String baseURL = "http://" + host + "/api";
    public static String ping = baseURL + "/ping";
    public static String login = baseURL + "/Cuenta/login";
    public static String register = baseURL + "/Cuenta";


    //Solictudes
    public static String amigosUsuario = baseURL + "/Solicitud/amigos/";

    //Publicaciones
    public static String feedfotos = baseURL + "/Publicacion/feedfotos";
}