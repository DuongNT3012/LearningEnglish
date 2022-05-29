package com.example.projectse.admin.luyennghe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.example.projectse.R;
import com.example.projectse.admin.dialog.DialogAddListening;
import com.example.projectse.admin.dialog.IonClickAddListening;
import com.example.projectse.admin.sapxepcau.ArrangeSentencesActivityAdmin;
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

public class ListeningActivityAdmin extends AppCompatActivity {
    private ArrayList<CauLuyenNghe> cauLuyenNghes;
    int idbo;
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton btnAdd;
    ListeningAdapterAdmin listeningAdapterAdmin;
    DialogAddListening dialogAddListening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_listening_admin);

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
        cauLuyenNghes = new ArrayList<>();
        AddArrayCLN();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddListening = new DialogAddListening(ListeningActivityAdmin.this, new IonClickAddListening() {
                    @Override
                    public void onClickAddListening(String ans, String imgPreview, String audio) {
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlAddListening, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AddArrayCLN();
                                dialogAddListening.dismiss();
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
                                param.put("imgPreview", imgPreview);
                                param.put("audio", audio);
                                param.put("idUnitCategory", String.valueOf(idbo));
                                return param;
                            }
                        };
                        requestQueue1.add(stringRequest1);
                    }
                });
                dialogAddListening.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void AddArrayCLN() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGetListening, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String answer = "";
                String imgPreview = "";
                String audio = "";
                int idUnitCategory = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    cauLuyenNghes.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        answer = jsonObject.getString("answer");
                        imgPreview = jsonObject.getString("imgPreview");
                        audio = jsonObject.getString("audio");
                        idUnitCategory = jsonObject.getInt("idUnitCategory");
                        cauLuyenNghes.add(new CauLuyenNghe(id, idUnitCategory, "A", "B", "C", "D", answer, imgPreview, audio));
                    }
                    listeningAdapterAdmin = new ListeningAdapterAdmin(cauLuyenNghes, new IonClickItemListening() {
                        @Override
                        public void onClickEdit(CauLuyenNghe cauLuyenNghe, String answer, String imgPreview, String audio) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlEditListening, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArrayCLN();
                                    Toast.makeText(ListeningActivityAdmin.this, "Edited", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(cauLuyenNghe.getIdbai()));
                                    param.put("answer", answer);
                                    param.put("imgPreview", imgPreview);
                                    param.put("audio", audio);
                                    param.put("idUnitCategory", String.valueOf(idbo));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }

                        @Override
                        public void onClickDelete(CauLuyenNghe cauLuyenNghe) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlDeleteListening, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArrayCLN();
                                    Toast.makeText(ListeningActivityAdmin.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(cauLuyenNghe.getIdbai()));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListeningActivityAdmin.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(listeningAdapterAdmin);
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
