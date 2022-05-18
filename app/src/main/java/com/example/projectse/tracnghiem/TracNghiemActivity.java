package com.example.projectse.tracnghiem;

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
        boHocTapAdapter = new BoHocTapAdapter(TracNghiemActivity.this, R.layout.row_botracnghiem, boHocTapArrayList);
        listView.setAdapter(boHocTapAdapter);
        boHocTapAdapter.notifyDataSetChanged();


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
        boHocTapArrayList.clear();
        boHocTapArrayList.add(new BoHocTap(1, 1, "Bộ học tập số 1"));
        boHocTapArrayList.add(new BoHocTap(2, 2, "Bộ học tập số 2"));
        boHocTapArrayList.add(new BoHocTap(3, 3, "Bộ học tập số 3"));
        boHocTapArrayList.add(new BoHocTap(4, 4, "Bộ học tập số 4"));
    }
}