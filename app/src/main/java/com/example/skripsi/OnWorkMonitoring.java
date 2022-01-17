package com.example.skripsi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.Monitoring;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.LongFunction;

import butterknife.BindView;

public class OnWorkMonitoring extends AppCompatActivity {
    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;
    @BindView(R.id.stopwatch)
    TextView txt_stopwatch;
    @BindView(R.id.tanggal_monitoring)
    TextView txt_tanggal;
    @BindView(R.id.jam_monitoring)
    TextView txt_jam;
    private Calendar calendar, newCalendar;
    private SimpleDateFormat dateFormat, timeFormat, timeFormatMulai;
    private String date,time, tanggalKerja, timeMulai , id;
    FirebaseDatabase db;
    FirebaseUser user;
    DatabaseReference dbRef;
    private DatabaseReference onWorkMonitoring;
    int hours, minutes, secs, jumlahWaktu;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    double lat, longt;
    ArrayList<Monitoring> list;
    RecyclerView recyclerView;
    private MonitoringAdapter myAdapter;
    private  RecyclerView.LayoutManager layoutManager;
    private final LocationCallback locationCallback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            lat = lastLocation.getLatitude();
            longt = lastLocation.getLongitude();
        }
    };
    private LocationListener locationListener;
    Location location;
    LatLng pos;
    private LocationManager locationManager;
    int PERMISSION_ID = 44;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_work);
        if( savedInstanceState!=null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();

        calendar=Calendar.getInstance();
        dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());
        timeFormat= new SimpleDateFormat("HH:mm:ss");
        time= timeFormat.format(calendar.getTime());
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        tanggalKerja = format.format(new Date());

        final TextView txt_tanggal = (TextView) findViewById(R.id.tanggal_monitoring);
        txt_tanggal.setText(date);
        final TextView txt_jam = (TextView) findViewById(R.id.jam_monitoring);
        txt_jam.setText(time);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();

        getLocation();
        readRecyclerView();

        //setup recyclerview
        recyclerView=findViewById(R.id.listMonitoringPekerjaan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        recyclerView.setAdapter(myAdapter);
        myAdapter = new MonitoringAdapter(list);
        recyclerView.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOnWork);
        setSupportActionBar(toolbar);
        toolbar.setTitle("On-Work");
    }

    private void getLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    lat = location.getLatitude();
                                    longt = location.getLongitude();
                                    LatLng currentLoc = new LatLng(lat, longt);
                                    Log.d("Location ", String.valueOf(longt)+String.valueOf(lat));
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Tolong nyalakan GPS anda", Toast.LENGTH_LONG).show();
                Intent enableGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(enableGPS);
            }
        } else {
            requestPermissions();
        }
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Toast.makeText(MainActivity.this, (CharSequence) addresses, Toast.LENGTH_SHORT).show();
            return addresses;
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){
        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private boolean checkPermission(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ID);
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    public void onSavedInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    protected void onPause(){
        super.onPause();
        wasRunning=running;
        running=false;
    }

    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
        if (checkPermission()) {
            getLocation();
        }
    }

    public void onClickStart(View view){
        timeFormatMulai= new SimpleDateFormat("HH:mm:ss");
        timeMulai= timeFormat.format(calendar.getTime());
        Log.d("Time mulai: ", timeMulai);
        running=true;
        id= user.getUid();
        onWorkMonitoring=db.getReference().child("Monitoring").child(tanggalKerja).child(timeMulai);
        DatabaseReference dbRead= db.getReference().child("Akun").child(id);
        dbRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Monitoring monitoring = snapshot.getValue(Monitoring.class);
                String namaPanjang= monitoring.getNamaPanjang();
                String nip= monitoring.getNip();
                String noTelp= monitoring.getNoTelp();
                Monitoring onWork = new Monitoring(namaPanjang,nip,noTelp, date, timeMulai);
                onWorkMonitoring.setValue(onWork);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OnWork Monitoring: ", error.getMessage());
            }
        });
    }

    public void onClickStop(View view){
        SimpleDateFormat timeFormatSelesai= new SimpleDateFormat("HH:mm:ss");
//        String timeSelesai= timeFormatSelesai.format(calendar.getTime());
        newCalendar=Calendar.getInstance();
        String timeSelesai = timeFormatSelesai.format(newCalendar.getTime());
        Log.d("Time Selesai: ", timeSelesai);
        running=false;
        onWorkMonitoring=db.getReference().child("Monitoring").child(tanggalKerja).child(timeMulai);
        onWorkMonitoring.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Monitoring monitoring = snapshot.getValue(Monitoring.class);
                    String namaPanjang= monitoring.getNamaPanjang();
                    String nip= monitoring.getNip();
                    String noTelp= monitoring.getNoTelp();
                    String waktuKerja = monitoring.getWaktuKerja();
                    Monitoring onWork = new Monitoring(namaPanjang, nip, noTelp, waktuKerja, tanggalKerja, timeMulai, timeSelesai, lat,  longt,  jumlahWaktu);
                    onWorkMonitoring.setValue(onWork);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickReset(View view){
        running=false;
        seconds=0;
        recreate();
    }

    private void runTimer(){
        final TextView stopwatch_txt = (TextView) findViewById(R.id.stopwatch);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                hours = seconds / 3600;
                minutes = (seconds%3600)/60;
                secs = seconds%60;

                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);
                stopwatch_txt.setText(time);

                if(running){
                    seconds++;
                    jumlahWaktu=seconds;
                }


                handler.postDelayed(this,1000);
            }
        });
    }

    public void readRecyclerView(){
        dbRef=db.getReference().child("Monitoring").child(tanggalKerja);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
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
                startActivity(new Intent(OnWorkMonitoring.this, TeknisiActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
