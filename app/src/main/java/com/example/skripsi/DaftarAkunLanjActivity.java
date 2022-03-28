package com.example.skripsi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.model.ListDaftarAkun;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarAkunLanjActivity extends AppCompatActivity{
    private DatabaseReference mDatabase;

//    @BindView(R.id.txt_inpt_atasan)
//    EditText inpt_atasan;
    @BindView(R.id.btn_daftar_lanj)
    Button btnDaftarAkun;
    FirebaseAuth mAuth;
    private SharedPreferences preferences;
    String spinnerNamaAtasan;
    Spinner listNamaAtasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_akun_lnjtn);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        listNamaAtasan = findViewById(R.id.spinner_namaAtasan);
    }


    @OnClick(R.id.btn_daftar_lanj)

    public void regisAkun(){
        if (listNamaAtasan.toString().isEmpty()){
            Toast.makeText(DaftarAkunLanjActivity.this, "Harus pilih salah satu!", Toast.LENGTH_SHORT).show();
        }
        sendData();
    }

    private void sendData(){
//        String namaAtasan = inpt_atasan.getText().toString();

        preferences=getSharedPreferences("daftarAkun", Context.MODE_PRIVATE);
        String role = preferences.getString("role","empty");
        String nip = preferences.getString("nip","empty");
        String nama= preferences.getString("nama","empty");
        String email = preferences.getString("email", "empty");
        String phone = preferences.getString("phone", "empty");
        String pass = preferences.getString("pass", "empty");

        if (role.equalsIgnoreCase("Sales") || role.equalsIgnoreCase("Teknisi")){
            mAuth= FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String uId = mAuth.getCurrentUser().getUid();
                        Log.d("Create User","getting Uid "+uId);
                        ListDaftarAkun User = new ListDaftarAkun(nip,nama,email,phone,pass,role,uId,listNamaAtasan.getSelectedItem().toString());
                        mDatabase.child("Pre-Akun").child(uId).setValue(User);
                        Toast.makeText(DaftarAkunLanjActivity.this, "Pendaftaran akun selesai, silahkan tunggu konfirmasi dari admin", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(DaftarAkunLanjActivity.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        Log.e("Create User", "Failed "+ task.getException());
                        Toast.makeText(DaftarAkunLanjActivity.this,"Pendaftaran akun gagal!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Role null", Toast.LENGTH_SHORT).show();
        }
    }
}
