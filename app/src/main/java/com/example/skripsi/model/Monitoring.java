package com.example.skripsi.model;

public class Monitoring {
    String namaPanjang, nip, noTelp, waktuKerja, tanggalKerja, mulaiKerja,selesaiKerja;
    double lat, longt;
    int jumlahWaktu;

    public Monitoring(){

    }
    public Monitoring(String namaPanjang, String nip, String noTelp, String waktuKerja, String tanggalKerja, String mulaiKerja, String selesaiKerja, double lat, double longt, int jumlahWaktu){
        this.namaPanjang=namaPanjang;
        this.nip=nip;
        this.noTelp=noTelp;
        this.lat=lat;
        this.longt=longt;
        this.jumlahWaktu=jumlahWaktu;
        this.tanggalKerja=tanggalKerja;
        this.waktuKerja=waktuKerja;
        this.mulaiKerja=mulaiKerja;
        this.selesaiKerja=selesaiKerja;
    }

    public Monitoring(String namaPanjang, String nip, String noTelp, String tanggalKerja, String mulaiKerja){
        this.namaPanjang=namaPanjang;
        this.nip=nip;
        this.noTelp=noTelp;
        this.tanggalKerja=tanggalKerja;
        this.mulaiKerja=mulaiKerja;
    }

    public Monitoring(String selesaiKerja, int jumlahWaktu, double lat, double longt){
        this.selesaiKerja=selesaiKerja;
        this.jumlahWaktu=jumlahWaktu;
        this.lat=lat;
        this.longt=longt;
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

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }

    public int getJumlahWaktu() {
        return jumlahWaktu;
    }

    public void setJumlahWaktu(int jumlahWaktu) {
        this.jumlahWaktu = jumlahWaktu;
    }

    public String getWaktuKerja() {
        return waktuKerja;
    }

    public void setWaktuKerja(String waktuKerja) {
        this.waktuKerja = waktuKerja;
    }

    public String getTanggalKerja() {
        return tanggalKerja;
    }

    public void setTanggalKerja(String tanggalKerja) {
        this.tanggalKerja = tanggalKerja;
    }

    public String getMulaiKerja() {
        return mulaiKerja;
    }

    public void setMulaiKerja(String mulaiKerja) {
        this.mulaiKerja = mulaiKerja;
    }

    public String getSelesaiKerja() {
        return selesaiKerja;
    }

    public void setSelesaiKerja(String selesaiKerja) {
        this.selesaiKerja = selesaiKerja;
    }




}
