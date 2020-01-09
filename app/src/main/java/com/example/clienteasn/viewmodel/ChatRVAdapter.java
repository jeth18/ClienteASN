package com.example.clienteasn.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.clienteasn.R;
import com.example.clienteasn.model.ChatGroup;
import com.example.clienteasn.model.Publicacion;
import com.example.clienteasn.services.network.VolleyS;
import com.example.clienteasn.services.persistence.Default;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ChatRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context = null;
    private final ClickListener listener;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String idUsuario;
    Default d;
    private String TAG = "ChatActivity";
    public List<ChatGroup> mItemList;


    public ChatRVAdapter(ArrayList<ChatGroup> itemList, Context context, ClickListener listener) {
        mItemList = itemList;
        this.context = context;
        this.listener = listener;
        volley = VolleyS.getInstance(context);
        fRequestQueue = volley.getRequestQueue();
        Log.d("Chats", mItemList.toString());
        d = Default.getInstance(context);
        idUsuario = d.getUsuario();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_chatgroup, parent, false);
        return new ItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        populateItemRows((ItemViewHolder) viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeakReference<ClickListener> listenerRef;
        private Button btnEntrarChat;
        private TextView txtChatGroup;

        public ItemViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);
            btnEntrarChat = itemView.findViewById(R.id.btnEntrarChat);
            txtChatGroup = itemView.findViewById(R.id.txtChatGroup);

            btnEntrarChat.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == btnEntrarChat.getId()) {
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }
    }

    private void populateItemRows(final ChatRVAdapter.ItemViewHolder viewHolder, int position) {
        ChatGroup item = mItemList.get(position);
        if(idUsuario.compareTo(item.getIdUsuario1()) == 0) {
            viewHolder.txtChatGroup.setText(item.getNombreUsuario2());
        }
        if(idUsuario.compareTo(item.getIdUsuario2()) == 0) {
            viewHolder.txtChatGroup.setText(item.getNombreUsuario1());
        }
    }
}
