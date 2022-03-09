package com.example.skripsi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListGaleriFoto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GaleriFoto extends AppCompatActivity {
    ArrayList<ListGaleriFoto> list = new ArrayList<>();
    RecyclerView recyclerView;
    GaleriFotoAdapter myAdapter;
    DatabaseReference dbRef;
    FirebaseStorage mStorage;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_photo);
        ButterKnife.bind(this);

        //setup recyclerview
        recyclerView= findViewById(R.id.recyclerview_gallery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new GaleriFotoAdapter(list);
        recyclerView.setAdapter(myAdapter);

        //setup toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGallery);
//        setSupportActionBar(toolbar);

        mStorage= FirebaseStorage.getInstance();
    }

    @OnClick(R.id.btn_add_photo_gallery)
    public void addPhoto(){
        startActivity(new Intent(GaleriFoto.this, UploadPhotoActivity.class));
    }

}
