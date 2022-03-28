package com.example.skripsi;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListGaleriFoto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import butterknife.OnClick;

public class GaleriFotoTeknisi extends AppCompatActivity {
    ArrayList<ListGaleriFoto> list = new ArrayList<>();
    RecyclerView recyclerView;
    GaleriFotoAdapter myAdapter;
    DatabaseReference dbRef;
    FirebaseDatabase db;
    FirebaseStorage mStorage;
    StorageReference storageRef;
    FirebaseUser mUser;
    String uId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_photo);
        ButterKnife.bind(this);

        //setup recyclerview
        recyclerView= findViewById(R.id.recyclerview_gallery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new GaleriFotoAdapter(GaleriFotoTeknisi.this,list);
        recyclerView.setAdapter(myAdapter);

        //setup toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGallery);
//        setSupportActionBar(toolbar);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uId= mUser.getUid();
        db = FirebaseDatabase.getInstance();
        mStorage= FirebaseStorage.getInstance();
        storageRef = mStorage.getReference().child("fotoKerja").child(uId);
        dbRef= db.getReference().child("fotoKerja");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Query query = dbRef.orderByChild("uIdUploader").equalTo(uId);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ListGaleriFoto listGaleriFoto = dataSnapshot.getValue(ListGaleriFoto.class);
                            list.add(listGaleriFoto);
                            String imageURL = listGaleriFoto.getImageURL();
                            Picasso.with(GaleriFotoTeknisi.this)
                                    .load(imageURL)
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
                Toast.makeText(GaleriFotoTeknisi.this,"Gagal mengunduh data!", Toast.LENGTH_LONG).show();
            }
        });


    }

    @OnClick(R.id.btn_add_photo_gallery)
    public void addPhoto(){
        startActivity(new Intent(GaleriFotoTeknisi.this, UploadPhotoActivity.class));
    }

}
