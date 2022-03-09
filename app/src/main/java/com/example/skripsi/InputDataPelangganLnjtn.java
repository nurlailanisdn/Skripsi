package com.example.skripsi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.model.InputData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputDataPelangganLnjtn extends AppCompatActivity {
    private DatabaseReference dbRef;
    private FirebaseStorage storage;
    StorageReference storageReference;

    @BindView(R.id.txt_alamatPelanggan)
    EditText input_alamatPelanggan;
    @BindView(R.id.txt_patokanAlamat)
    EditText input_patokanAlamat;
    @BindView(R.id.txt_tanggalInstalasi)
    EditText input_tanggalInstalasi;
    @BindView(R.id.btn_inputData)
    Button inputData;
    @BindView(R.id.btn_uploadKTP)
    Button upload_ktp;
    final Calendar myCalendar=Calendar.getInstance();
    final int CAMERA_PIC_REQUEST = 1337;
    Bitmap fotoKtp;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data_pelanggan_lnjtn);
        dbRef= FirebaseDatabase.getInstance().getReference();
        ButterKnife.bind(this);
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        //datepicker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        input_tanggalInstalasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InputDataPelangganLnjtn.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button uploadKTP =  (Button) findViewById(R.id.btn_uploadKTP);
        uploadKTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            fotoKtp = (Bitmap) data.getExtras().get("data");

        }
    }
    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        input_tanggalInstalasi.setText(dateFormat.format(myCalendar.getTime()));
    }

    @OnClick (R.id.btn_inputData)
    public void inputData() {
        if (input_alamatPelanggan.getText().toString().isEmpty()) {
            input_alamatPelanggan.setError("Tidak boleh kosong!");
            return;
        }
        if (input_patokanAlamat.getText().toString().isEmpty()) {
            input_patokanAlamat.setError("Tidak boleh kosong");
            return;
        }
        if (input_tanggalInstalasi.getText().toString().isEmpty()) {
            input_tanggalInstalasi.setError("Tidak boleh kosong");
            return;
        }
        if (fotoKtp!= null) {
            Toast.makeText(InputDataPelangganLnjtn.this, "Foto KTP masih kosong!", Toast.LENGTH_LONG).show();
            return;
        } else{
            sendData();
    }
    }


    public void sendData(){
        preferences= getSharedPreferences("inputDataPelanggan", Context.MODE_PRIVATE);
        String kodeSales = preferences.getString("kodeSales","empty");
        String nomorMobi = preferences.getString("nomorMobi", "empty");
        String namaPelanggan = preferences.getString("namaPelanggan", "empty");
        String noTelpPelanggan = preferences.getString("noTelpPelanggan", "empty");
        String noTelpAlt = preferences.getString("noTelpAlt","empty");
        String relasiPelanggan = preferences.getString("relasiPelanggan","empty");

        String alamatPelanggan = input_alamatPelanggan.getText().toString();
        String patokanAlamat = input_patokanAlamat.getText().toString();
        String tanggalInstalasi = input_tanggalInstalasi.getText().toString();
        //send to db
        InputData data = new InputData(kodeSales,nomorMobi,namaPelanggan,noTelpPelanggan,noTelpAlt,relasiPelanggan,alamatPelanggan,patokanAlamat,tanggalInstalasi,fotoKtp);
        dbRef.child("DataPelanggan").push().setValue(data);
        Toast.makeText(InputDataPelangganLnjtn.this,"Menyimpan data pelanggan", Toast.LENGTH_LONG).show();
        startActivity(new Intent(InputDataPelangganLnjtn.this, SalesActivity.class));
    }
}
