package com.prechat.prechat.Claslar;

public class GamerProfile {
    private String Sohbet, Rank, Cinsiyet, Tanim, KullaniciAd, KullaniciProfil,KullaniciId;

    public GamerProfile(String sohbet, String rank, String cinsiyet, String tanim, String kullaniciAd, String kullaniciProfil, String kullaniciId) {
        Sohbet = sohbet;
        Rank = rank;
        Cinsiyet = cinsiyet;
        Tanim = tanim;
        KullaniciAd = kullaniciAd;
        KullaniciProfil = kullaniciProfil;
        KullaniciId = kullaniciId;
    }

    public GamerProfile() {
    }

    public String getSohbet() {
        return Sohbet;
    }

    public void setSohbet(String sohbet) {
        Sohbet = sohbet;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getCinsiyet() {
        return Cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        Cinsiyet = cinsiyet;
    }

    public String getTanim() {
        return Tanim;
    }

    public void setTanim(String tanim) {
        Tanim = tanim;
    }

    public String getKullaniciAd() {
        return KullaniciAd;
    }

    public void setKullaniciAd(String kullaniciAd) {
        KullaniciAd = kullaniciAd;
    }

    public String getKullaniciProfil() {
        return KullaniciProfil;
    }

    public void setKullaniciProfil(String kullaniciProfil) {
        KullaniciProfil = kullaniciProfil;
    }

    public String getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        KullaniciId = kullaniciId;
    }
}
