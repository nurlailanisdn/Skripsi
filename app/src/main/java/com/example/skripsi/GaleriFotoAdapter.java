package com.example.skripsi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListGaleriFoto;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class GaleriFotoAdapter extends RecyclerView.Adapter<GaleriFotoAdapter.MyViewHolder> {
    ArrayList<ListGaleriFoto> list = new ArrayList<>();
    Context context;

    public  GaleriFotoAdapter(Context context, ArrayList<ListGaleriFoto>list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_gallery, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListGaleriFoto listGaleriFoto = list.get(position);
        holder.uploaderGaleri.setText(listGaleriFoto.getNamaPanjang());
        holder.tanggalGaleri.setText(listGaleriFoto.getTanggalFotoKerja());
        holder.judulGaleri.setText(listGaleriFoto.getJudulFotoKerja());
        Picasso.with(context)
                .load(listGaleriFoto.getImageURL())
                .into(holder.imageViewGaleri);
        holder.cardViewListGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PhotoDetailActivity.class);
                i.putExtra("photoData", listGaleriFoto);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tanggalGaleri, judulGaleri, uploaderGaleri;
        ImageView imageViewGaleri;
        CardView cardViewListGaleri;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tanggalGaleri=itemView.findViewById(R.id.tanggal_gallery);
            judulGaleri=itemView.findViewById(R.id.judul_gallery);
            uploaderGaleri= itemView.findViewById(R.id.uploader_gallery);
            imageViewGaleri=itemView.findViewById(R.id.imageView_gallery);
            cardViewListGaleri= itemView.findViewById(R.id.cardViewListGaleri);
        }
    }
}
