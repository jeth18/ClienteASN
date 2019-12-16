package com.example.clienteasn.viewmodel;

import com.example.clienteasn.model.Reaccion;

public interface ClickListener {
    void onPositionClicked(int position);
    void onEliminarClicked(int position);
    void onReaccionarClicked(int position, Reaccion reaccion);
    void onDeleteReaccionClicked(int position, int postionReaccion);
    void onComentarioEliminado(int position);
}
