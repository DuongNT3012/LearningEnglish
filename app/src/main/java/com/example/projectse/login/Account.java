package com.example.projectse.login;

import java.io.Serializable;

public class Account implements Serializable {
    int idAccount;
    String username;
    String password;
    String hoTen;
    String soDienThoai;
    String email;
    String diaChi;
    String ngayDangKy;
    String loaiTaiKhoan;

    public Account(){

    }

    public Account(int idAccount, String username, String password, String hoTen, String soDienThoai, String email, String diaChi) {
        this.idAccount = idAccount;
        this.username = username;
        this.password = password;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }

    public Account(int idAccount, String username, String password, String hoTen, String soDienThoai, String email, String diaChi, String ngayDangKy, String loaiTaiKhoan) {
        this.idAccount = idAccount;
        this.username = username;
        this.password = password;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.ngayDangKy = ngayDangKy;
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(String ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public String getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(String loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }
}
