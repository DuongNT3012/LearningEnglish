package com.example.projectse.admin.hoctuvung;

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
import com.example.projectse.admin.bohoctap.BoHocTapAdmin;
import com.example.projectse.admin.bohoctap.BoHocTapAdapterAdmin;
import com.example.projectse.ultils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HocTuVungActivityAdmin extends AppCompatActivity {

    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgback;

    ArrayList<BoHocTapAdmin> boTuVungs;
    BoHocTapAdapterAdmin adapter;
    ListView botuvungs;

    int idbo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hoctuvung);
        botuvungs = findViewById(R.id.listviewHTV);
        imgback = findViewById(R.id.imgVBackHTV);
        boTuVungs = new ArrayList<>();
        AddArrayBTV();

        botuvungs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idbo = boTuVungs.get(position).getIdBo();
                Intent dstv = new Intent(HocTuVungActivityAdmin.this, DSTuVungActivityAdmin.class);
                dstv.putExtra("idbo", idbo);
                startActivity(dstv);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AddArrayBTV() {
        /*database = Database.initDatabase(HocTuVungActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi", null);
        boTuVungs.clear();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idbo = cursor.getInt(0);
            int stt = cursor.getInt(1);
            String tenbo = cursor.getString(2);
            boTuVungs.add(new BoHocTap(idbo, stt, tenbo));
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
                    boTuVungs.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        name = jsonObject.getString("name");
                        imgPreview = jsonObject.getString("imgPreview");
                        idSubjectCategory = jsonObject.getInt("idSubjectCategory");
                        boTuVungs.add(new BoHocTapAdmin(id, id, name));
                    }
                    adapter = new BoHocTapAdapterAdmin(HocTuVungActivityAdmin.this, R.layout.row_bo, boTuVungs);
                    botuvungs.setAdapter(adapter);
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
                param.put("idSubjectCategory", String.valueOf(1));
                return param;
            }
        };
        requestQueue.add(stringRequest);
        /*boTuVungs.clear();
        boTuVungs.add(new BoHocTap(1, 1, "Bộ học tập số 1"));
        boTuVungs.add(new BoHocTap(2, 2, "Bộ học tập số 2"));
        boTuVungs.add(new BoHocTap(3, 3, "Bộ học tập số 3"));
        boTuVungs.add(new BoHocTap(4, 4, "Bộ học tập số 4"));*/
    }
}