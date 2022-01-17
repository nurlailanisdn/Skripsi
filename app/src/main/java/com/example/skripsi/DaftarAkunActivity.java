package com.example.skripsi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.model.ListDaftarAkun;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarAkunActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @BindView(R.id.txt_nip_daftar)
    EditText nipDaftar;
    @BindView(R.id.txt_nama_daftar)
    EditText namaDaftar;
    @BindView(R.id.txt_email_daftar)
    EditText emailDaftar;
    @BindView(R.id.txt_phone_daftar)
    EditText phoneDaftar;
    @BindView(R.id.txt_password_daftar)
    EditText passwordDaftar;
    @BindView(R.id.txt_repassword_daftar)
    EditText repasswordDaftar;
    @BindView(R.id.role_daftar)
    RadioGroup roleDaftar;
    @BindView(R.id.radiobtn_atasan)
    RadioButton radioBtnAtasan;
    @BindView(R.id.radiobtn_sales)
    RadioButton radioBtnSales;
    @BindView(R.id.radiobtn_teknisi)
    RadioButton radioBtnTeknisi;
    @BindView(R.id.btn_daftarAkun)
    Button btnDaftarAkun;
    FirebaseAuth mAuth;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_akun);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        mDatabase =  FirebaseDatabase.getInstance().getReference();
    }

    @OnClick (R.id.btn_daftarAkun)
    public void regisAkun(){
        if(nipDaftar.getText().toString().isEmpty()){
            nipDaftar.setError("Tidak boleh kosong!");
            return;
        }
        if(namaDaftar.getText().toString().isEmpty()){
            namaDaftar.setError("Tidak boleh kosong!");
            return;
        }
        if(emailDaftar.getText().toString().isEmpty()){
            emailDaftar.setError("Tidak boleh kosong!");
            return;
        }
        if(passwordDaftar.getText().toString().isEmpty()){
            passwordDaftar.setError("Tidak boleh kosong!");
            return;
        }
        if(repasswordDaftar.getText().toString().isEmpty()){
            repasswordDaftar.setError("Tidak boleh kosong!");
            return;
        }
        if(!radioBtnAtasan.isChecked() && !radioBtnSales.isChecked() && !radioBtnTeknisi.isChecked()){
            radioBtnAtasan.setError("Pilih salah satu!");
        }

        sendData();
    }

    public static String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }  // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendData() {
        int selectedId = roleDaftar.getCheckedRadioButtonId();
        String result = "";

        if (selectedId == radioBtnAtasan.getId()) {
            result = "Atasan";
        } else if (selectedId == radioBtnSales.getId()) {
            result = "Sales";
        } else if (selectedId == radioBtnTeknisi.getId()) {
            result = "Teknisi";
        }

        String nip = nipDaftar.getText().toString();
        String nama = namaDaftar.getText().toString();
        String email = emailDaftar.getText().toString();
        String phone = phoneDaftar.getText().toString();
        String password = passwordDaftar.getText().toString();
        String repassword = repasswordDaftar.getText().toString();
        String role = result;

        String hashPass = getMd5(password);

        if (password.equals(repassword)) {
            if (result == "Atasan") {
                mAuth= FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email,hashPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String id = mAuth.getCurrentUser().getUid();
                            Log.d("Create User: ","getting Uid "+id);
                            ListDaftarAkun User = new ListDaftarAkun(nip, nama, email, phone, hashPass, role, id);
                            mDatabase.child("Pre-Akun").child(id).setValue(User);
                            Intent i = new Intent(DaftarAkunActivity.this, LoginActivity.class);
                            startActivity(i);
                        } else {
                            Log.e("Create User: ", "Failed"+ task.getException());
                        }
                    }
                });

            } else if (result == "Sales" || result == "Teknisi") {
                preferences=getSharedPreferences("daftarAkun", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nip",nip);
                editor.putString("nama",nama);
                editor.putString("email", email);
                editor.putString("phone", phone);
                editor.putString("pass", hashPass);
                editor.putString("role", role);
                editor.commit();
                Intent i = new Intent(DaftarAkunActivity.this, DaftarAkunLanjActivity.class);
                startActivity(i);
            }
        } else {
            repasswordDaftar.setError("Password tidak sama!");
        }
    }
}
