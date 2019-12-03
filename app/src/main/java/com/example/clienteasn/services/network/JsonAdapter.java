package com.example.clienteasn.services.network;

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
        res.setToken(jsonObject.getString("token"));
        res.setCuenta(jsonObject.getString("idCuenta"));
        res.setUsuario(jsonObject.getString("idUsuario"));
        return res;
    }

    public static RegisterPOJO registerAdapter(JSONObject jsonObject) throws  JSONException {
        RegisterPOJO res = new RegisterPOJO();
        res.setUsuario(jsonObject.getString("usuario"));
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
            p.setUsuarioPropietario(jsonObject.getString("usuario"));

            ArrayList<Comentario> comentarios = new ArrayList<>();
            ArrayList<Reaccion> reacciones  = new ArrayList<>();
            for(int x = 0; x < comentariosJSON.length(); x++) {
                JSONObject jsonComentario = comentariosJSON.getJSONObject(x);
                Comentario comentarioTemp = new Comentario();
                comentarioTemp.setComentario(jsonComentario.getString("comentario"));
                comentarioTemp.setUsuarioPropietario(jsonComentario.getString("usuario"));
                comentarioTemp.setId(jsonComentario.getString("_id"));

                comentarios.add(comentarioTemp);
            }

            for(int j = 0; j < reaccionesJSON.length(); j++) {
                JSONObject jsonReaccion = reaccionesJSON.getJSONObject(j);
                Reaccion reaccionTemp = new Reaccion();
                reaccionTemp.setTipo(jsonReaccion.getString("tipo"));
                reaccionTemp.setUsuarioPropietario(jsonReaccion.getString("usuario"));
                reaccionTemp.setId(jsonReaccion.getString("_id"));

                reacciones.add(reaccionTemp);
            }

            p.setComentarios(comentarios);
            p.setReacciones(reacciones);

            res.add(p);
        }
        return res;
    }

}
