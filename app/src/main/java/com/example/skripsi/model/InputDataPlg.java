package com.example.skripsi.model;

public class InputDataPlg {
    String kodeSales,noMobi,namaPlg,noTelp,noTelpAlt,relasiPlg,alamatPlg, patokanAlmt,tanggalInstalasi,fotoKtp;

    public InputDataPlg(String kodeSales, String noMobi, String namaPlg, String noTelp, String noTelpAlt, String relasiPlg){
        this.kodeSales=kodeSales;
        this.noMobi=noMobi;
        this.namaPlg=namaPlg;
        this.noTelp=noTelp;
        this.noTelpAlt=noTelpAlt;
        this.relasiPlg=relasiPlg;
    }

    public InputDataPlg(String alamatPlg, String patokanAlmt, String tanggalInstalasi, String fotoKtp){
        this.alamatPlg=alamatPlg;
        this.patokanAlmt=patokanAlmt;
        this.tanggalInstalasi=tanggalInstalasi;
        this.fotoKtp=fotoKtp;
    }

    public String getKodeSales() {
        return kodeSales;
    }

    public void setKodeSales(String kodeSales) {
        this.kodeSales = kodeSales;
    }

    public String getNoMobi() {
        return noMobi;
    }

    public void setNoMobi(String noMobi) {
        this.noMobi = noMobi;
    }

    public String getNamaPlg() {
        return namaPlg;
    }

    public void setNamaPlg(String namaPlg) {
        this.namaPlg = namaPlg;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getNoTelpAlt() {
        return noTelpAlt;
    }

    public void setNoTelpAlt(String noTelpAlt) {
        this.noTelpAlt = noTelpAlt;
    }

    public String getRelasiPlg() {
        return relasiPlg;
    }

    public void setRelasiPlg(String relasiPlg) {
        this.relasiPlg = relasiPlg;
    }

    public String getAlamatPlg() {
        return alamatPlg;
    }

    public void setAlamatPlg(String alamatPlg) {
        this.alamatPlg = alamatPlg;
    }

    public String getPatokanAlmt() {
        return patokanAlmt;
    }

    public void setPatokanAlmt(String patokanAlmt) {
        this.patokanAlmt = patokanAlmt;
    }

    public String getTanggalInstalasi() {
        return tanggalInstalasi;
    }

    public void setTanggalInstalasi(String tanggalInstalasi) {
        this.tanggalInstalasi = tanggalInstalasi;
    }

    public String getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(String fotoKtp) {
        this.fotoKtp = fotoKtp;
    }
}
