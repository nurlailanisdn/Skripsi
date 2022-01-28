package com.example.skripsi;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.Monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonitoringAdapter extends RecyclerView.Adapter<MonitoringAdapter.MyViewHolder> {
    ArrayList<Monitoring> list = new ArrayList<>();
    public MonitoringAdapter(ArrayList<Monitoring> list){

        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_onwork_monitoring, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Monitoring listMonitoring = list.get(position);
        holder.waktuMulai.setText(listMonitoring.getMulaiKerja());
        holder.waktuBerhenti.setText(listMonitoring.getSelesaiKerja());
        holder.lokasiMonitoring.setText(listMonitoring.getLat() +", "+ listMonitoring.getLongt());
        holder.lokasiMonitoring.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_location_on, 0,0,0);
        holder.lokasiMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", listMonitoring.getLat(), listMonitoring.getLongt());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView waktuMulai,waktuBerhenti, lokasiMonitoring;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            waktuMulai= itemView.findViewById(R.id.txt_waktumulai_monitoring);
            waktuBerhenti=itemView.findViewById(R.id.txt_waktuberhenti_monitoring);
            lokasiMonitoring= itemView.findViewById(R.id.txt_lokasi_monitoring);
        }
    }
}
