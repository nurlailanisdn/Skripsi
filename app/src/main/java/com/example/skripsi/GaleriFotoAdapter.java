package com.example.skripsi;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListGaleriFoto;

import java.util.ArrayList;

public class GaleriFotoAdapter extends RecyclerView.Adapter<GaleriFotoAdapter.MyViewHolder> {
    ArrayList<ListGaleriFoto> list = new ArrayList<>();

    public  GaleriFotoAdapter(ArrayList<ListGaleriFoto>list){
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
        holder.imageViewGaleri.setImageURI(listGaleriFoto.getUri());
        holder.uploaderGaleri.setText(listGaleriFoto.getUploaderGaleri());
        holder.tanggalGaleri.setText(listGaleriFoto.getTanggalGaleri());
        holder.judulGaleri.setText(listGaleriFoto.getJudulGaleri());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tanggalGaleri, judulGaleri, uploaderGaleri;
        ImageView imageViewGaleri;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tanggalGaleri=itemView.findViewById(R.id.tanggal_gallery);
            judulGaleri=itemView.findViewById(R.id.judul_gallery);
            uploaderGaleri= itemView.findViewById(R.id.uploader_gallery);
            imageViewGaleri=itemView.findViewById(R.id.imageView_gallery);
        }
    }
}
