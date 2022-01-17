package com.example.skripsi;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListPermintaanAkun;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.List;

public class PermintaanAkunAdapter extends RecyclerView.Adapter<PermintaanAkunAdapter.MyViewHolder>{
    List<ListPermintaanAkun> list = new ArrayList<>();
    private IOnItemClickListener callback;

    public interface IOnItemClickListener{
        void onItemPermintaanAkunAdapterClickListener(boolean activate, ListPermintaanAkun listPermintaanAkun);
    }

    public PermintaanAkunAdapter( ArrayList<ListPermintaanAkun> list,IOnItemClickListener itemClickListener){
        this.list=list;
        callback=itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_daftar_akun,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListPermintaanAkun listPermintaanAkun = list.get(position);
        holder.txt_nama.setText(listPermintaanAkun.getNamaPanjang());
        holder.txt_nip.setText(listPermintaanAkun.getNip());
        holder.txt_role.setText(listPermintaanAkun.getRole());
        holder.txt_noTelp.setText(listPermintaanAkun.getNoTelp());
        holder.btn_accAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = holder.radioGroup_acc.getCheckedRadioButtonId();
                if (radioButtonId == holder.radioButton_acc.getId()) {
                    callback.onItemPermintaanAkunAdapterClickListener(true, list.get(position));
                } else if (radioButtonId == holder.radioButton_del.getId()) {
                    callback.onItemPermintaanAkunAdapterClickListener(false, list.get(position));
                }else {
                    Toast.makeText(v.getContext(),"Pilih salah satu!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_nama, txt_nip, txt_role, txt_noTelp;
        Button btn_accAkun;
        RadioGroup radioGroup_acc;
        RadioButton radioButton_acc, radioButton_del;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            radioGroup_acc= itemView.findViewById(R.id.radiogrp_acc);
            radioButton_acc= itemView.findViewById(R.id.radioBtn_acc);
            radioButton_del=itemView.findViewById(R.id.radioBtn_delete);
            txt_nama=itemView.findViewById(R.id.txtNama_DaftarAkun);
            txt_nip=itemView.findViewById(R.id.txtNIP_DaftarAkun);
            txt_role=itemView.findViewById(R.id.txtRole_DaftarAkun);
            txt_noTelp=itemView.findViewById(R.id.txtNoTelp_DaftarAkun);
            btn_accAkun=itemView.findViewById(R.id.btn_submitAcc);
        }
    }
}
