package com.example.clienteasn.services.network;

public class ApiEndpoint {
    private static String host = "10.0.2.2:8080";
    //private static String host = "192.168.1.78:8080";
    public static String baseURL = "http://" + host + "/api";
    public static String ping = baseURL + "/ping";

    //Cuenta
    public static String login = baseURL + "/Cuenta/login";
    public static String register = baseURL + "/Cuenta";
    public static String obtenerCuenta = baseURL + "/Cuenta/buscarCuenta";
    public static String miCuenta = baseURL + "/Cuenta/MiCuenta";

    //Solictudes
    public static String amigosUsuario = baseURL + "/Solicitud/amigos/";
    public static String agregarAmigo = baseURL + "/Solicitud/";

    //Publicaciones
    public static String publicacion = baseURL + "/Publicacion";
    public static String feedfotos = baseURL + "/Publicacion/feedfotos";
    public static String upload = baseURL + "/Publicacion/upload";
    public static String feedmoderador = baseURL + "/Publicacion/feedmoderador";

    //Comentarios
    public static String nuevoComentario = baseURL + "/Publicacion/nuevocomentario";

    //Reacciones
    public static String nuevaReaccion = baseURL + "/Publicacion/nuevareaccion";
    public static String eliminarReaccion = baseURL + "/Publicacion/delReaccion";

    //chat
    public static String chatGroup = baseURL + "/ChatGroup/save";
    public static String getMessages = baseURL + "/ChatGroup/getMessages/";
    public static String getChatGroup = baseURL + "/ChatGroup/getGroup/";
    public static String sendMessage = baseURL + "/ChatGroup/saveMessage";
    public static String getChatGroupsUser = baseURL + "/ChatGroup/";



}
