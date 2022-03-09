package com.example.skripsi.model;

import android.graphics.Bitmap;

public class InputData {
    String kodeSales, nomorMobi, namaPelanggan, noTelpPelanggan, noTelpAlt, relasiPelanggan, alamatPelanggan, patokanAlamat, tanggalInstalasi;
    Bitmap fotoKTP;
    public InputData(){

    }

    public InputData(String kodeSales, String nomorMobi, String namaPelanggan, String noTelpPelanggan, String noTelpAlt, String relasiPelanggan, String alamatPelanggan, String patokanAlamat, String tanggalInstalasi, Bitmap fotoKTP){
        this.kodeSales=kodeSales;
        this.nomorMobi=nomorMobi;
        this.namaPelanggan=namaPelanggan;
        this.noTelpPelanggan=noTelpPelanggan;
        this.noTelpAlt=noTelpAlt;
        this.relasiPelanggan=relasiPelanggan;
        this.alamatPelanggan=alamatPelanggan;
        this.patokanAlamat=patokanAlamat;
        this.tanggalInstalasi=tanggalInstalasi;
        this.fotoKTP=fotoKTP;
    }

    public String getKodeSales() {
        return kodeSales;
    }

    public void setKodeSales(String kodeSales) {
        this.kodeSales = kodeSales;
    }

    public String getNomorMobi() {
        return nomorMobi;
    }

    public void setNomorMobi(String nomorMobi) {
        this.nomorMobi = nomorMobi;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getNoTelpPelanggan() {
        return noTelpPelanggan;
    }

    public void setNoTelpPelanggan(String noTelpPelanggan) {
        this.noTelpPelanggan = noTelpPelanggan;
    }

    public String getNoTelpAlt() {
        return noTelpAlt;
    }

    public void setNoTelpAlt(String noTelpAlt) {
        this.noTelpAlt = noTelpAlt;
    }

    public String getRelasiPelanggan() {
        return relasiPelanggan;
    }

    public void setRelasiPelanggan(String relasiPelanggan) {
        this.relasiPelanggan = relasiPelanggan;
    }

    public String getAlamatPelanggan() {
        return alamatPelanggan;
    }

    public void setAlamatPelanggan(String alamatPelanggan) {
        this.alamatPelanggan = alamatPelanggan;
    }

    public String getPatokanAlamat() {
        return patokanAlamat;
    }

    public void setPatokanAlamat(String patokanAlamat) {
        this.patokanAlamat = patokanAlamat;
    }

    public String getTanggalInstalasi() {
        return tanggalInstalasi;
    }

    public void setTanggalInstalasi(String tanggalInstalasi) {
        this.tanggalInstalasi = tanggalInstalasi;
    }

    public Bitmap getFotoKTP() {
        return fotoKTP;
    }

    public void setFotoKTP(Bitmap fotoKTP) {
        this.fotoKTP = fotoKTP;
    }
}
