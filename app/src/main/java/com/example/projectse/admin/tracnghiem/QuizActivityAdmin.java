package com.example.projectse.admin.tracnghiem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.projectse.admin.dialog.DialogAddMultipleChoice;
import com.example.projectse.admin.dialog.IonClickAddMultipleChoice;
import com.example.projectse.user.taikhoan.DatabaseAccess;
import com.example.projectse.user.taikhoan.User;
import com.example.projectse.admin.ui.home.Database;
import com.example.projectse.ultils.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizActivityAdmin extends AppCompatActivity {

    private ArrayList<CauTracNghiem> cauTracNghiems;
    int idbo;

    RecyclerView recyclerView;
    QuizAdapterAdmin quizAdapterAdmin;
    Toolbar toolbar;
    FloatingActionButton btnAdd;
    DialogAddMultipleChoice dialogAddMultipleChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_admin);

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
        idbo = intent.getIntExtra("Bo", 0);
        cauTracNghiems = new ArrayList<>();
        AddArrayCTN();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddMultipleChoice = new DialogAddMultipleChoice(QuizActivityAdmin.this, new IonClickAddMultipleChoice() {
                    @Override
                    public void onClickAddMultipleChoice(String ques, String a, String b, String c, String d, String ans) {
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlAddMultipleChoice, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AddArrayCTN();
                                dialogAddMultipleChoice.dismiss();
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
                                param.put("a", a);
                                param.put("b", b);
                                param.put("c", c);
                                param.put("d", d);
                                param.put("answer", ans);
                                param.put("idUnitCategory", String.valueOf(idbo));
                                return param;
                            }
                        };
                        requestQueue1.add(stringRequest1);
                    }
                });
                dialogAddMultipleChoice.show();
            }
        });
    }

    private void AddArrayCTN() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGetMultipleChoice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String question = "";
                String a = "";
                String b = "";
                String c = "";
                String d = "";
                String answer = "";
                int idUnitCategory = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    cauTracNghiems.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        question = jsonObject.getString("question");
                        a = jsonObject.getString("a");
                        b = jsonObject.getString("b");
                        c = jsonObject.getString("c");
                        d = jsonObject.getString("d");
                        answer = jsonObject.getString("answer");
                        idUnitCategory = jsonObject.getInt("idUnitCategory");
                        cauTracNghiems.add(new CauTracNghiem(id, idUnitCategory, question, a, b, c, d, answer));
                    }
                    quizAdapterAdmin = new QuizAdapterAdmin(cauTracNghiems, new IonClickItemMultipleChoice() {
                        @Override
                        public void onClickEdit(CauTracNghiem cauTracNghiem, String ques, String a, String b, String c, String d, String ans) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlEditMultipleChoice, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArrayCTN();
                                    Toast.makeText(QuizActivityAdmin.this, "Edited", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(cauTracNghiem.getIdcau()));
                                    param.put("question", ques);
                                    param.put("a", a);
                                    param.put("b", b);
                                    param.put("c", c);
                                    param.put("d", d);
                                    param.put("answer", ans);
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }

                        @Override
                        public void onClickDelete(CauTracNghiem cauTracNghiem) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlDeleteMultipleChoice, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArrayCTN();
                                    Toast.makeText(QuizActivityAdmin.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(cauTracNghiem.getIdcau()));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(QuizActivityAdmin.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(quizAdapterAdmin);
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