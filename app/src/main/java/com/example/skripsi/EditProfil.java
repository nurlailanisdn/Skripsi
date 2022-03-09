package com.example.skripsi;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;

public class EditProfil extends AppCompatActivity {
    @BindView(R.id.txt_editNIP)
    EditText editNip;
    @BindView(R.id.txt_editPhone)
    EditText editPhone;
    @BindView(R.id.txt_editPassword)
    EditText editPassword;
    @BindView(R.id.txt_editNama)
    EditText editNama;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    String nip, phone, password, nama, hashPass;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profil);

        nip = editNip.getText().toString();
        phone= editPhone.getText().toString();
        password = editPassword.getText().toString();
        nama = editNama.getText().toString();
        hashPass = getMd5(password);
    }

    @OnClick(R.id.btn_editProfil)
    public void editProfil(){
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("Akun").child(uId);
        dbRef.child("namaPanjang").setValue(nama);
        dbRef.child("nip").setValue(nip);
        dbRef.child("noTelp").setValue(phone);
        dbRef.child("password").setValue(hashPass);

        user.updatePassword(hashPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("Change pw: ", "password updated");
                }
            }
        });

        Toast.makeText(EditProfil.this,"Pengubahan data berhasil!", Toast.LENGTH_SHORT).show();

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

}
