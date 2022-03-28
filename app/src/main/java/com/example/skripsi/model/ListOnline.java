package com.example.skripsi.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ListOnline implements Serializable {
    private String namaPanjang, nip, role, lastSeen, noTelp, namaAtasan, tanggalLastSeen;
    double lat,lng;

    public ListOnline(){

    }

    public ListOnline(String namaPanjang, String nip, String role, String lastSeen, String tanggalLastSeen, String noTelp, double lat, double lng) {
        this.namaPanjang = namaPanjang;
        this.nip = nip;
        this.role = role;
        this.lastSeen = lastSeen;
        this.tanggalLastSeen=tanggalLastSeen;
        this.noTelp=noTelp;
        this.lat=lat;
        this.lng=lng;
    }

    public ListOnline(String namaPanjang, String nip, String role, String lastSeen, String namaAtasan, String tanggalLastSeen, String noTelp,double lat,double lng){
        this.namaPanjang=namaPanjang;
        this.nip=nip;
        this.role=role;
        this.lastSeen=lastSeen;
        this.namaAtasan=namaAtasan;
        this.tanggalLastSeen=tanggalLastSeen;
        this.noTelp=noTelp;
        this.lat=lat;
        this.lng=lng;
    }

    public String getTanggalLastSeen() {
        return tanggalLastSeen;
    }

    public void setTanggalLastSeen(String tanggalLastSeen) {
        this.tanggalLastSeen = tanggalLastSeen;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getNamaAtasan() {
        return namaAtasan;
    }

    public void setNamaAtasan(String namaAtasan) {
        this.namaAtasan = namaAtasan;
    }

    public String getNamaPanjang() {
        return namaPanjang;
    }

    public void setNamaPanjang(String namaPanjang) {
        this.namaPanjang = namaPanjang;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("NIP", nip);
        result.put("Nama", namaPanjang);
        result.put("Role", role);
        result.put("Last Seen", lastSeen);
        return result;
    }
}

