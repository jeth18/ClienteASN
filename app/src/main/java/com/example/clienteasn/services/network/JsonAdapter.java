package com.example.clienteasn.services.network;

import android.util.Log;

import com.example.clienteasn.model.Comentario;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.services.pojo.LoginPOJO;
import com.example.clienteasn.services.pojo.RegisterPOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            res.setModerador(jsonObject.getBoolean("isModerador"));
        }
        return res;
    }

    public static RegisterPOJO registerAdapter(JSONObject jsonObject) throws  JSONException {
        RegisterPOJO res = new RegisterPOJO();
        res.setUsuario(jsonObject.getString("usuario"));
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

}
