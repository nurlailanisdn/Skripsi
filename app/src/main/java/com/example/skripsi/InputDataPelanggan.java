package com.example.skripsi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputDataPelanggan extends AppCompatActivity {
    private SharedPreferences preferences;

    @BindView(R.id.txt_kodeSales)
    EditText kodeSalesInput;
    @BindView(R.id.txt_nomorMobi)
    EditText nomorMobiInput;
    @BindView(R.id.txt_namaPelanggan)
    EditText namaPelangganInput;
    @BindView(R.id.txt_noTelpPelanggan)
    EditText noTelpPelangganInput;
    @BindView(R.id.txt_noTelpAlt)
    EditText noTelpAltInput;
    @BindView(R.id.txt_relasiPelanggan)
    EditText relasiPelangganInput;
    @BindView(R.id.btn_lanjutInputData)
    Button lanjutInputData;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data_pelanggan);
        ButterKnife.bind(this);
    }

    @OnClick (R.id.btn_lanjutInputData)
    public void inputData() {
        if (kodeSalesInput.getText().toString().isEmpty()) {
            kodeSalesInput.setError("Tidak boleh kosong!");
            return;
        }
        if (nomorMobiInput.getText().toString().isEmpty()) {
            nomorMobiInput.setError("Tidak boleh kosong!");
            return;
        }
        if (namaPelangganInput.getText().toString().isEmpty()) {
            namaPelangganInput.setError("Tidak boleh kosong!");
            return;
        }
        if (noTelpPelangganInput.getText().toString().isEmpty()) {
            noTelpPelangganInput.setError("Tidak boleh kosong!");
            return;
        }
        if (noTelpAltInput.getText().toString().isEmpty()) {
            noTelpAltInput.setError("Tidak boleh kosong!");
            return;
        }
        if (relasiPelangganInput.getText().toString().isEmpty()) {
            relasiPelangganInput.setError("Tidak boleh kosong!");
            return;
        } else {
            saveData();
        }
    }

        public void saveData() {
            String kodeSales = kodeSalesInput.getText().toString();
            String nomorMobi = nomorMobiInput.getText().toString();
            String namaPelanggan = namaPelangganInput.getText().toString();
            String noTelpPelanggan = noTelpPelangganInput.getText().toString();
            String noTelpAlt = noTelpAltInput.getText().toString();
            String relasiPelanggan = relasiPelangganInput.getText().toString();

            preferences = getSharedPreferences("inputDataPelanggan", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("kodeSales", kodeSales);
            editor.putString("nomorMobi", nomorMobi);
            editor.putString("namaPelanggan", namaPelanggan);
            editor.putString("noTelpPelanggan", noTelpPelanggan);
            editor.putString("noTelpAlt", noTelpAlt);
            editor.putString("relasiPelanggan", relasiPelanggan);
            editor.commit();

            startActivity(new Intent(InputDataPelanggan.this, InputDataPelangganLnjtn.class));
            finish();
        }
    }
