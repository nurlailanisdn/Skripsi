package com.example.skripsi.model;


import java.util.HashMap;
import java.util.Map;

public class ListOnline {
    private String namaPanjang, nip, role, lastSeen, noTelp;

    public ListOnline(){

    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp=noTelp;
    }

    public ListOnline(String namaPanjang, String nip, String role, String lastSeen) {
        this.namaPanjang = namaPanjang;
        this.nip = nip;
        this.role = role;
        this.lastSeen = lastSeen;
    }

    public ListOnline(String namaPanjang, String nip, String role,String lastSeen, String noTelp){
        this.namaPanjang=namaPanjang;
        this.nip=nip;
        this.role=role;
        this.lastSeen=lastSeen;
        this.noTelp=noTelp;
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

