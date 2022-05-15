package com.example.projectse.hoctuvung;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectse.MainActivity;
import com.example.projectse.R;
import com.example.projectse.bohoctap.BoHocTap;
import com.example.projectse.bohoctap.BoHocTapAdapter;
import com.example.projectse.ui.home.Database;

import java.util.ArrayList;

public class HocTuVungActivity extends AppCompatActivity {

    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgback;

    ArrayList<BoHocTap> boTuVungs;
    BoHocTapAdapter adapter;
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
        adapter = new BoHocTapAdapter(HocTuVungActivity.this, R.layout.row_botuvung, boTuVungs);
        botuvungs.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        botuvungs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idbo = boTuVungs.get(position).getIdBo();
                Intent dstv = new Intent(HocTuVungActivity.this, DSTuVungActivity.class);
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
        boTuVungs.clear();
        boTuVungs.add(new BoHocTap(1, 1, "Bộ học tập số 1"));
        boTuVungs.add(new BoHocTap(2, 2, "Bộ học tập số 2"));
        boTuVungs.add(new BoHocTap(3, 3, "Bộ học tập số 3"));
        boTuVungs.add(new BoHocTap(4, 4, "Bộ học tập số 4"));
    }
}