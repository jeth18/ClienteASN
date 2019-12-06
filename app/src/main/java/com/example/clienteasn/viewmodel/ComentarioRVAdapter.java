package com.example.clienteasn.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clienteasn.R;
import com.example.clienteasn.model.Comentario;
import com.example.clienteasn.model.Publicacion;

import java.lang.ref.WeakReference;
import java.util.List;

public class ComentarioRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ClickListener listener;
    private Context context;


    public List<Comentario> mItemList;

    public ComentarioRVAdapter(List<Comentario> itemList, Context context, ClickListener listener) {
        mItemList = itemList;
        this.context = context;
        this.listener = listener;

        Log.d("Lista comentarios", mItemList.toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_comentario, parent, false);
        return new ItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        populateItemRows((ComentarioRVAdapter.ItemViewHolder) viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeakReference<ClickListener> listenerRef;
        private TextView usuarioComentario;
        private TextView comentario;

        public ItemViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            usuarioComentario = itemView.findViewById(R.id.txtUsuarioComentario);
            comentario = itemView.findViewById(R.id.txtComentario);
        }

        @Override
        public void onClick(View v) {

        }
    }


    private void populateItemRows(final ComentarioRVAdapter.ItemViewHolder viewHolder, int position) {

        Comentario item = mItemList.get(position);
        viewHolder.usuarioComentario.setText(item.getNombreUsuarioPropietario());
        viewHolder.comentario.setText(item.getComentario());

    }
}
