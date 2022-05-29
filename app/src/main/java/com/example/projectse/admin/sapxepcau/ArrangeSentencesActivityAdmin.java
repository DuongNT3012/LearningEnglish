package com.example.projectse.admin.sapxepcau;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.projectse.admin.dialog.DialogAddArrangeSentence;
import com.example.projectse.admin.dialog.IonClickAddArrangeSentence;
import com.example.projectse.admin.tracnghiem.QuizActivityAdmin;
import com.example.projectse.admin.tracnghiem.QuizAdapterAdmin;
import com.example.projectse.ultils.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrangeSentencesActivityAdmin extends AppCompatActivity {

    ArrayList<CauSapXep> cauSapXeps;
    int idbo;
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton btnAdd;
    ArrangeAdapterAdmin arrangeAdapterAdmin;
    DialogAddArrangeSentence dialogAddArrangeSentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_arrange_sentences_admin);

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
        idbo = intent.getIntExtra("idbo", 0);
        cauSapXeps = new ArrayList<>();
        AddArraySXC();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddArrangeSentence = new DialogAddArrangeSentence(ArrangeSentencesActivityAdmin.this, new IonClickAddArrangeSentence() {
                    @Override
                    public void onClickAddArrangeSentence(String ans, String a, String b, String c, String d) {
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlAddArrangeSentence, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AddArraySXC();
                                dialogAddArrangeSentence.dismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("answer", ans);
                                param.put("part1", a);
                                param.put("part2", b);
                                param.put("part3", c);
                                param.put("part4", d);
                                param.put("idUnitCategory", String.valueOf(idbo));
                                return param;
                            }
                        };
                        requestQueue1.add(stringRequest1);
                    }
                });
                dialogAddArrangeSentence.show();
            }
        });
    }

    private void AddArraySXC() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGetArrangeSentence, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String answer = "";
                String a = "";
                String b = "";
                String c = "";
                String d = "";
                int idUnitCategory = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    cauSapXeps.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        answer = jsonObject.getString("answer");
                        a = jsonObject.getString("part1");
                        b = jsonObject.getString("part2");
                        c = jsonObject.getString("part3");
                        d = jsonObject.getString("part4");
                        idUnitCategory = jsonObject.getInt("idUnitCategory");
                        cauSapXeps.add(new CauSapXep(id, idUnitCategory, answer, a, b, c, d));
                    }
                    arrangeAdapterAdmin = new ArrangeAdapterAdmin(cauSapXeps, new IonClickItemArrange() {
                        @Override
                        public void onClickEdit(CauSapXep cauSapXep, String answer, String a, String b, String c, String d) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlEditArrangeSentence, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArraySXC();
                                    Toast.makeText(ArrangeSentencesActivityAdmin.this, "Edited", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(cauSapXep.getIdcau()));
                                    param.put("answer", answer);
                                    param.put("part1", a);
                                    param.put("part2", b);
                                    param.put("part3", c);
                                    param.put("part4", d);
                                    param.put("idUnitCategory", String.valueOf(idbo));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }

                        @Override
                        public void onClickDelete(CauSapXep cauSapXep) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlDeleteArrangeSentence, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArraySXC();
                                    Toast.makeText(ArrangeSentencesActivityAdmin.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(cauSapXep.getIdcau()));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(ArrangeSentencesActivityAdmin.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(arrangeAdapterAdmin);
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