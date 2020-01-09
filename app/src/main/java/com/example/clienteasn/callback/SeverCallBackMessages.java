package com.example.clienteasn.callback;

import com.example.clienteasn.model.Mensaje;

import java.util.ArrayList;


public interface SeverCallBackMessages {
    void setListaMessages(ArrayList<Mensaje> listaMessages);
    void afterGetGrupo();

}
