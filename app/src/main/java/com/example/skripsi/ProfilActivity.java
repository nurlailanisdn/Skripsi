package com.example.skripsi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.skripsi.model.ListOnline;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    private GoogleMap googleMap;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_page);
        ButterKnife.bind(this); //Binding ButterKnife dengan activity ini
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        readIntent();
    }

    ListOnline listOnline;


    protected void readIntent() {
        if (getIntent().getExtras() != null) {
            listOnline = (ListOnline) getIntent().getSerializableExtra("profilData");
            String nama = listOnline.getNamaPanjang();
            String nip = listOnline.getNip();
            String role = listOnline.getRole();
            String phone = listOnline.getNoTelp();
            String jam = listOnline.getLastSeen();
            String tanggal = listOnline.getTanggalLastSeen();
            lat = listOnline.getLat();
            lng = listOnline.getLng();

            namaProfil.setText(nama);
            nipProfil.setText(nip);
            roleProfil.setText(role);
            phoneProfil.setText(phone);
            jamProfil.setText(jam);
            tanggalProfil.setText(tanggal);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
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
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title("Bekerja disini"));
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
    }
}