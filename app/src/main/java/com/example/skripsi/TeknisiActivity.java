package com.example.skripsi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListOnline;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeknisiActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.cardViewListOnline)
    CardView cViewListOnline;

    ListOnlineAdapter myAdapter;
    RecyclerView recyclerView;
    private DatabaseReference userListRef, onlineStatus, connectedRef;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<ListOnline> list;
    FirebaseDatabase db;
    FirebaseUser user;
    private SharedPreferences preferences;
    String nama, nip, role, namaAtasan, tanggalLastSeen, noTelp;
    private FusedLocationProviderClient fusedLocationProviderClient;
    protected double lat, lng;
    Location currentLocation;
    int PERMISSION_ID = 44;


    private final LocationCallback locationCallback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            lat = lastLocation.getLatitude();
            lng = lastLocation.getLongitude();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_online);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarListOnline);
        setSupportActionBar(toolbar);
        toolbar.setTitle("List Online");

        //setup recyclerview
        recyclerView = findViewById(R.id.listOnline);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        list = new ArrayList<>();
        myAdapter = new ListOnlineAdapter(TeknisiActivity.this, list);

        //firebase
        db = FirebaseDatabase.getInstance();
        userListRef = db.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        readAndWrite();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setVisibility(View.VISIBLE);

        readDB();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (checkPermissions()) {
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
                                    lng = location.getLongitude();
                                    LatLng currentLoc = new LatLng(lat, lng);
                                }
                            }
                        });
            } else {
                Toast.makeText(TeknisiActivity.this, "Tolong nyalakan GPS anda", Toast.LENGTH_LONG).show();
                Intent enableGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(enableGPS);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getCurrentLocation();
        }
    }

    private void readAndWrite(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat tanggal=new SimpleDateFormat("dd/MM/yyyy");
        String tanggalLastSeen = tanggal.format(currentTime);
        String lastSeen = format.format(currentTime);
        String id= user.getUid();

        onlineStatus=db.getReference("listOnline/"+id);
        connectedRef= FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class)){
                    DatabaseReference dbRead= db.getReference().child("Akun").child(id);
                    dbRead.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                onlineStatus.onDisconnect().removeValue();//delete child from listOnline

                                ListOnline listOnline = snapshot.getValue(ListOnline.class);

                                nama= listOnline.getNamaPanjang();
                                nip=listOnline.getNip();
                                role= listOnline.getRole();
                                namaAtasan=listOnline.getNamaAtasan();
                                noTelp = listOnline.getNoTelp();
                                ListOnline listOnlineAkun = new ListOnline (nama, nip, role, lastSeen, namaAtasan,tanggalLastSeen, noTelp, lat, lng);
                                onlineStatus.setValue(listOnlineAkun);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("List Online: ", error.getMessage());
                        }
                    });
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e ("list online: ", error.getMessage());
            }
        });
    }

    public void readDB(){
        userListRef = db.getReference().child("listOnline");
        Query userListRef2 = userListRef.orderByChild("namaAtasan").equalTo(namaAtasan);
        userListRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ListOnline listOnline = dataSnapshot.getValue(ListOnline.class);
                    list.add(listOnline);
                }

                myAdapter .notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("List Online:", error.getMessage());
            }
        });
    }

    public void onStop(){
        super.onStop();
        onlineStatus.onDisconnect().removeValue();//delete child from listOnline
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.teknisi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.onWork_monitoring:
                startActivity(new Intent(TeknisiActivity.this, OnWorkMonitoring.class));
                break;
            case R.id.action_logout:
                onlineStatus.onDisconnect().removeValue();//delete child from listOnline
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(TeknisiActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.action_galeri:
                startActivity(new Intent(TeknisiActivity.this, GaleriFotoTeknisi.class));
                break;
            case R.id.edit_profil:
                startActivity(new Intent(TeknisiActivity.this, EditProfil.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
