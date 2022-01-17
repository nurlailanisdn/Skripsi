package com.example.skripsi.model;

public class Profil {
    String nama, nip, role, phone, lastSeen;

    public Profil(){

    }

    public Profil(String nama, String nip, String role, String phone, String lastSeen){
        this.nama=nama;
        this.nip=nip;
        this.role=role;
        this.phone=phone;
        this.lastSeen=lastSeen;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }
}
