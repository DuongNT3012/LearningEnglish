package com.example.projectse.hoctuvung;

import java.io.Serializable;

public class TuVung implements Serializable {
    private int idtu;
    private int idbo;
    private String dapan;
    private String dichnghia;
    private String loaitu;
    private String audio;
    private int anh;

    public TuVung(int idtu, int idbo, String dapan, String dichnghia, String loaitu, String audio, int anh) {
        this.idtu = idtu;
        this.idbo = idbo;
        this.dapan = dapan;
        this.dichnghia = dichnghia;
        this.loaitu = loaitu;
        this.audio = audio;
        this.anh = anh;
    }

    public int getIdtu() {
        return idtu;
    }

    public void setIdtu(int idtu) {
        this.idtu = idtu;
    }

    public int getIdbo() {
        return idbo;
    }

    public void setIdbo(int idbo) {
        this.idbo = idbo;
    }

    public String getDapan() {
        return dapan;
    }

    public void setDapan(String dapan) {
        this.dapan = dapan;
    }

    public String getDichnghia() {
        return dichnghia;
    }

    public void setDichnghia(String dichnghia) {
        this.dichnghia = dichnghia;
    }

    public String getLoaitu() {
        return loaitu;
    }

    public void setLoaitu(String loaitu) {
        this.loaitu = loaitu;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getAnh() {
        return anh;
    }

    public void setAnh(int anh) {
        this.anh = anh;
    }
}
