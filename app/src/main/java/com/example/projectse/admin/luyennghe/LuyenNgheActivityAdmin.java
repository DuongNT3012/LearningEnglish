package com.example.projectse.admin.luyennghe;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectse.R;
import com.example.projectse.admin.bohoctap.BoHocTapAdmin;
import com.example.projectse.admin.bohoctap.BoHocTapAdapterAdmin;
import com.example.projectse.admin.bohoctap.IonClickItemUnit;
import com.example.projectse.admin.dialog.DialogAddUnit;
import com.example.projectse.admin.dialog.IonClickDialogAddUnit;
import com.example.projectse.admin.hoctuvung.HocTuVungActivityAdmin;
import com.example.projectse.admin.tracnghiem.TracNghiemActivityAdmin;
import com.example.projectse.ultils.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LuyenNgheActivityAdmin extends AppCompatActivity {
    RecyclerView listView;
    ImageView imgback;
    ArrayList<BoHocTapAdmin> boCauHoiArrayList;
    BoHocTapAdapterAdmin boCauHoiAdapter;
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    int idbocauhoi;
    int idSubjectCategory = 0;
    FloatingActionButton btnAdd;
    DialogAddUnit dialogAddUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_luyen_nghe_admin);

        idSubjectCategory = getIntent().getIntExtra("idSubjectCategory", 0);

        listView = findViewById(R.id.lvluyennghe);
        imgback = findViewById(R.id.imgVBackLN);
        btnAdd = findViewById(R.id.btn_add);
        boCauHoiArrayList = new ArrayList<>();
        AddArrayBTN();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddUnit = new DialogAddUnit(LuyenNgheActivityAdmin.this, new IonClickDialogAddUnit() {
                    @Override
                    public void onClickDialogAddUnit(String name, String imgPreviewLink) {
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlAddUnit, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AddArrayBTN();
                                dialogAddUnit.dismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("name", name);
                                param.put("imgPreview", imgPreviewLink);
                                param.put("idSubjectCategory", String.valueOf(idSubjectCategory));
                                param.put("active", String.valueOf(0));
                                return param;
                            }
                        };
                        requestQueue1.add(stringRequest1);
                    }
                });
                dialogAddUnit.show();
            }
        });
    }

    private void AddArrayBTN() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGetUnitCategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String name = "";
                String imgPreview = "";
                int idSubjectCategory = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    boCauHoiArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        name = jsonObject.getString("name");
                        imgPreview = jsonObject.getString("imgPreview");
                        idSubjectCategory = jsonObject.getInt("idSubjectCategory");
                        boCauHoiArrayList.add(new BoHocTapAdmin(id, id, name));
                    }
                    boCauHoiAdapter = new BoHocTapAdapterAdmin(LuyenNgheActivityAdmin.this, R.layout.row_bo_admin, boCauHoiArrayList, new IonClickItemUnit() {
                        @Override
                        public void onClickItemUnit(int position) {
                            idbocauhoi = boCauHoiArrayList.get(position).getIdBo();
                            Intent quiz = new Intent(LuyenNgheActivityAdmin.this, ListeningActivityAdmin.class);
                            quiz.putExtra("Bo", idbocauhoi);
                            startActivity(quiz);
                        }

                        @Override
                        public void onClickDelete(int idUnitCategory) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlDeleteUnit, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArrayBTN();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(idUnitCategory));
                                    param.put("active", String.valueOf(1));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }
                    });
                    listView.setAdapter(boCauHoiAdapter);
                    listView.setLayoutManager(new LinearLayoutManager(LuyenNgheActivityAdmin.this, LinearLayoutManager.VERTICAL, false));
                    boCauHoiAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("idSubjectCategory", String.valueOf(4));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
