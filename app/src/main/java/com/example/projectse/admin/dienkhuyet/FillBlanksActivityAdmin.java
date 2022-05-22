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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectse.R;
import com.example.projectse.admin.ui.home.Database;
import com.example.projectse.ultils.Server;
import com.example.projectse.user.taikhoan.DatabaseAccess;
import com.example.projectse.user.taikhoan.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FillBlanksActivityAdmin extends AppCompatActivity {

    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    DatabaseAccess DB;
    TextView txtscoreDK, txtquestcountDK, txtquestionDK, txttimeDK, txtGoiy;
    EditText edtAnswerDK;
    Button btnconfirm, btnQuit;
    int questioncurrent = 0;
    int questiontrue = 0;
    String answer1;
    int score = 0;
    int idbo;

    User user;
    ArrayList<CauDienKhuyet> cauDienKhuyets = new ArrayList<>();
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fill_blanks);
        DB = DatabaseAccess.getInstance(getApplicationContext());

        Anhxa();
        LayUser();

        Intent intent = getIntent();
        idbo = intent.getIntExtra("BoDK", 0);

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
                    txttimeDK.setText(" ");
                    shownextquestion(questioncurrent);


                    countDownTimer = new CountDownTimer(3000, 2000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            questioncurrent++;
                            edtAnswerDK.setTextColor(Color.BLACK);
                            btnconfirm.setEnabled(true);
                            edtAnswerDK.setText("");
                            answer1 = "";
                            shownextquestion(questioncurrent);
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
                cauDienKhuyets.clear();
                cauDienKhuyets.add(new CauDienKhuyet(1, 1, "The frog can___", "jump", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(2, 1, "The duck___swim", "can", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(3, 1, "The rabbit likes to___carrots", "eat", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(4, 1, "My dogs___fast", "run", "eat run can jump"));
                break;
            case 2:
                cauDienKhuyets.clear();
                cauDienKhuyets.add(new CauDienKhuyet(1, 2, "The frog can___", "jump", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(2, 2, "The duck___swim", "can", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(3, 2, "The rabbit likes to___carrots", "eat", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(4, 2, "My dogs___fast", "run", "eat run can jump"));
                break;
            case 3:
                cauDienKhuyets.clear();
                cauDienKhuyets.add(new CauDienKhuyet(1, 3, "The frog can___", "jump", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(2, 3, "The duck___swim", "can", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(3, 3, "The rabbit likes to___carrots", "eat", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(4, 3, "My dogs___fast", "run", "eat run can jump"));
                break;
            case 4:
                cauDienKhuyets.clear();
                cauDienKhuyets.add(new CauDienKhuyet(1, 4, "The frog can___", "jump", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(2, 4, "The duck___swim", "can", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(3, 4, "The rabbit likes to___carrots", "eat", "eat run can jump"));
                cauDienKhuyets.add(new CauDienKhuyet(4, 4, "My dogs___fast", "run", "eat run can jump"));
                break;
        }*/

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                showanswer();

                countDownTimer.start();
            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillBlanksActivityAdmin.this, DienKhuyetActivityAdmin.class);
                startActivity(intent);
            }
        });

    }

    public void LayUser() {
        try {
            database = Database.initDatabase(FillBlanksActivityAdmin.this, DATABASE_NAME);
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

    public void shownextquestion(int pos) {

        /*database = Database.initDatabase(FillBlanksActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM DienKhuyet WHERE ID_Bo=?", new String[]{String.valueOf(idbo)});*/

        /*cauDienKhuyets.clear();
        cauDienKhuyets.add(new CauDienKhuyet(1, idbo, ""));*/

        txtquestcountDK.setText("Question: " + (questioncurrent + 1) + "/" + cauDienKhuyets.size() + "");

        edtAnswerDK.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));


        if (pos == cauDienKhuyets.size()) {
            //DB.capnhatdiem(DB.iduser, user.getPoint(), score);
            Intent intent = new Intent(FillBlanksActivityAdmin.this, FinishDKActivityAdmin.class);
            intent.putExtra("scoreDK", score);
            intent.putExtra("questiontrueDK", questiontrue);
            intent.putExtra("qcountDK", pos);
            startActivity(intent);
            finish();
        }
        for (int i = pos; i < cauDienKhuyets.size(); i++) {
            //cursor.moveToPosition(i);
            String questcontent = cauDienKhuyets.get(i).getCauHoi();
            answer1 = cauDienKhuyets.get(i).getDapan();
            txtGoiy.setText(cauDienKhuyets.get(i).getGoiY());
            txtquestionDK.setText(questcontent);
            break;
        }
    }

    public void showanswer() {
        edtAnswerDK.setText(answer1);
        edtAnswerDK.setTextColor(Color.GREEN);
        edtAnswerDK.clearFocus();
    }

    public void checkAnswer() {
        btnconfirm.setEnabled(false);
        if (answer1.equals(edtAnswerDK.getText().toString())) {
            Toast.makeText(this, "Đáp án chính xác", Toast.LENGTH_SHORT).show();
            edtAnswerDK.setTextColor(Color.GREEN);
            questiontrue++;
            score += 5;
            txtscoreDK.setText("Score: " + score);
            edtAnswerDK.clearFocus();

        } else {
            Toast.makeText(this, "Sai rồi", Toast.LENGTH_SHORT).show();
            edtAnswerDK.setTextColor(Color.RED);
            edtAnswerDK.startAnimation(shakeError());
            edtAnswerDK.clearFocus();


        }

    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    public void Anhxa() {
        txtscoreDK = findViewById(R.id.txtscoreDK);
        txtquestcountDK = findViewById(R.id.txtquestcountDK);
        txtquestionDK = findViewById(R.id.txtquestionDK);
        txttimeDK = findViewById(R.id.txttimeDK);
        edtAnswerDK = findViewById(R.id.AnswerDK);
        btnconfirm = findViewById(R.id.btnconfirmDK);
        txtGoiy = findViewById(R.id.textviewGoiy);
        btnQuit = findViewById(R.id.btnQuitDK);
    }

}