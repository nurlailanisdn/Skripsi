package com.example.skripsi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.skripsi.model.Profil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilActivity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @BindView(R.id.txt_namaProfil)
    TextView namaProfil;
    @BindView(R.id.txt_nipProfil)
    TextView nipProfil;
    @BindView(R.id.txt_roleProfil)
    TextView roleProfil;
    @BindView(R.id.txt_phoneProfil)
    TextView phoneProfil;
    @BindView(R.id.txt_tanggalProfil)
    TextView tanggalProfil;
    @BindView(R.id.txt_jamProfil)
    TextView jamProfil;
    private SharedPreferences preferences;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private double lat, longt;

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
    private GoogleMap googleMap;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_page);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        mDatabase = FirebaseDatabase.getInstance().getReference();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapProfil);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
        readDB();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (googleMap != null && location != null && !location.equals("")) {
            new GeocoderTask().execute((Runnable) location);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
    }
        protected void readDB(){
            preferences = getSharedPreferences("profil", Context.MODE_PRIVATE);
            String id = preferences.getString("id", "empty");

            mDatabase.child("listOnline").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String nama = snapshot.child("namaPanjang").getValue(String.class);
                        String nip = snapshot.child("nip").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);
                        String lastSeen = snapshot.child("lastSeen").getValue(String.class);
                        String phone= snapshot.child("noTelp").getValue(String.class);

                        Log.d("Profil", nama+""+nip+""+role+""+lastSeen+""+phone);

                        namaProfil.setText(nama);
                        nipProfil.setText(nip);
                        roleProfil.setText(role);
                        phoneProfil.setText(phone);
                        jamProfil.setText(lastSeen);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Profil", error.getMessage());
                }
            });
        }

        private void getCurrentLocation(){
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
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(currentLoc)
                                            .title("Bekerja dari sini"));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
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
                getCurrentLocation();
            }
        }
    }

    public void onResume(){
        super.onResume();
        if (checkPermission()){
            getCurrentLocation();
        }
    }
}
