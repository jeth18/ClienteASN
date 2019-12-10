package com.example.clienteasn.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.clienteasn.R;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ModeradorRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context = null;
    private final ClickListener listener;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    Default d;
    private String TAG = "AppActivity";


    public List<Publicacion> mItemList;


    public ModeradorRVAdapter(List<Publicacion> itemList, Context context, ClickListener listener) {
        mItemList = itemList;
        this.context = context;
        this.listener = listener;
        volley = VolleyS.getInstance(context);
        fRequestQueue = volley.getRequestQueue();
        Log.d("Lista publicaciones", mItemList.toString());
        d = Default.getInstance(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_moderador, parent, false);
            return new ModeradorRVAdapter.ItemViewHolder(view, listener);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new ModeradorRVAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ModeradorRVAdapter.ItemViewHolder) {

            populateItemRows((ModeradorRVAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof ModeradorRVAdapter.LoadingViewHolder) {
            showLoadingView((ModeradorRVAdapter.LoadingViewHolder) viewHolder, position);
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

        TextView tvItemUrlModerador;
        TextView tvItemDescripcionModerador;
        ImageView imageviewModerador;
        Button btnEliminarPublicacion;
        private WeakReference<ClickListener> listenerRef;

        public ItemViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);

            tvItemUrlModerador = itemView.findViewById(R.id.tvItemUrlModerador);
            tvItemDescripcionModerador = itemView.findViewById(R.id.tvItemDescripcionModerador);
            imageviewModerador = itemView.findViewById(R.id.imageViewModerador);

            btnEliminarPublicacion = itemView.findViewById(R.id.btnEliminarPublicacion);

            btnEliminarPublicacion.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (v.getId() == btnEliminarPublicacion.getId()) {

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

    private void showLoadingView( ModeradorRVAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(final ModeradorRVAdapter.ItemViewHolder viewHolder, int position) {

        Publicacion item = mItemList.get(position);
        ArrayList<Reaccion> reacciones = item.getReacciones();
        viewHolder.tvItemUrlModerador.setText(item.getNombreUsuarioPropietario());
        viewHolder.tvItemDescripcionModerador.setText(item.getDescripcion());

        String url = "http://10.0.2.2:8080/public/" + item.getFotoURL();
        Picasso.with(context).setLoggingEnabled(true);


        Picasso.with(context).load(url).error(R.drawable.ic_missing)
                .into(viewHolder.imageviewModerador);
        Log.d("url", url);



    }
}
