package com.example.skripsi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    ArrayList<Monitoring> list = new ArrayList<>();
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
        recyclerView=findViewById(R.id.listMonitoring);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MonitoringAdapter(list);
        recyclerView.setAdapter(myAdapter);

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMonitoringTeknisi);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Monitoring Teknisi");

        //get calendar
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        tanggalKerja = dateFormat.format(calendar.getTime());

        readRecyclerView();
    }

    public void readRecyclerView() {
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("Monitoring").child(tanggalKerja);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Monitoring listMonitoring = dataSnapshot.getValue(Monitoring.class);
                    list.add(listMonitoring);
                    Log.d("Monitoring Success: ", String.valueOf(listMonitoring));
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Monitoring error: ", error.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.onwork_monitoring, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.back_home:
                startActivity(new Intent(MonitoringTeknisi.this, TeknisiActivity.class));;
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
