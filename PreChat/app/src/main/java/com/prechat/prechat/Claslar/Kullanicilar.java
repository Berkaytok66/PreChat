package com.prechat.prechat.Claslar;

public class Kullanicilar {
    private String  KullaniciIsmi, KullaniciEmail, KullaniciId,KullaniciPassword,KullaniciProfil;

    public Kullanicilar(String kullaniciIsmi, String kullaniciEmail, String kullaniciId, String kullaniciPassword, String kullaniciProfil) {
        KullaniciIsmi = kullaniciIsmi;
        KullaniciEmail = kullaniciEmail;
        KullaniciId = kullaniciId;
        KullaniciPassword = kullaniciPassword;
        KullaniciProfil = kullaniciProfil;
    }

    public Kullanicilar() {
    }

    public String getKullaniciIsmi() {
        return KullaniciIsmi;
    }

    public void setKullaniciIsmi(String kullaniciIsmi) {
        KullaniciIsmi = kullaniciIsmi;
    }

    public String getKullaniciEmail() {
        return KullaniciEmail;
    }

    public void setKullaniciEmail(String kullaniciEmail) {
        KullaniciEmail = kullaniciEmail;
    }

    public String getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        KullaniciId = kullaniciId;
    }

    public String getKullaniciPassword() {
        return KullaniciPassword;
    }

    public void setKullaniciPassword(String kullaniciPassword) {
        KullaniciPassword = kullaniciPassword;
    }

    public String getKullaniciProfil() {
        return KullaniciProfil;
    }

    public void setKullaniciProfil(String kullaniciProfil) {
        KullaniciProfil = kullaniciProfil;
    }
}
