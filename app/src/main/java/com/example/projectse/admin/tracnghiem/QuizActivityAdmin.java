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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectse.R;
import com.example.projectse.user.taikhoan.DatabaseAccess;
import com.example.projectse.user.taikhoan.User;
import com.example.projectse.admin.ui.home.Database;
import com.example.projectse.ultils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizActivityAdmin extends AppCompatActivity {

    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    DatabaseAccess DB;
    TextView txtscore, txtquestcount, txtquestion, txttime;
    RadioGroup rdgchoices;
    RadioButton btnop1, btnop2, btnop3, btnop4;
    Button btnconfirm;
    Button btnquit;
    private ArrayList<CauTracNghiem> cauTracNghiems;
    int questioncurrent = 0;
    int questiontrue = 0;
    int answer = 0;
    int score = 0;
    int idbo;

    User user;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz);
        DB = DatabaseAccess.getInstance(getApplicationContext());
        Anhxa();

        LayUser();
        Intent intent = getIntent();
        idbo = intent.getIntExtra("Bo", 0);
        txttime.setText(" ");

        cauTracNghiems = new ArrayList<>();
        AddArrayCTN();

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkans();
                questioncurrent++;
                countDownTimer.start();
            }
        });

        btnquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void Anhxa() {
        txtscore = findViewById(R.id.txtscoreTN);
        txtquestcount = findViewById(R.id.txtquestcountTN);
        txtquestion = findViewById(R.id.txtquestionTN);
        txttime = findViewById(R.id.txttimeTN);
        rdgchoices = findViewById(R.id.radiochoices);
        btnop1 = findViewById(R.id.op1);
        btnop2 = findViewById(R.id.op2);
        btnop3 = findViewById(R.id.op3);
        btnop4 = findViewById(R.id.op4);
        btnconfirm = findViewById(R.id.btnconfirmTN);
        btnquit = findViewById(R.id.btnQuitTN);
    }

    private void AddArrayCTN() {
        /*database = Database.initDatabase(QuizActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM TracNghiem WHERE ID_Bo = ?", new String[]{String.valueOf(idbo)});
        cauTracNghiems.clear();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idcau = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String noidung = cursor.getString(2);
            String A = cursor.getString(3);
            String B = cursor.getString(4);
            String C = cursor.getString(5);
            String D = cursor.getString(6);
            String True = cursor.getString(7);

            cauTracNghiems.add(new CauTracNghiem(idcau, idbo, noidung, A, B, C, D, True));
        }*/

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
                    shownextquestion(questioncurrent, cauTracNghiems);

                    countDownTimer = new CountDownTimer(3000, 300) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            showanswer();
                        }

                        @Override
                        public void onFinish() {

                            btnconfirm.setEnabled(true);
                            shownextquestion(questioncurrent, cauTracNghiems);
                        }
                    };
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

        /*switch (idbo) {
            case 1:
                cauTracNghiems.add(new CauTracNghiem(1, idbo, "They are required to inform the human resources department when resigning due .......... a disagreement over company policy.", "to", "by", "on", "for", "1"));
                cauTracNghiems.add(new CauTracNghiem(2, idbo, "All the important files were organized first by color and .......... alphabetized by the title and name.", "since", "here", "then", "much", "3"));
                break;
            case 2:
                cauTracNghiems.add(new CauTracNghiem(1, idbo, "They are required to inform the human resources department when resigning due .......... a disagreement over company policy.", "to", "by", "on", "for", "1"));
                cauTracNghiems.add(new CauTracNghiem(2, idbo, "All the important files were organized first by color and .......... alphabetized by the title and name.", "since", "here", "then", "much", "3"));
                break;
            case 3:
                cauTracNghiems.add(new CauTracNghiem(1, idbo, "They are required to inform the human resources department when resigning due .......... a disagreement over company policy.", "to", "by", "on", "for", "1"));
                cauTracNghiems.add(new CauTracNghiem(2, idbo, "All the important files were organized first by color and .......... alphabetized by the title and name.", "since", "here", "then", "much", "3"));
                break;
            case 4:
                cauTracNghiems.add(new CauTracNghiem(1, idbo, "They are required to inform the human resources department when resigning due .......... a disagreement over company policy.", "to", "by", "on", "for", "1"));
                cauTracNghiems.add(new CauTracNghiem(2, idbo, "All the important files were organized first by color and .......... alphabetized by the title and name.", "since", "here", "then", "much", "3"));
                break;
        }*/
    }

    public void LayUser() {
        try {
            database = Database.initDatabase(QuizActivityAdmin.this, DATABASE_NAME);
            Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?", new String[]{String.valueOf(DB.iduser)});
            cursor.moveToNext();
            String Iduser = cursor.getString(0);
            String HoTen = cursor.getString(1);
            int Point = cursor.getInt(2);
            String Email = cursor.getString(3);
            String SDT = cursor.getString(4);
            user = new User(Iduser, HoTen, Point, Email, SDT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shownextquestion(int pos, ArrayList<CauTracNghiem> cauTracNghiems) {

//        database= Database.initDatabase(QuizActivity.this,DATABASE_NAME);
//        String a=null;
//        Cursor cursor=database.rawQuery("SELECT * FROM TracNghiem WHERE ID_Bo=?",new String[]{String.valueOf(idbo)});
//        txtquestcount.setText("Question: "+(questioncurrent+1)+"/"+cursor.getCount()+"");
        txtquestcount.setText("Question: " + (questioncurrent + 1) + "/" + cauTracNghiems.size() + "");
        rdgchoices.clearCheck();
        btnop1.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop2.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop3.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop4.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
//        if(pos==cursor.getCount()){
        try {
            if (pos == cauTracNghiems.size()) {
                //DB.capnhatdiem(DB.iduser, user.getPoint(), score);
                Intent intent = new Intent(QuizActivityAdmin.this, FinishQuizActivityAdmin.class);
                intent.putExtra("score", score);
                intent.putExtra("questiontrue", questiontrue);
                intent.putExtra("qcount", pos);
                startActivity(intent);
            } else {
                answer = Integer.valueOf(cauTracNghiems.get(pos).getDapanTrue());
                txtquestion.setText(cauTracNghiems.get(pos).getNoidung());
                btnop1.setText(cauTracNghiems.get(pos).getDapanA());
                btnop2.setText(cauTracNghiems.get(pos).getDapanB());
                btnop3.setText(cauTracNghiems.get(pos).getDapanC());
                btnop4.setText(cauTracNghiems.get(pos).getDapanD());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for(int i=pos;i<cursor.getCount();i++){
//            cursor.moveToPosition(i);
//            String questcontent=cursor.getString(2);
//            String op1=cursor.getString(3);
//            String op2=cursor.getString(4);
//            String op3=cursor.getString(5);
//            String op4=cursor.getString(6);
//            String ans=cursor.getString(7);
//            answer=Integer.valueOf(ans);
//            txtquestion.setText(questcontent);
//            btnop1.setText(op1);
//            btnop2.setText(op2);
//            btnop3.setText(op3);
//            btnop4.setText(op4);
//            break;
//        }


    }

    public void checkans() {
        btnconfirm.setEnabled(false);
        if (btnop1.isChecked()) {
            if (1 == answer) {
                questiontrue++;
                score += 5;
            }
        }
        if (btnop2.isChecked()) {
            if (2 == answer) {
                questiontrue++;
                score += 5;
            }
        }
        if (btnop3.isChecked()) {
            if (3 == answer) {
                questiontrue++;
                score += 5;
            }
        }
        if (btnop4.isChecked()) {
            if (4 == answer) {
                questiontrue++;
                score += 5;
            }
        }

        txtscore.setText("Score: " + score + "");
    }

    public void showanswer() {
        if (1 == answer) {
            btnop1.setBackground(this.getResources().getDrawable(R.drawable.button_2));
            btnop2.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop3.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop4.setBackground(this.getResources().getDrawable(R.drawable.button_1));

        }
        if (2 == answer) {
            btnop1.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop2.setBackground(this.getResources().getDrawable(R.drawable.button_2));
            btnop3.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop4.setBackground(this.getResources().getDrawable(R.drawable.button_1));

        }
        if (3 == answer) {
            btnop1.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop2.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop3.setBackground(this.getResources().getDrawable(R.drawable.button_2));
            btnop4.setBackground(this.getResources().getDrawable(R.drawable.button_1));

        }
        if (4 == answer) {
            btnop1.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop2.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop3.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop4.setBackground(this.getResources().getDrawable(R.drawable.button_2));

        }
    }
}