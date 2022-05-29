package com.example.projectse.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectse.admin.MainActivityAdmin;
import com.example.projectse.user.MainActivity;
import com.example.projectse.R;
import com.example.projectse.databinding.ActivityDangNhapBinding;
import com.example.projectse.ultils.Server;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DangNhap extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ActivityDangNhapBinding binding;
    ArrayList<Account> listAccount;
    static boolean checkDangNhap = false;
    //Current Account
    public static Account currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dang_nhap);
        //binding.edtUsername.setText("NguyenThaiDuong");
        binding.edtUsername.setText("Admin");
        binding.edtPassword.setText("123456789");
        CatchOnButtonQuayLai();
        CatchOnButtonDangNhap();
        CatchOnLLChuaCoTaiKhoan();
        CatchOnButtonDangKi();
        SetDefaultLayout();
        SetVisiblePassword();
    }

    private void SaveAccountByShareReference(Account currentAccount) {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentAccount);
        prefsEditor.putString("currentAccount", json);
        prefsEditor.commit();
    }

    @Override
    protected void onRestart() {
        currentAccount = null;
        super.onRestart();
    }

    private void CatchOnLLChuaCoTaiKhoan() {
        binding.llChuaCoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvDangNhap.setText("Create an account");
                binding.btnDangNhap.setVisibility(View.GONE);
                binding.edtHoTen.setVisibility(View.VISIBLE);
                binding.edtSDT.setVisibility(View.VISIBLE);
                binding.edtEmail.setVisibility(View.VISIBLE);
                binding.edtDiaChi.setVisibility(View.VISIBLE);
                binding.btnDangKi.setVisibility(View.VISIBLE);
                binding.llChuaCoTaiKhoan.setVisibility(View.GONE);
            }
        });
    }

    private void SetVisiblePassword() {
        binding.edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtPassword.getRight() - binding.edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (binding.edtPassword.getInputType() == InputType.TYPE_CLASS_TEXT) {
                            binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vpn_key_black_24dp, 0, R.drawable.ic_visibility_black_24dp, 0);
                        } else {
                            binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                            binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vpn_key_black_24dp, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void CatchOnButtonDangKi() {
        binding.btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate fields
                if (binding.edtUsername.getText().toString().trim().length() > 0 && binding.edtPassword.getText().toString().trim().length() > 0 && binding.edtSDT.getText().toString().trim().length() > 0 && binding.edtEmail.getText().toString().trim().length() > 0) {
                    if (binding.edtUsername.getText().toString().trim().length() < 6 || binding.edtPassword.getText().toString().trim().length() < 6) {
                        CheckConnection.ShowToast_Short(getApplicationContext(), "Độ dài Username và Password phải > 6!");
                        return;
                    }
                    if (binding.edtSDT.getText().toString().trim().length() < 10) {
                        CheckConnection.ShowToast_Short(getApplicationContext(), "Độ dài Số Điện Thoại phải >= 10!");
                        return;
                    }
                    if (binding.edtEmail.getText().toString().trim().length() < 11) {
                        CheckConnection.ShowToast_Short(getApplicationContext(), "Độ dài Email phải >= 11!");
                        return;
                    }
                }
                if (binding.edtUsername.getText().toString().trim().isEmpty() || binding.edtPassword.getText().toString().trim().isEmpty() || binding.edtHoTen.getText().toString().trim().isEmpty() || binding.edtSDT.getText().toString().trim().isEmpty() || binding.edtEmail.getText().toString().trim().isEmpty() || binding.edtDiaChi.getText().toString().trim().isEmpty()) {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Hãy nhập đủ thông tin các trường!");
                    return;
                } else {
                    listAccount = new ArrayList<>();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlAccount, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            int idAccount = 0;
                            String usernameGet = "";
                            String passwordGet = "";
                            String hoTenGet = "";
                            String soDienThoaiGet = "";
                            String emailGet = "";
                            String diaChiGet = "";
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    idAccount = jsonObject.getInt("idAccount");
                                    usernameGet = jsonObject.getString("username");
                                    passwordGet = jsonObject.getString("password");
                                    hoTenGet = jsonObject.getString("hoTen");
                                    soDienThoaiGet = jsonObject.getString("soDienThoai");
                                    emailGet = jsonObject.getString("email");
                                    diaChiGet = jsonObject.getString("diaChi");
                                    listAccount.add(new Account(idAccount, usernameGet, passwordGet, hoTenGet, soDienThoaiGet, emailGet, diaChiGet));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            final String username = binding.edtUsername.getText().toString().trim();
                            final String password = binding.edtPassword.getText().toString().trim();
                            final String hoTen = binding.edtHoTen.getText().toString().trim();
                            final String soDienThoai = binding.edtSDT.getText().toString().trim();
                            final String email = binding.edtEmail.getText().toString().trim();
                            final String diaChi = binding.edtDiaChi.getText().toString().trim();
                            boolean existsAccount = false;
                            for (int i = 0; i < listAccount.size(); i++) {
                                if (username.equals(listAccount.get(i).getUsername())) {
                                    CheckConnection.ShowToast_Short(getApplicationContext(), "Tài khoản đã tồn tại!");
                                    existsAccount = true;
                                }
                            }
                            if (existsAccount == false) {
                                RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlDangKiTaiKhoan, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response != null) {
                                            CheckConnection.ShowToast_Short(getApplicationContext(), "Đăng kí tài khoản thành công. Vui lòng đăng nhập để mua hàng!");
                                            binding.edtHoTen.setText("");
                                            binding.edtSDT.setText("");
                                            binding.edtEmail.setText("");
                                            binding.edtDiaChi.setText("");
                                            binding.edtHoTen.setVisibility(View.GONE);
                                            binding.edtSDT.setVisibility(View.GONE);
                                            binding.edtEmail.setVisibility(View.GONE);
                                            binding.edtDiaChi.setVisibility(View.GONE);
                                            binding.btnDangNhap.setVisibility(View.VISIBLE);
                                            binding.btnDangKi.setVisibility(View.GONE);
                                            binding.tvDangNhap.setText("Login");
                                        } else {
                                            CheckConnection.ShowToast_Short(getApplicationContext(), "Đăng kí thất bại!" + response);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String, String> param = new HashMap<>();
                                        param.put("username", username);
                                        param.put("password", password);
                                        param.put("hoTen", hoTen);
                                        param.put("soDienThoai", soDienThoai);
                                        param.put("email", email);
                                        param.put("diaChi", diaChi);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                        String ngayDangKy = simpleDateFormat.format(Calendar.getInstance().getTime());
                                        param.put("ngayDangKy", ngayDangKy);
                                        param.put("idLoaiTaiKhoan", String.valueOf(2));
                                        return param;
                                    }
                                };
                                requestQueue1.add(stringRequest1);
                            } else {
                                CheckConnection.ShowToast_Short(getApplicationContext(), "Tài khoản đã tồn tại!");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    requestQueue.add(jsonArrayRequest);
                }
            }
        });
    }

    private void SetDefaultLayout() {
        binding.edtHoTen.setVisibility(View.GONE);
        binding.edtSDT.setVisibility(View.GONE);
        binding.edtEmail.setVisibility(View.GONE);
        binding.edtDiaChi.setVisibility(View.GONE);
        binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void CatchOnButtonDangNhap() {
        binding.btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(DangNhap.this, "Login", "Wait a second...");
                progressDialog.show();
                listAccount = new ArrayList<>();
                final String username = binding.edtUsername.getText().toString().trim();
                final String password = binding.edtPassword.getText().toString().trim();
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                //Get all account
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlAccount, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int idAccount = 0;
                        String usernameGet = "";
                        String passwordGet = "";
                        String hoTen = "";
                        String soDienThoai = "";
                        String email = "";
                        String diaChi = "";
                        String ngayDangKy = "";
                        String loaiTaiKhoan = "";
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                idAccount = jsonObject.getInt("idAccount");
                                usernameGet = jsonObject.getString("username");
                                passwordGet = jsonObject.getString("password");
                                hoTen = jsonObject.getString("hoTen");
                                soDienThoai = jsonObject.getString("soDienThoai");
                                email = jsonObject.getString("email");
                                diaChi = jsonObject.getString("diaChi");
                                ngayDangKy = jsonObject.getString("ngayDangKy");
                                loaiTaiKhoan = jsonObject.getString("loaiTaiKhoan");
                                listAccount.add(new Account(idAccount, usernameGet, passwordGet, hoTen, soDienThoai, email, diaChi, ngayDangKy, loaiTaiKhoan));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        int demChuaCoTaiKhoan = 0;
                        for (int i = 0; i < listAccount.size(); i++) {
                            if (!listAccount.get(i).getUsername().equals(username)) {
                                demChuaCoTaiKhoan++;
                            }
                            if (listAccount.get(i).getUsername().equals(username) && !listAccount.get(i).getPassword().equals(password)) {
                                CheckConnection.ShowToast_Short(getApplicationContext(), "Sai mật khẩu, vui lòng nhập lại!");
                                progressDialog.dismiss();
                            }
                            if (listAccount.get(i).getUsername().equals(username) && listAccount.get(i).getPassword().equals(password)) {
                                if (listAccount.get(i).getLoaiTaiKhoan().equals("Admin")) {
                                    CheckConnection.ShowToast_Short(getApplicationContext(), "Đăng nhập thành công. Bạn đang đăng nhập với quyền Admin!");
                                    currentAccount = listAccount.get(i);
                                    checkDangNhap = true;
                                    Intent intent = new Intent(DangNhap.this, MainActivityAdmin.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                } else if (listAccount.get(i).getLoaiTaiKhoan().equals("Staff")) {
                                    CheckConnection.ShowToast_Short(getApplicationContext(), "You are a staff");
                                    /*Intent intent = new Intent(DangNhap.this, Staff.class);
                                    intent.putExtra("idAccount", listAccount.get(i).getIdAccount());
                                    startActivity(intent);*/
                                    progressDialog.dismiss();
                                } else {
                                    CheckConnection.ShowToast_Short(getApplicationContext(), "Đăng nhập thành công, học thôi nào!");
                                    currentAccount = listAccount.get(i);
                                    checkDangNhap = true;
                                    startActivity(new Intent(DangNhap.this, MainActivity.class));
                                    finish();
                                    progressDialog.dismiss();
                                }
                            }
                        }
                        if (demChuaCoTaiKhoan == listAccount.size()) {
                            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn chưa có tài khoản, vui lòng nhập thông tin đăng kí!");
                            binding.tvDangNhap.setText("Create an account");
                            binding.edtHoTen.setVisibility(View.VISIBLE);
                            binding.edtSDT.setVisibility(View.VISIBLE);
                            binding.edtEmail.setVisibility(View.VISIBLE);
                            binding.edtDiaChi.setVisibility(View.VISIBLE);
                            binding.btnDangNhap.setVisibility(View.GONE);
                            binding.btnDangKi.setVisibility(View.VISIBLE);
                            binding.llChuaCoTaiKhoan.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "onErrorResponse: " + error.toString());
                    }
                });
                requestQueue.add(jsonArrayRequest);
            }
        });
    }

    private void CatchOnButtonQuayLai() {
        binding.btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.tvDangNhap.getText().equals("Welcome")) {
                    finish();
                } else {
                    binding.tvDangNhap.setText("Welcome");
                    binding.edtHoTen.setVisibility(View.GONE);
                    binding.edtSDT.setVisibility(View.GONE);
                    binding.edtEmail.setVisibility(View.GONE);
                    binding.edtDiaChi.setVisibility(View.GONE);
                    binding.btnDangNhap.setVisibility(View.VISIBLE);
                    binding.btnDangKi.setVisibility(View.GONE);
                    binding.llChuaCoTaiKhoan.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
