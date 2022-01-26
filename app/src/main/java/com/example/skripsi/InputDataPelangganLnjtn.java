package com.example.skripsi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputDataPelangganLnjtn extends AppCompatActivity {
    DatabaseReference dbRef;
    final Calendar myCalendar = Calendar.getInstance();

    @BindView(R.id.alamat_pelanggan)
    EditText alamatPlgn;
    @BindView(R.id.patokan_alamat)
    EditText patokanAlmt;
    @BindView(R.id.tanggal_instalasi)
    EditText tanggalInstalasi;
    @BindView(R.id.btn_foto_ktp)
    Button fotoKTP;
    @BindView(R.id.btn_lanjut_input_data)
    Button simpanData;
    @BindView(R.id.fotoKtp)
    ImageView imageFotoKtp;
    private static final int CAMERA_PIC_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data_pelanggan_lanjutan);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_lanjut_input_data)
    public void verifData(){
        if (alamatPlgn.getText().toString().isEmpty()){
            alamatPlgn.setError("Tidak boleh kosong!");
        }
        if (patokanAlmt.getText().toString().isEmpty()){
            patokanAlmt.setError("Tidak boleh kosong!");
        }
        if (tanggalInstalasi.getText().toString().isEmpty()){
            tanggalInstalasi.setError("Tidak boleh kosong!");
        }
        if (fotoKTP.getText().toString().isEmpty()){
            fotoKTP.setError("Tidak boleh kosong!");
        }

        sendData();
    }

    public void sendData() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        tanggalInstalasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InputDataPelangganLnjtn.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fotoKTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
    }
        protected void onActivityResult (int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CAMERA_PIC_REQUEST) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imageFotoKtp.setImageBitmap(image);
            }
        }

    private void updateLabel(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        tanggalInstalasi.setText(dateFormat.format(myCalendar.getTime()));
    }

}
