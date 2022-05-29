package com.example.projectse.admin.dienkhuyet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.projectse.admin.dialog.DialogAddFillBlank;
import com.example.projectse.admin.dialog.IonClickAddFillBlank;
import com.example.projectse.admin.luyennghe.ListeningActivityAdmin;
import com.example.projectse.admin.ui.home.Database;
import com.example.projectse.ultils.Server;
import com.example.projectse.user.taikhoan.DatabaseAccess;
import com.example.projectse.user.taikhoan.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FillBlanksActivityAdmin extends AppCompatActivity {

    int idbo;
    ArrayList<CauDienKhuyet> cauDienKhuyets = new ArrayList<>();
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton btnAdd;
    FillBlankAdapterAdmin fillBlankAdapterAdmin;
    DialogAddFillBlank dialogAddFillBlank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fill_blanks_admin);

        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        btnAdd = findViewById(R.id.btn_add);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        idbo = intent.getIntExtra("BoDK", 0);

        AddArray();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddFillBlank = new DialogAddFillBlank(FillBlanksActivityAdmin.this, new IonClickAddFillBlank() {
                    @Override
                    public void onClickAddFillBlank(String ques, String ans, String suggest) {
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlAddFillBlank, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AddArray();
                                dialogAddFillBlank.dismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("question", ques);
                                param.put("answer", ans);
                                param.put("suggest", suggest);
                                param.put("idUnitCategory", String.valueOf(idbo));
                                return param;
                            }
                        };
                        requestQueue1.add(stringRequest1);
                    }
                });
                dialogAddFillBlank.show();
            }
        });
    }

    void AddArray(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGetFillBlank, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String question = "";
                String answer = "";
                String suggest = "";
                int idUnitCategory = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    cauDienKhuyets.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        question = jsonObject.getString("question");
                        answer = jsonObject.getString("answer");
                        suggest = jsonObject.getString("suggest");
                        idUnitCategory = jsonObject.getInt("idUnitCategory");
                        cauDienKhuyets.add(new CauDienKhuyet(id, idUnitCategory, question, answer, suggest));
                    }
                    fillBlankAdapterAdmin = new FillBlankAdapterAdmin(cauDienKhuyets, new IonClickItemFillBlank() {
                        @Override
                        public void onClickEdit(CauDienKhuyet cauDienKhuyet, String ques, String ans, String suggest) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlEditFillBlank, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArray();
                                    Toast.makeText(FillBlanksActivityAdmin.this, "Edited", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(cauDienKhuyet.getIdcau()));
                                    param.put("question", ques);
                                    param.put("answer", ans);
                                    param.put("suggest", suggest);
                                    param.put("idUnitCategory", String.valueOf(idbo));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }

                        @Override
                        public void onClickDelete(CauDienKhuyet cauDienKhuyet) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlDeleteFillBlank, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArray();
                                    Toast.makeText(FillBlanksActivityAdmin.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(cauDienKhuyet.getIdcau()));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(FillBlanksActivityAdmin.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(fillBlankAdapterAdmin);
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
                param.put("idUnitCategory", String.valueOf(idbo));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}