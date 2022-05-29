package com.example.projectse.admin.sapxepcau;

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

public class SapXepCauActivityAdmin extends AppCompatActivity {

    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgback;


    ArrayList<BoHocTapAdmin> boTuVungs;
    BoHocTapAdapterAdmin adapter;
    RecyclerView botuvungs;

    int idbo;
    int idSubjectCategory = 0;
    FloatingActionButton btnAdd;
    DialogAddUnit dialogAddUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sapxepcau_admin);

        idSubjectCategory = getIntent().getIntExtra("idSubjectCategory", 0);

        botuvungs = findViewById(R.id.listviewSXC);
        imgback = findViewById(R.id.imgVBackSXC);
        btnAdd = findViewById(R.id.btn_add);
        boTuVungs = new ArrayList<>();
        AddArrayBTV();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddUnit = new DialogAddUnit(SapXepCauActivityAdmin.this, new IonClickDialogAddUnit() {
                    @Override
                    public void onClickDialogAddUnit(String name, String imgPreviewLink) {
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlAddUnit, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AddArrayBTV();
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
    private void AddArrayBTV(){
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
                    boTuVungs.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        name = jsonObject.getString("name");
                        imgPreview = jsonObject.getString("imgPreview");
                        idSubjectCategory = jsonObject.getInt("idSubjectCategory");
                        boTuVungs.add(new BoHocTapAdmin(id, id, name));
                    }
                    adapter = new BoHocTapAdapterAdmin(SapXepCauActivityAdmin.this, R.layout.row_bo_admin, boTuVungs, new IonClickItemUnit() {
                        @Override
                        public void onClickItemUnit(int position) {
                            idbo = boTuVungs.get(position).getIdBo();
                            Intent sxc = new Intent(SapXepCauActivityAdmin.this, ArrangeSentencesActivityAdmin.class);
                            sxc.putExtra("idbo", idbo);
                            startActivity(sxc);
                        }

                        @Override
                        public void onClickDelete(int idUnitCategory) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlDeleteUnit, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArrayBTV();
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
                    botuvungs.setAdapter(adapter);
                    botuvungs.setLayoutManager(new LinearLayoutManager(SapXepCauActivityAdmin.this, LinearLayoutManager.VERTICAL, false));
                    adapter.notifyDataSetChanged();
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
                param.put("idSubjectCategory", String.valueOf(3));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}