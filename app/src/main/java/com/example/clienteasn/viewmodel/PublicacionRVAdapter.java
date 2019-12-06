package com.example.clienteasn.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clienteasn.Activities.ComentarioActivity;
import com.example.clienteasn.R;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.ApiService;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class PublicacionRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ApiService apiService;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context = null;
    private final ClickListener listener;


    public List<Publicacion> mItemList;


    public PublicacionRVAdapter(List<Publicacion> itemList, Context context, ClickListener listener) {
        mItemList = itemList;
        this.context = context;
        this.listener = listener;
        Log.d("Lista publicaciones", mItemList.toString());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view, listener);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvItemUrl;
        TextView tvItemDescripcion;
        ImageView imageview;
        Button btnComentarios;
        ImageButton btnMegusta;
        private WeakReference<ClickListener> listenerRef;

        public ItemViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);

            tvItemUrl = itemView.findViewById(R.id.tvItemUrl);
            tvItemDescripcion = itemView.findViewById(R.id.tvItemDescripcion);
            imageview = itemView.findViewById(R.id.imageView);
            btnMegusta = itemView.findViewById(R.id.btnMegusta);
            btnComentarios = itemView.findViewById(R.id.btnComentarios);

            btnMegusta.setOnClickListener(this);
            btnComentarios.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (v.getId() == btnMegusta.getId()) {
                Toast.makeText(v.getContext(), "ME GUSTA = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();

            }
            if(v.getId() == btnComentarios.getId()){
                Log.d("comentarios", mItemList.get(getAdapterPosition()).getComentarios().toString());
                Toast.makeText(v.getContext(), "VER COMENTARIOS = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ComentarioActivity.class);
                intent.putExtra("comentarios", mItemList.get(getAdapterPosition()).getComentarios());
                intent.putExtra("idPublicacion", mItemList.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            }

            listenerRef.get().onPositionClicked(getAdapterPosition());
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(final ItemViewHolder viewHolder, int position) {

        Publicacion item = mItemList.get(position);
        viewHolder.tvItemUrl.setText(item.getNombreUsuarioPropietario());
        viewHolder.tvItemDescripcion.setText(item.getDescripcion());

        String url = "http://10.0.2.2:8080/public/" + item.getFotoURL();
        Picasso.with(context).setLoggingEnabled(true);


        Picasso.with(context).load(url).error(R.drawable.ic_missing)
                .into(viewHolder.imageview);
        Log.d("url", url);



    }
}
