package com.example.projectse.admin.bohoctap;


import java.io.Serializable;

public class BoHocTapAdmin implements Serializable {
    private int idBo;
    private int stt;
    private String TenBo;
    private String imgPreview;

    public String getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(String imgPreview) {
        this.imgPreview = imgPreview;
    }

    public BoHocTapAdmin(int idBo, int stt, String tenBo, String imgPreview) {
        this.idBo = idBo;
        this.stt = stt;
        TenBo = tenBo;
        this.imgPreview = imgPreview;
    }

    public int getIdBo() {
        return idBo;
    }

    public void setIdBo(int idBo) {
        this.idBo = idBo;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getTenBo() {
        return TenBo;
    }

    public void setTenBo(String tenBo) {
        TenBo = tenBo;
    }
}
