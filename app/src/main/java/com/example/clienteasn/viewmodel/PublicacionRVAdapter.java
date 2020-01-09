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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clienteasn.Activities.ComentarioActivity;
import com.example.clienteasn.R;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.model.Reaccion;
import com.example.clienteasn.services.network.ApiEndpoint;
import com.example.clienteasn.services.network.ApiService;
import com.example.clienteasn.services.network.JsonAdapter;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PublicacionRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context = null;
    private final ClickListener listener;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    Default d;
    private String TAG = "AppActivity";

    public List<Publicacion> mItemList;


    public PublicacionRVAdapter(List<Publicacion> itemList, Context context, ClickListener listener) {
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
        TextView tvItemReaccion;
        ImageView imageview;
        Button btnComentarios;
        ImageButton btnMegusta;
        private WeakReference<ClickListener> listenerRef;

        public ItemViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);

            tvItemUrl = itemView.findViewById(R.id.tvItemUrl);
            tvItemDescripcion = itemView.findViewById(R.id.tvItemDescripcion);
            tvItemReaccion = itemView.findViewById(R.id.tvItemreaccion);
            imageview = itemView.findViewById(R.id.imageView);
            btnMegusta = itemView.findViewById(R.id.btnMegusta);
            btnComentarios = itemView.findViewById(R.id.btnComentarios);

            btnMegusta.setOnClickListener(this);
            btnComentarios.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (v.getId() == btnMegusta.getId()) {
                if(!mItemList.get(getAdapterPosition()).isiLike()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("reaccion", "1");
                        jsonObject.put("idUsuario", d.getUsuario());
                    } catch (JSONException e){
                        Log.e(TAG, "Cannot set reaccion jsonobject");
                    }

                    JsonObjectRequest reaccionNuevaRequest = new JsonObjectRequest(Request.Method.PUT,
                            ApiEndpoint.nuevaReaccion + "/" + mItemList.get(getAdapterPosition()).getId(),
                            jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Reaccion resultado = JsonAdapter.reaccionAdapter(response);
                                mItemList.get(getAdapterPosition()).setiLike(true);
                                mItemList.get(getAdapterPosition()).getReacciones().add(resultado);
                                Toast.makeText(context, "Te gusta", Toast.LENGTH_SHORT).show();
                                listenerRef.get().onReaccionarClicked(getAdapterPosition(), resultado);
                                notifyDataSetChanged();

                            } catch (JSONException e) {
                                Log.e(TAG, "Cannot parse JSON response Reaccion");
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, "Testing Network");
                                }
                            }
                    );
                    volley.addToQueue(reaccionNuevaRequest);
                }else if(mItemList.get(getAdapterPosition()).isiLike()) {
                    ArrayList<Reaccion> reacciones = mItemList.get(getAdapterPosition()).getReacciones();
                    for(int i = 0; i < reacciones.size(); i++){
                        if(reacciones.get(i).getUsuarioPropietario().compareTo(d.getUsuario()) == 0) {
                            final int posicionReaccion = i;
                            JsonObjectRequest eliminarReaccionRequest = new JsonObjectRequest(Request.Method.DELETE,
                                    ApiEndpoint.eliminarReaccion + "/" + reacciones.get(i).getId(), null,
                                    new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {

                                            mItemList.get(getAdapterPosition()).setiLike(false);
                                            mItemList.get(getAdapterPosition()).getReacciones().remove(posicionReaccion);
                                            Toast.makeText(context, "No me gusta", Toast.LENGTH_SHORT).show();
                                            listenerRef.get().onDeleteReaccionClicked(getAdapterPosition(), posicionReaccion);
                                            notifyDataSetChanged();

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, "Testing Network");
                                        }
                                    }
                                    );
                            volley.addToQueue(eliminarReaccionRequest);
                            break;
                        }
                    }
                }

            }
            if(v.getId() == btnComentarios.getId()){
                Log.d("comentarios", mItemList.get(getAdapterPosition()).getComentarios().toString());
                Toast.makeText(v.getContext(), "VER COMENTARIOS = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ComentarioActivity.class);
                intent.putExtra("comentarios", mItemList.get(getAdapterPosition()).getComentarios());
                intent.putExtra("idPublicacion", mItemList.get(getAdapterPosition()).getId());
                context.startActivity(intent);
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
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
        ArrayList<Reaccion> reacciones = item.getReacciones();
        viewHolder.tvItemUrl.setText(item.getNombreUsuarioPropietario());
        viewHolder.tvItemDescripcion.setText(item.getDescripcion());
        viewHolder.tvItemReaccion.setText(item.getReacciones().size() + "");
        if(reacciones.size() > 0) {
            for(int i = 0; i < reacciones.size(); i++){
                if(reacciones.get(i).getUsuarioPropietario().compareTo(d.getUsuario()) == 0) {
                    mItemList.get(position).setiLike(true);
                    viewHolder.btnMegusta.setBackgroundResource(R.drawable.ic_favorite_border_red);
                } else {
                    mItemList.get(position).setiLike(false);
                    viewHolder.btnMegusta.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        } else {
            mItemList.get(position).setiLike(false);
            viewHolder.btnMegusta.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        }



        String url = "http://10.0.2.2:8080/public/" + item.getFotoURL();
        Picasso.with(context).setLoggingEnabled(true);


        Picasso.with(context).load(url).error(R.drawable.ic_missing)
                .into(viewHolder.imageview);
        Log.d("url", url);



    }
}
