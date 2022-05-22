package com.example.projectse.user.tracnghiem;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectse.R;
import com.example.projectse.user.bohoctap.BoHocTap;
import com.example.projectse.user.bohoctap.BoHocTapAdapter;
import com.example.projectse.ultils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TracNghiemActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<BoHocTap> boHocTapArrayList;
    BoHocTapAdapter boHocTapAdapter;
    ImageView imgback;
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    int idbocauhoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trac_nghiem);

        listView = findViewById(R.id.lvtracnghiem);
        imgback = findViewById(R.id.imgVBackTN);
        boHocTapArrayList = new ArrayList<>();
        AddArrayBTN();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*database = Database.initDatabase(TracNghiemActivity.this, DATABASE_NAME);
                String a = null;
                Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi", null);
                for (int i = position; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    int idbo = cursor.getInt(0);
                    int stt = cursor.getInt(1);
                    String tenbo = cursor.getString(2);
                    a = tenbo;
                    idbocauhoi = idbo;
                    break;
                }*/
                idbocauhoi = boHocTapArrayList.get(position).getIdBo();
                Intent quiz = new Intent(TracNghiemActivity.this, QuizActivity.class);
                quiz.putExtra("Bo", idbocauhoi);

                startActivity(quiz);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AddArrayBTN() {
        /*database= Database.initDatabase(TracNghiemActivity.this,DATABASE_NAME);
        Cursor cursor=database.rawQuery("SELECT * FROM BoCauHoi",null);
        boHocTapArrayList.clear();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idbo = cursor.getInt(0);
            int  stt = cursor.getInt(1);
            String tenbo = cursor.getString(2);
            boHocTapArrayList.add(new BoHocTap(idbo,stt,tenbo));

        }*/
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
                    boHocTapArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        name = jsonObject.getString("name");
                        imgPreview = jsonObject.getString("imgPreview");
                        idSubjectCategory = jsonObject.getInt("idSubjectCategory");
                        boHocTapArrayList.add(new BoHocTap(id, id, name));
                    }
                    boHocTapAdapter = new BoHocTapAdapter(TracNghiemActivity.this, R.layout.row_bo, boHocTapArrayList);
                    listView.setAdapter(boHocTapAdapter);
                    boHocTapAdapter.notifyDataSetChanged();
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
                param.put("idSubjectCategory", String.valueOf(2));
                return param;
            }
        };
        requestQueue.add(stringRequest);
        /*boHocTapArrayList.clear();
        boHocTapArrayList.add(new BoHocTap(1, 1, "Bộ học tập số 1"));
        boHocTapArrayList.add(new BoHocTap(2, 2, "Bộ học tập số 2"));
        boHocTapArrayList.add(new BoHocTap(3, 3, "Bộ học tập số 3"));
        boHocTapArrayList.add(new BoHocTap(4, 4, "Bộ học tập số 4"));*/
    }
}