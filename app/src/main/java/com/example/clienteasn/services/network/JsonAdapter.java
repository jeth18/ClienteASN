package com.example.clienteasn.services.network;

import android.util.Log;

import com.example.clienteasn.model.AmigoBusqueda;
import com.example.clienteasn.model.Comentario;
import com.example.clienteasn.model.Cuenta;
import com.example.clienteasn.model.Mensaje;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.model.ChatGroup;
import com.example.clienteasn.services.pojo.LoginPOJO;
import com.example.clienteasn.services.pojo.RegisterPOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class JsonAdapter {
    public static LoginPOJO loginAdapter(JSONObject jsonObject) throws JSONException {
        LoginPOJO res = new LoginPOJO();
        boolean isModerador = jsonObject.getBoolean("isModerador");
        if(isModerador){
            res.setToken(jsonObject.getString("token"));
            res.setCuenta(jsonObject.getString("idCuenta"));
            res.setModerador(jsonObject.getBoolean("isModerador"));
        } else {
            res.setToken(jsonObject.getString("token"));
            res.setCuenta(jsonObject.getString("idCuenta"));
            res.setUsuario(jsonObject.getString("idUsuario"));
            res.setUsername(jsonObject.getString("usuario"));
            res.setModerador(jsonObject.getBoolean("isModerador"));
        }
        return res;
    }

    public static RegisterPOJO registerAdapter(JSONObject jsonObject) throws  JSONException {
        RegisterPOJO res = new RegisterPOJO();
        res.setUsuario(jsonObject.getString("usuario"));
        return res;
    }

    public static ChatGroup chatGroupAdapter(JSONObject jsonObject) throws  JSONException {
        ChatGroup res = new ChatGroup();
        JSONArray members = jsonObject.getJSONArray("members");
        JSONObject member1 = members.getJSONObject(0);
        JSONObject member2 = members.getJSONObject(1);
        JSONObject usuario1 = member1.getJSONObject("member");
        JSONObject usuario2 = member2.getJSONObject("member");

        res.setIdChatG(jsonObject.getString("_id"));
        res.setIdUsuario1(usuario1.getString("_id"));
        res.setNombreUsuario1(usuario1.getString("nombrePublico"));
        res.setIdUsuario2(usuario2.getString("_id"));
        res.setNombreUsuario2(usuario2.getString("nombrePublico"));

        return res;
    }

    public static ArrayList<ChatGroup> chatGroupsAdapter(JSONArray jsonArray) throws JSONException{
        ArrayList<ChatGroup> res = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            ChatGroup newChat = new ChatGroup();
            JSONObject chat = jsonArray.getJSONObject(i);
            JSONArray members = chat.getJSONArray("members");
            JSONObject member1 = members.getJSONObject(0);
            JSONObject member2 = members.getJSONObject(1);
            JSONObject usuario1 = member1.getJSONObject("member");
            JSONObject usuario2 = member2.getJSONObject("member");

            newChat.setIdChatG(chat.getString("_id"));
            newChat.setIdUsuario1(usuario1.getString("_id"));
            newChat.setNombreUsuario1(usuario1.getString("nombrePublico"));
            newChat.setIdUsuario2(usuario2.getString("_id"));
            newChat.setNombreUsuario2(usuario2.getString("nombrePublico"));

            res.add(newChat);
        }
        return res;
    }

    public static AmigoBusqueda cuentaAdapter(JSONObject jsonObject) throws JSONException {
        AmigoBusqueda res = new AmigoBusqueda();
        res.setId(jsonObject.getString("_id"));
        res.setNombre(jsonObject.getString("nombre"));
        res.setApellidos(jsonObject.getString("apellido"));
        res.setUsuario(jsonObject.getString("usuario"));

        JSONObject usuarioAsociado = jsonObject.getJSONObject("usuarioAsociado");
        res.setIdUsuario(usuarioAsociado.getString("_id"));
        res.setFoto_perfil(usuarioAsociado.getString("foto_perfil"));
        res.setDescripcion(usuarioAsociado.getString("descripcion"));

        return res;
    }

    public static Cuenta miCuentaAdapter(JSONObject jsonObject) throws JSONException {
        Cuenta res = new Cuenta();
        res.setId(jsonObject.getString("_id"));
        res.setCorreo(jsonObject.getString("correo"));
        res.setNombre(jsonObject.getString("nombre"));
        res.setApellido(jsonObject.getString("apellido"));
        res.setUsuario(jsonObject.getString("usuario"));

        JSONObject usuarioAsociado = jsonObject.getJSONObject("usuarioAsociado");
        res.setIdUsuario(usuarioAsociado.getString("_id"));
        res.setFoto_perfil(usuarioAsociado.getString("foto_perfil"));
        res.setDescripcion(usuarioAsociado.getString("descripcion"));

        return res;
    }

    public static Comentario comentarioAdapter(JSONObject jsonObject) throws JSONException {

        Log.d("Prueba", jsonObject.toString());
        Comentario res = new Comentario();
        res.setId(jsonObject.getString("_id"));
        res.setComentario(jsonObject.getString("comentario"));

        Log.d("usuarioPropietario", "Hola");

        JSONObject usuarioPropietario = jsonObject.getJSONObject("usuario");


        res.setUsuarioPropietario(usuarioPropietario.getString("_id"));
        res.setNombreUsuarioPropietario(usuarioPropietario.getString("nombrePublico"));
        return res;
    }

    public static Reaccion reaccionAdapter(JSONObject jsonObject) throws JSONException {
        Reaccion res = new Reaccion();
        res.setId(jsonObject.getString("_id"));
        res.setTipo(jsonObject.getString("tipo"));
        JSONObject usuarioPropietario = jsonObject.getJSONObject("usuario");
        res.setUsuarioPropietario(usuarioPropietario.getString("_id"));
        res.setNombreUsuarioPropietario(usuarioPropietario.getString("nombrePublico"));
        return res;

    }


    public static ArrayList<Publicacion> publicacionAdapter(JSONArray jsonArray) throws JSONException {
        ArrayList<Publicacion> res = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Publicacion p = new Publicacion();
            JSONArray comentariosJSON = jsonObject.getJSONArray("comentarios");
            JSONArray reaccionesJSON = jsonObject.getJSONArray("reacciones");
            p.setId(jsonObject.getString("_id"));
            p.setFotoURL(jsonObject.getString("fotoUrl"));
            p.setFechaCarga(jsonObject.getString("fechaCarga"));
            p.setDescripcion(jsonObject.getString("descripcion"));

            JSONObject usuarioPublicacion = jsonObject.getJSONObject("usuario");
            p.setUsuarioPropietario(usuarioPublicacion.getString("_id"));
            p.setNombreUsuarioPropietario(usuarioPublicacion.getString("nombrePublico"));

            ArrayList<Comentario> comentarios = new ArrayList<>();
            ArrayList<Reaccion> reacciones  = new ArrayList<>();
            for(int x = 0; x < comentariosJSON.length(); x++) {
                JSONObject jsonComentario = comentariosJSON.getJSONObject(x);
                Comentario comentarioTemp = new Comentario();
                comentarioTemp.setComentario(jsonComentario.getString("comentario"));

                comentarioTemp.setId(jsonComentario.getString("_id"));
                JSONObject jsonUsuarioComentario = jsonComentario.getJSONObject("usuario");
                comentarioTemp.setUsuarioPropietario(jsonUsuarioComentario.getString("_id"));
                comentarioTemp.setNombreUsuarioPropietario(jsonUsuarioComentario.getString("nombrePublico"));
                comentarios.add(comentarioTemp);
            }

            for(int j = 0; j < reaccionesJSON.length(); j++) {
                JSONObject jsonReaccion = reaccionesJSON.getJSONObject(j);
                Reaccion reaccionTemp = new Reaccion();
                reaccionTemp.setTipo(jsonReaccion.getString("tipo"));

                reaccionTemp.setId(jsonReaccion.getString("_id"));
                JSONObject jsonUsuarioReaccion = jsonReaccion.getJSONObject("usuario");
                reaccionTemp.setUsuarioPropietario(jsonUsuarioReaccion.getString("_id"));
                reaccionTemp.setNombreUsuarioPropietario(jsonUsuarioReaccion.getString("nombrePublico"));
                reacciones.add(reaccionTemp);
            }

            p.setComentarios(comentarios);
            p.setReacciones(reacciones);

            res.add(p);
        }
        return res;
    }

    public static ArrayList<Mensaje> mensajesAdapter(JSONArray jsonArray) throws JSONException {
        ArrayList<Mensaje> res = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Mensaje m = new Mensaje();
            m.setId(jsonObject.getString("_id"));
            m.setIdChatG(jsonObject.getString("chatgroup"));
            JSONObject member = jsonObject.getJSONObject("member");
            m.setIdUsuario(member.getString("_id"));
            m.setNombreUsuario(member.getString("nombrePublico"));
            m.setTextMensaje(jsonObject.getString("message"));

            res.add(m);
        }

        return res;
    }

    public static Mensaje mensajeAdapter(JSONObject jsonObject) throws JSONException {
        Mensaje m = new Mensaje();
        m.setId(jsonObject.getString("_id"));
        m.setIdChatG(jsonObject.getString("chatgroup"));
        m.setIdUsuario(jsonObject.getString("member"));
        m.setNombreUsuario("Yo");
        m.setTextMensaje(jsonObject.getString("message"));
        return m;
    }
}


