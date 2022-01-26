package com.example.skripsi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.model.InputDataPlg;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputDataPelanggan extends AppCompatActivity {
    DatabaseReference dbRef;
    @BindView(R.id.kode_sales)
    EditText kodeSales;
    @BindView(R.id.nomor_mobi)
    EditText noMobi;
    @BindView(R.id.nama_pelanggan)
    EditText namaPlg;
    @BindView(R.id.nomor_telp)
    EditText noTelp;
    @BindView(R.id.nomor_telp_alt)
    EditText noTelpAlt;
    @BindView(R.id.relasi_pelanggan)
    EditText relasiPlg;
    @BindView(R.id.btn_lanjut_input_data)
    Button btnLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data_pelanggan);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_lanjut_input_data)
    public void verifData(){
        if (kodeSales.getText().toString().isEmpty()){
            kodeSales.setError("Tidak boleh kosong!");
        }
        if (noMobi.getText().toString().isEmpty()){
            noMobi.setError("Tidak boleh kosong!");
        }
        if (namaPlg.getText().toString().isEmpty()){
            namaPlg.setError("Tidak boleh kosong!");
        }
        if (noTelp.getText().toString().isEmpty()){
            noTelp.setError("Tidak boleh kosong!");
        }
        if (noTelpAlt.getText().toString().isEmpty()){
            noTelpAlt.setError("Tidak boleh kosong!");
        }
        if (relasiPlg.getText().toString().isEmpty()){
            relasiPlg.setError("Tidak boleh kosong!");
        }
        sendData();
    }

    public void sendData(){
        dbRef= FirebaseDatabase.getInstance().getReference();
        String kode=kodeSales.getText().toString();
        String nomorMobi= noMobi.getText().toString();
        String namaPlgn= namaPlg.getText().toString();
        String noTlpn = noTelp.getText().toString();
        String noTlpnAlt= noTelpAlt.getText().toString();
        String relasiPlgn= relasiPlg.getText().toString();

        InputDataPlg inputData = new InputDataPlg(kode,nomorMobi,namaPlgn,noTlpn,noTlpnAlt,relasiPlgn);
        dbRef.child("DataPelanggan").push().setValue(inputData);
        Log.d("Input data: ", "Berhasil!");
//        Toast.makeText(this, "Data berhasil tersimpan!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(InputDataPelanggan.this, InputDataPelangganLnjtn.class));

    }
}
