package com.example.skripsi;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.Monitoring;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MonitoringTeknisi extends AppCompatActivity {
    DatabaseReference dbRef;
    ArrayList<Monitoring> list;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MonitoringAdapter myAdapter;
    FirebaseDatabase db;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String tanggalKerja;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring);

        //setup recyclerview
        recyclerView = findViewById(R.id.listMonitoringPekerjaan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        recyclerView.setAdapter(myAdapter);
        myAdapter = new MonitoringAdapter(list);
        recyclerView.setVisibility(View.VISIBLE);

        //setup toolbar
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarMonitoringTeknisi);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Monitoring Teknisi");

        //get calendar
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        tanggalKerja = dateFormat.format(calendar.getTime());

        readRecyclerView();
    }

    public void readRecyclerView() {
        dbRef = db.getReference().child("Monitoring").child(tanggalKerja);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Monitoring listMonitoring = dataSnapshot.getValue(Monitoring.class);
                    list.add(listMonitoring);
                    Log.d("Monitoring Success: ", String.valueOf(listMonitoring));
                }
                myAdapter.updateList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Monitoring error: ", error.getMessage());
            }
        });
    }
}
