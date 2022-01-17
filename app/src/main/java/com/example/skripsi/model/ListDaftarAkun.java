package com.example.skripsi.model;

public class ListDaftarAkun {
    private String nip, namaPanjang, email, noTelp, password, namaAtasan, role, uId;

    public ListDaftarAkun(){

    }


    public ListDaftarAkun(String nip, String namaPanjang, String email, String noTelp, String password, String role, String uId){
        this.nip=nip;
        this.namaPanjang=namaPanjang;
        this.email=email;
        this.noTelp=noTelp;
        this.password=password;
        this.role=role;
        this.uId=uId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public ListDaftarAkun (String nip, String namaPanjang, String email, String noTelp, String password, String role, String uId, String namaAtasan){
        this.nip=nip;
        this.namaPanjang=namaPanjang;
        this.email=email;
        this.noTelp=noTelp;
        this.password=password;
        this.role=role;
        this.uId=uId;
        this.namaAtasan=namaAtasan;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNamaPanjang() {
        return namaPanjang;
    }

    public void setNamaPanjang(String namaPanjang) {
        this.namaPanjang = namaPanjang;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole(){ return role;}

    public void setRole(String role){ this.role = role;}

    public String getNamaAtasan() {
        return namaAtasan;
    }

    public void setNamaAtasan(String namaAtasan) {
        this.namaAtasan = namaAtasan;
    }
}

