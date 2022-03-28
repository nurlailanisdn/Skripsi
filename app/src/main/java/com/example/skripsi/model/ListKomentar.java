package com.example.skripsi.model;

public class ListKomentar {

    String namaPanjang, komentar, komentarKey, uId;

    public ListKomentar(){

    }

    public ListKomentar(String namaPanjang, String komentar, String komentarKey, String uId){
        this.namaPanjang=namaPanjang;
        this.komentar=komentar;
        this.komentarKey=komentarKey;
        this.uId=uId;
    }

    public String getNamaPanjang() {
        return namaPanjang;
    }

    public void setNamaPanjang(String namaPanjang) {
        this.namaPanjang = namaPanjang;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getKomentarKey() {
        return komentarKey;
    }

    public void setKomentarKey(String komentarKey) {
        this.komentarKey = komentarKey;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}

