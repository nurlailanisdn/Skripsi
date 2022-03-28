package com.example.skripsi;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListKomentar;

import java.util.ArrayList;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.MyViewHolder> {
    ArrayList<ListKomentar> list = new ArrayList<>();

    public KomentarAdapter (ArrayList<ListKomentar> list){
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_photo_detail, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListKomentar listKomentar = list.get(position);
        holder.namaPanjang.setText(listKomentar.getNamaPanjang());
        holder.isiKomentar.setText(listKomentar.getKomentar());
    }


    @Override
    public int getItemCount(){
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView namaPanjang, isiKomentar;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            namaPanjang=itemView.findViewById(R.id.tvNamaKomentar);
            isiKomentar=itemView.findViewById(R.id.tvIsiKomentar);
        }
    }

}
