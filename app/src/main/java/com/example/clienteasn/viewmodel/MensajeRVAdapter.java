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
import com.example.clienteasn.model.Mensaje;

import java.lang.ref.WeakReference;
import java.util.List;

public class MensajeRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ClickListener listener;
    private Context context;


    public List<Mensaje> mItemList;

    public MensajeRVAdapter(List<Mensaje> itemList, Context context, ClickListener listener) {
        mItemList = itemList;
        this.context = context;
        this.listener = listener;

        Log.d("Lista Mensajes", mItemList.toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_mensaje, parent, false);
        return new ItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        populateItemRows((MensajeRVAdapter.ItemViewHolder) viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeakReference<ClickListener> listenerRef;
        private TextView usuarioMensaje;
        private TextView mensaje;

        public ItemViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            usuarioMensaje = itemView.findViewById(R.id.txtUsuarioMensaje);
            mensaje = itemView.findViewById(R.id.txtMensaje);
        }

        @Override
        public void onClick(View v) {

        }
    }


    private void populateItemRows(final MensajeRVAdapter.ItemViewHolder viewHolder, int position) {

        Mensaje item = mItemList.get(position);
        viewHolder.usuarioMensaje.setText(item.getNombreUsuario());
        viewHolder.mensaje.setText(item.getTextMensaje());

    }
}
