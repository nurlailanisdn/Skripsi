package com.example.skripsi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListPermintaanAkun;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PermintaanAkun extends AppCompatActivity {
    @Nullable
    @BindView(R.id.radiogrp_acc)
    RadioButton radiouGrp_acc;
    @Nullable
    @BindView(R.id.radioBtn_acc)
    RadioButton radioBtn_acc;
    @Nullable
    @BindView(R.id.radioBtn_delete)
    RadioButton getRadioBtn_delete;

    RecyclerView recyclerView;
    private PermintaanAkunAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    DatabaseReference dbRef,dbRef2;
    ArrayList<ListPermintaanAkun> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_daftar_akun);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAktivasiAkun);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Mananage Permintaan Akun");

//        setup recyclerview
        recyclerView = findViewById(R.id.listdaftarAkun);
        dbRef = FirebaseDatabase.getInstance().getReference("Pre-Akun");
        dbRef2 = FirebaseDatabase.getInstance().getReference("Akun");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        recyclerView.setAdapter(myAdapter);
        myAdapter = new PermintaanAkunAdapter(list, new PermintaanAkunAdapter.IOnItemClickListener(){

            @Override
            public void onItemPermintaanAkunAdapterClickListener(boolean activate, ListPermintaanAkun listPermintaanAkun) {
                DatabaseReference dbDel = dbRef.child(listPermintaanAkun.getuId());
                if(activate){
                    Toast.makeText(PermintaanAkun.this, "Permintaan akun telah diproses. . .",Toast.LENGTH_SHORT).show();
                    dbRef2.child(listPermintaanAkun.getuId()).setValue(listPermintaanAkun);
                    dbDel.removeValue();
                    recreate();
                } else {
                    Toast.makeText(PermintaanAkun.this,"Permintaan akun telah dihapus. . .", Toast.LENGTH_SHORT).show();
                    dbDel.removeValue();
                    recreate();
                }
            }
        });

        recyclerView.setAdapter(myAdapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ListPermintaanAkun listPermintaanAkun = dataSnapshot.getValue(ListPermintaanAkun.class);
                    list.add(listPermintaanAkun);
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Permintaan Akun: ", error.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                signOut();
                break;
            case R.id.manage_sales:
                startActivity(new Intent(PermintaanAkun.this, PermintaanAkunSales.class));
                break;
            case R.id.manage_teknisi:
                startActivity(new Intent(PermintaanAkun.this, PermintaanAkunTeknisi.class));
                break;
            case R.id.manage_atasan:
                startActivity(new Intent(PermintaanAkun.this, PermintaanAkunAtasan.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(PermintaanAkun.this, LoginActivity.class));
        finish();
    }
}
