package com.example.skripsi;

import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListDaftarAkun;
import com.example.skripsi.model.ListGaleriFoto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class GaleriFoto extends AppCompatActivity {
    ArrayList<ListGaleriFoto> list = new ArrayList<>();
    RecyclerView recyclerView;
    GaleriFotoAdapter myAdapter;
    DatabaseReference dbRef, dbRef2, dbRef3;
    FirebaseDatabase db, mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;
    String namaAtasan;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_photo_atasan);
        ButterKnife.bind(this);

        //setup recyclerview
        recyclerView = findViewById(R.id.recyclerview_gallery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new GaleriFotoAdapter(GaleriFoto.this, list);
        recyclerView.setAdapter(myAdapter);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("fotoKerja");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("fotoKerja");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mUser.getUid();
        DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("Akun").child(uid);
        dbRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListDaftarAkun listDaftarAkun = snapshot.getValue(ListDaftarAkun.class);
                namaAtasan = listDaftarAkun.getNamaPanjang();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Query query = dbRef.orderByChild("namaAtasan").equalTo(namaAtasan);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ListGaleriFoto listGaleriFoto = dataSnapshot.getValue(ListGaleriFoto.class);
                                String imageUrl = listGaleriFoto.getImageURL();
                                list.add(listGaleriFoto);
                                Picasso.with(GaleriFoto.this)
                                        .load(imageUrl)
                                        .fit();
                            }
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
