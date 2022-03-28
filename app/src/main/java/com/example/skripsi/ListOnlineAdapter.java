package com.example.skripsi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListOnline;
import com.example.skripsi.model.ListPermintaanAkun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListOnlineAdapter extends RecyclerView.Adapter<ListOnlineAdapter.MyViewHolder> {
    List<ListOnline> list =  new ArrayList<>();
    Context context;


    public ListOnlineAdapter(Context context,ArrayList<ListOnline> list) {

        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOnlineAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListOnline listOnline = list.get(position);
        holder.txt_nama.setText(listOnline.getNamaPanjang());
        holder.txt_nip.setText(listOnline.getNip());
        holder.txt_role.setText(listOnline.getRole());
        holder.txt_lastSeen.setText(listOnline.getLastSeen());
        holder.cardViewListOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfilActivity.class);
                i.putExtra("profilData", listOnline);
                context.startActivity(i);
            }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_nama, txt_nip, txt_role, txt_lastSeen;
        CardView cardViewListOnline;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nama = itemView.findViewById(R.id.txtNamaAkun);
            txt_nip = itemView.findViewById(R.id.txtNIPAkun);
            txt_role = itemView.findViewById(R.id.txtRoleAkun);
            txt_lastSeen = itemView.findViewById(R.id.txtLastSeenAkun);
            cardViewListOnline=itemView.findViewById(R.id.cardViewListOnline);
        }
    }
}