package com.example.projectse.dienkhuyet;

import java.io.Serializable;

public class CauDienKhuyet implements Serializable {
    private int idcau;
    private int idbo;
    private String cauHoi;
    private String Dapan;
    private String goiY;

    public CauDienKhuyet(int idcau, int idbo, String cauHoi, String dapan, String goiY) {
        this.idcau = idcau;
        this.idbo = idbo;
        Dapan = dapan;
        this.cauHoi = cauHoi;
        this.goiY = goiY;
    }

    public String getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(String cauHoi) {
        this.cauHoi = cauHoi;
    }

    public String getGoiY() {
        return goiY;
    }

    public void setGoiY(String goiY) {
        this.goiY = goiY;
    }

    public int getIdcau() {
        return idcau;
    }

    public void setIdcau(int idcau) {
        this.idcau = idcau;
    }

    public int getIdbo() {
        return idbo;
    }

    public void setIdbo(int idbo) {
        this.idbo = idbo;
    }

    public String getDapan() {
        return Dapan;
    }

    public void setDapan(String dapan) {
        Dapan = dapan;
    }
}
