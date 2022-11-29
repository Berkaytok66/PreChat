package com.prechat.prechat.Claslar;

public class Chat {
    private String mesajIcerigi,Gonderen,Alici,mesajTipi,docID;

    public Chat(String mesajIcerigi, String gonderen, String alici, String mesajTipi, String docID) {
        this.mesajIcerigi = mesajIcerigi;
        Gonderen = gonderen;
        Alici = alici;
        this.mesajTipi = mesajTipi;
        this.docID = docID;
    }

    public Chat() {
    }

    public String getMesajIcerigi() {
        return mesajIcerigi;
    }

    public void setMesajIcerigi(String mesajIcerigi) {
        this.mesajIcerigi = mesajIcerigi;
    }

    public String getGonderen() {
        return Gonderen;
    }

    public void setGonderen(String gonderen) {
        Gonderen = gonderen;
    }

    public String getAlici() {
        return Alici;
    }

    public void setAlici(String alici) {
        Alici = alici;
    }

    public String getMesajTipi() {
        return mesajTipi;
    }

    public void setMesajTipi(String mesajTipi) {
        this.mesajTipi = mesajTipi;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
}
