package com.example.skripsi.model;

public class UploadPhoto {
    String judulFotoKerja,deskripsiFotoKerja, imageURL;

    public UploadPhoto(){

    }

    public UploadPhoto(String judulFotoKerja, String deskripsiFotoKerja, String imageURL){
        this.judulFotoKerja=judulFotoKerja;
        this.deskripsiFotoKerja=deskripsiFotoKerja;
        this.imageURL=imageURL;
    }

    public String getJudulFotoKerja() {
        return judulFotoKerja;
    }

    public void setJudulFotoKerja(String judulFotoKerja) {
        this.judulFotoKerja = judulFotoKerja;
    }

    public String getDeskripsiFotoKerja() {
        return deskripsiFotoKerja;
    }

    public void setDeskripsiFotoKerja(String deskripsiFotoKerja) {
        this.deskripsiFotoKerja = deskripsiFotoKerja;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
