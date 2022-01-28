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

public class DaftarAkunLanjActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseReference mDatabase;

//    @BindView(R.id.txt_inpt_atasan)
//    EditText inpt_atasan;
    @BindView(R.id.btn_daftar_lanj)
    Button btnDaftarAkun;
    @BindView(R.id.spinner_namaAtasan)
            Spinner namaAtasan;
    FirebaseAuth mAuth;
    private SharedPreferences preferences;
    String spinnerNamaAtasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_akun_lnjtn);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        Intent i = this.getIntent();
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        namaAtasan.setOnItemSelectedListener(this);
    }


    @OnClick(R.id.btn_daftar_lanj)

    public void regisAkun(){
        if (spinnerNamaAtasan.isEmpty()){
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
                        String id = mAuth.getCurrentUser().getUid();
                        Log.d("Create User","getting Uid "+id);
                        ListDaftarAkun User = new ListDaftarAkun(nama, nip,email, phone, pass, role, id, spinnerNamaAtasan);
                        mDatabase.child("Pre-Akun").child(id).setValue(User);
                        Toast.makeText(DaftarAkunLanjActivity.this, "Pendaftaran akun selesai, silahkan tunggu konfirmasi dari admin", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(DaftarAkunLanjActivity.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        Log.e("Create User", "Failed "+ task.getException());
                    }
                }
            });
        } else {
            Toast.makeText(this, "Role null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerNamaAtasan = parent.getItemAtPosition(position).toString();
        List<String> list = new ArrayList<String>();
        list.add("Atasan 1");
        list.add("Atasan 2");
        list.add("Atasan 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        namaAtasan.setAdapter(dataAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
