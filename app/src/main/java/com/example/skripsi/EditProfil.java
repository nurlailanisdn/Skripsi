package com.example.skripsi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.model.ListOnline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfil extends AppCompatActivity {
    @BindView(R.id.txt_editNIP)
    EditText editNip;
    @BindView(R.id.txt_editPhone)
    EditText editPhone;
    @BindView(R.id.txt_editNama)
    EditText editNama;
    FirebaseDatabase db;
    DatabaseReference dbRef, dbRef2;
    FirebaseUser user;
    String uId;
    String role;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profil);
        db = FirebaseDatabase.getInstance();
        ButterKnife.bind(this);

        user= FirebaseAuth.getInstance().getCurrentUser();
        uId= user.getUid();

        dbRef2 = db.getReference().child("Akun").child(uId);
        dbRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListOnline profil = snapshot.getValue(ListOnline.class);

                String namaAwal = profil.getNamaPanjang();
                String nipAwal = profil.getNip();
                String nomorTelponAwal= profil.getNoTelp();
                role = profil.getRole();

                editNama.setText(namaAwal);
                editNip.setText(nipAwal);
                editPhone.setText(nomorTelponAwal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfil.this, "Gagal memanggil data!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.btn_editProfil)
    public void editProfil(){
        String nip = editNip.getText().toString();
        String phone= editPhone.getText().toString();
        String nama = editNama.getText().toString();
        dbRef = db.getReference().child("Akun").child(uId);

        dbRef.child("namaPanjang").setValue(nama);
        dbRef.child("nip").setValue(nip);
        dbRef.child("noTelp").setValue(phone);
        Toast.makeText(EditProfil.this,"Pengubahan data berhasil!", Toast.LENGTH_SHORT).show();
        if (role.equalsIgnoreCase("Teknisi")) {
            startActivity(new Intent(EditProfil.this, TeknisiActivity.class));
        } else if (role.equalsIgnoreCase("Sales")){
            startActivity(new Intent(EditProfil.this, SalesActivity.class));
        } else {
            startActivity(new Intent(EditProfil.this, MainActivity.class));
        }
    }
}
