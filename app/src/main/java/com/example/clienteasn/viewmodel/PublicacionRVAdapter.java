package com.example.clienteasn.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clienteasn.R;
import com.example.clienteasn.model.Publicacion;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PublicacionRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context = null;

    public List<Publicacion> mItemList;


    public PublicacionRVAdapter(List<Publicacion> itemList, Context context) {

        mItemList = itemList;
        this.context = context;
        Log.d("Lista publicaciones", mItemList.toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view);
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


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemUrl;
        TextView tvItemDescripcion;
        ImageView imageview;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemUrl = itemView.findViewById(R.id.tvItemUrl);
            tvItemDescripcion = itemView.findViewById(R.id.tvItemDescripcion);
            imageview = itemView.findViewById(R.id.imageView);

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

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Publicacion item = mItemList.get(position);
        viewHolder.tvItemUrl.setText(item.getFotoURL());
        viewHolder.tvItemDescripcion.setText(item.getDescripcion());
        String url = "http://localhost:8080/api/Publicacion/uploads/" + item.getFotoURL();
        Log.d("url", url);

        Picasso.with(context).load(url).into(viewHolder.imageview);

    }
}
