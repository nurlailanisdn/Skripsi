package com.example.skripsi.model;

import android.net.Uri;

public class ListGaleriFoto {
    Uri uri;
    String tanggalGaleri, uploaderGaleri, judulGaleri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getTanggalGaleri() {
        return tanggalGaleri;
    }

    public void setTanggalGaleri(String tanggalGaleri) {
        this.tanggalGaleri = tanggalGaleri;
    }

    public String getUploaderGaleri() {
        return uploaderGaleri;
    }

    public void setUploaderGaleri(String uploaderGaleri) {
        this.uploaderGaleri = uploaderGaleri;
    }

    public String getJudulGaleri() {
        return judulGaleri;
    }

    public void setJudulGaleri(String judulGaleri) {
        this.judulGaleri = judulGaleri;
    }
}
