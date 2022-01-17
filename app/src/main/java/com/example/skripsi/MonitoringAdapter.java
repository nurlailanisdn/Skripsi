package com.example.skripsi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.Monitoring;

import java.util.ArrayList;
import java.util.List;

public class MonitoringAdapter extends RecyclerView.Adapter<MonitoringAdapter.MyViewHolder> {
    List<Monitoring> list = new ArrayList<>();
    public MonitoringAdapter(ArrayList<Monitoring> list){

        this.list=list;
    }

    public void updateList(List list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
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
        holder.lokasiMonitoring.setText((int) listMonitoring.getLat());
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
