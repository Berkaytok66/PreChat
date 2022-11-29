package com.prechat.prechat.Claslar;

public class MesajIstegi {
    private String kanalID,kullaniciID,kullaniciName, kullaniciProfilPhoto;

    public MesajIstegi(String kanalID, String kullaniciID,String kullaniciName,String kullaniciProfilPhoto) {
        this.kanalID = kanalID;
        this.kullaniciID = kullaniciID;
        this.kullaniciName = kullaniciName;
        this.kullaniciProfilPhoto = kullaniciProfilPhoto;
    }

    public MesajIstegi() {
    }

    public String getKullaniciProfilPhoto() {
        return kullaniciProfilPhoto;
    }

    public void setKullaniciProfilPhoto(String kullaniciProfilPhoto) {
        this.kullaniciProfilPhoto = kullaniciProfilPhoto;
    }

    public String getKullaniciName() {
        return kullaniciName;
    }

    public void setKullaniciName(String kullaniciName) {
        this.kullaniciName = kullaniciName;
    }

    public String getKanalID() {
        return kanalID;
    }

    public void setKanalID(String kanalID) {
        this.kanalID = kanalID;
    }

    public String getKullaniciID() {
        return kullaniciID;
    }

    public void setKullaniciID(String kullaniciID) {
        this.kullaniciID = kullaniciID;
    }
}
