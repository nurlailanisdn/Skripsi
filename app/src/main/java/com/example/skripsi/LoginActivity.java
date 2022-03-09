package com.example.skripsi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    @BindView(R.id.txt_emailLogin)
    EditText txt_email;
    @BindView(R.id.txt_passLogin)
    EditText txt_pass;
    @BindView(R.id.txt_resetPassword)
    TextView txt_resetPassword;
    @BindView(R.id.txt_daftarAkun)
    TextView txt_daftarAkun;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()==null){
        txt_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPassword.class));
            }
        });
        txt_daftarAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, DaftarAkunActivity.class);
                startActivity(i);
            }
        });
    } else {
            mAuth.signOut();
        }
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

    @OnClick(R.id.btn_login)
    public void OnLogin() {
        if (txt_email.getText().toString().isEmpty()) {
            txt_email.setError("Tidak boleh kosong!");
        }
        if (txt_pass.getText().toString().isEmpty()) {
            txt_pass.setError("Tidak boleh kosong!");
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                loginUser();
            } else {
                FirebaseAuth.getInstance().signOut();
            }
        }
    }

    private void loginUser(){
        String email = txt_email.getText().toString();
        String pass = txt_pass.getText().toString();
        String hashPass = getMd5(pass);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth.signInWithEmailAndPassword(email, hashPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //success
                            Toast.makeText(LoginActivity.this,"Proses login. . .", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "signInWithEmail:success");
                            String uId = mAuth.getCurrentUser().getUid();
                            FirebaseUser user= mAuth.getCurrentUser();
                            if(user!=null) {
                                mDatabase.child("Akun").child(uId).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("Firebase", "Error getting data", task.getException());
                                        } else {
                                            String currentRole = String.valueOf(task.getResult().getValue());
                                            Log.d("Role: ", currentRole);
                                            if (currentRole!=null) {
                                                switch (currentRole) {
                                                    case "Admin":
                                                        Intent a = new Intent(LoginActivity.this, PermintaanAkun.class);
                                                        startActivity(a);
                                                        finish();
                                                        break;
                                                    case "Atasan":
                                                        Intent b = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(b);
                                                        finish();
                                                        break;
                                                    case "Teknisi":
                                                        Intent c = new Intent(LoginActivity.this, TeknisiActivity.class);
                                                        startActivity(c);
                                                        finish();
                                                        break;
                                                    case "Sales":
                                                        startActivity(new Intent(LoginActivity.this, SalesActivity.class));
                                                        break;
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this,"Akun belum diaktivasi oleh admin", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(LoginActivity.this,"user is null", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //failed
                            Log.w("TAG", "signInwWithEmail: failure", task.getException());
                            Toast.makeText(LoginActivity.this,"Login gagal",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
