package com.example.skripsi.model;

import android.content.Context;
import android.net.Uri;

import java.io.Serializable;

public class ListGaleriFoto implements Serializable {
    String uIdUploader, judulFotoKerja, imageURL, namaPanjang, tanggalFotoKerja, deskripsiFotoKerja, namaAtasan, key;

    public ListGaleriFoto() {

    }

    public ListGaleriFoto(String tanggalFotoKerja, String namaPanjang, String judulFotoKerja, String imageURL, String deskripsiFotoKerja, String namaAtasan, String key, String uIdUploader) {
        this.tanggalFotoKerja = tanggalFotoKerja;
        this.namaPanjang = namaPanjang;
        this.judulFotoKerja = judulFotoKerja;
        this.imageURL = imageURL;
        this.deskripsiFotoKerja=deskripsiFotoKerja;
        this.namaAtasan=namaAtasan;
        this.key=key;
        this.uIdUploader=uIdUploader;
    }

    public String getuIdUploader() {
        return uIdUploader;
    }

    public void setuIdUploader(String uIdUploader) {
        this.uIdUploader = uIdUploader;
    }

    public String getJudulFotoKerja() {
        return judulFotoKerja;
    }

    public void setJudulFotoKerja(String judulFotoKerja) {
        this.judulFotoKerja = judulFotoKerja;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNamaPanjang() {
        return namaPanjang;
    }

    public void setNamaPanjang(String namaPanjang) {
        this.namaPanjang = namaPanjang;
    }

    public String getTanggalFotoKerja() {
        return tanggalFotoKerja;
    }

    public void setTanggalFotoKerja(String tanggalFotoKerja) {
        this.tanggalFotoKerja = tanggalFotoKerja;
    }

    public String getDeskripsiFotoKerja() {
        return deskripsiFotoKerja;
    }

    public void setDeskripsiFotoKerja(String deskripsiFotoKerja) {
        this.deskripsiFotoKerja = deskripsiFotoKerja;
    }

    public String getNamaAtasan() {
        return namaAtasan;
    }

    public void setNamaAtasan(String namaAtasan) {
        this.namaAtasan = namaAtasan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
