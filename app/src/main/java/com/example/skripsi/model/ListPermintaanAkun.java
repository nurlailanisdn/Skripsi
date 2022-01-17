package com.example.skripsi.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ListPermintaanAkun {
    private String namaPanjang, nip, role, noTelp, password, email, namaAtasan, uId;

    public ListPermintaanAkun(){

    }

    public ListPermintaanAkun(String namaPanjang, String nip, String role, String noTelp){
        this.namaPanjang=namaPanjang;
        this.nip=nip;
        this.role=role;
        this.noTelp=noTelp;
    }

    public ListPermintaanAkun(String namaPanjang, String nip, String role, String noTelp, String password, String email, String uId) {
        this.namaPanjang = namaPanjang;
        this.nip = nip;
        this.role = role;
        this.noTelp = noTelp;
        this.password=password;
        this.email=email;
        this.uId=uId;
    }

    public ListPermintaanAkun(String namaPanjang, String nip, String role, String noTelp, String password, String email, String namaAtasan, String uId){
        this.namaPanjang=namaPanjang;
        this.nip=nip;
        this.role=role;
        this.noTelp=noTelp;
        this.password=password;
        this.email=email;
        this.namaAtasan=namaAtasan;
        this.uId=uId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
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

    @Override
    public int hashCode() {
        return super.hashCode();
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

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password=password;}

    public String getEmail() {return email;}

    public void setEmail(String email) { this.email=email;}

    public String getNamaAtasan() {return namaAtasan;}

    public void setNamaAtasan(String namaAtasan) {this.namaAtasan=namaAtasan;}

    public String getuId() {return uId;}

    public void setuId(String uId) {this.uId=uId;}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("NIP", nip);
        result.put("Nama", namaPanjang);
        result.put("Role", role);
        result.put("Nomor Telepon", noTelp);
        return result;
    }
}
