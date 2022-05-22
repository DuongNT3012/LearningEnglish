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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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

public class ListeningActivityAdmin extends AppCompatActivity {
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    DatabaseAccess DB;
    TextView txtscore, txtquestcount;
    RadioGroup rdgchoices;
    RadioButton btnop1, btnop2, btnop3, btnop4;
    Button btnconfirm;
    Button btnquit;
    ImageView imHA;
    private ArrayList<CauLuyenNghe> cauLuyenNghes;
    private MediaPlayer mediaPlayer;
    private ImageButton ImgBT;

    User user;

    String URL = "https://github.com/Lap2000/songs/raw/main/Bay-Giua-Ngan-Ha-Nam-Cuong.mp3";
    int questioncurrent = 0;
    int questiontrue = 0;
    int answer = 0;
    int score = 0;
    int idbo;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_listening);
        DB = DatabaseAccess.getInstance(getApplicationContext());
        Anhxa();
        LayUser();
        Intent intent = getIntent();
        idbo = intent.getIntExtra("Bo", 0);
        cauLuyenNghes = new ArrayList<>();
        AddArrayCLN();


        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnop1.isChecked() && !btnop2.isChecked() && !btnop3.isChecked() && !btnop4.isChecked()) {
                    Toast.makeText(ListeningActivityAdmin.this, "Choose an answer", Toast.LENGTH_SHORT).show();
                } else {
                    checkans();
                    questioncurrent++;
                    countDownTimer.start();
                }
            }
        });


        // When the video file ready for playback.
        this.ImgBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http://example.coom/mysong.mp3
                //String mediaURL = MediaPlayerUtils.URL_MEDIA_SAMPLE;
                String mediaURL = URL;
                MediaPlayerUtils.playURLMedia(ListeningActivityAdmin.this, mediaPlayer, mediaURL);
                //doStart();
            }
        });

        btnquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                doStop();
                Intent intent
                        = new Intent(ListeningActivityAdmin.this,
                        LuyenNgheActivityAdmin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void Anhxa() {
        txtscore = findViewById(R.id.txtscoreLN);
        txtquestcount = findViewById(R.id.txtquestcountLN);
        rdgchoices = findViewById(R.id.radiochoices);
        btnop1 = findViewById(R.id.op1);
        btnop2 = findViewById(R.id.op2);
        btnop3 = findViewById(R.id.op3);
        btnop4 = findViewById(R.id.op4);
        btnconfirm = findViewById(R.id.btnconfirmLN);
        btnquit = findViewById(R.id.btnQuitLN);
        imHA = findViewById(R.id.imgHinh);
        ImgBT = findViewById(R.id.ImgBT);
    }

    private void AddArrayCLN() {
        /*database = Database.initDatabase(ListeningActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM LuyenNghe WHERE ID_Bo = ?", new String[]{String.valueOf(idbo)});
        cauLuyenNghes.clear();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idbai = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String A = cursor.getString(2);
            String B = cursor.getString(3);
            String C = cursor.getString(4);
            String D = cursor.getString(5);
            String True = cursor.getString(6);
            byte[] hinh = cursor.getBlob(7);
            String audio = cursor.getString(8);

            cauLuyenNghes.add(new CauLuyenNghe(idbai, idbo, A, B, C, D, True, hinh, audio));
        }*/

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
                    shownextquestion(questioncurrent, cauLuyenNghes);

                    // Create MediaPlayer.
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    countDownTimer = new CountDownTimer(3000, 300) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            showanswer();
                        }

                        @Override
                        public void onFinish() {

                            btnconfirm.setEnabled(true);
                            shownextquestion(questioncurrent, cauLuyenNghes);
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

        /*cauLuyenNghes.clear();
        switch (idbo){
            case 1:
                cauLuyenNghes.add(new CauLuyenNghe(1, idbo, "A", "B", "C", "D", "2", R.drawable.listening1, "https://github.com/Lap2000/songs/raw/main/hinh01.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(2, idbo, "A", "B", "C", "D", "4", R.drawable.listening2, "https://github.com/Lap2000/songs/raw/main/hinh02.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(3, idbo, "A", "B", "C", "D", "1", R.drawable.listening3, "https://github.com/Lap2000/songs/raw/main/hinh03.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(4, idbo, "A", "B", "C", "D", "4", R.drawable.listening4, "https://github.com/Lap2000/songs/raw/main/hinh04.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(5, idbo, "A", "B", "C", "D", "3", R.drawable.listening5, "https://github.com/Lap2000/songs/raw/main/hinh05.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(6, idbo, "A", "B", "C", "D", "3", R.drawable.listening6, "https://github.com/Lap2000/songs/raw/main/hinh06.wav"));
                break;
            case 2:
                cauLuyenNghes.add(new CauLuyenNghe(1, idbo, "A", "B", "C", "D", "2", R.drawable.listening1, "https://github.com/Lap2000/songs/raw/main/hinh01.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(2, idbo, "A", "B", "C", "D", "4", R.drawable.listening2, "https://github.com/Lap2000/songs/raw/main/hinh02.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(3, idbo, "A", "B", "C", "D", "1", R.drawable.listening3, "https://github.com/Lap2000/songs/raw/main/hinh03.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(4, idbo, "A", "B", "C", "D", "4", R.drawable.listening4, "https://github.com/Lap2000/songs/raw/main/hinh04.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(5, idbo, "A", "B", "C", "D", "3", R.drawable.listening5, "https://github.com/Lap2000/songs/raw/main/hinh05.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(6, idbo, "A", "B", "C", "D", "3", R.drawable.listening6, "https://github.com/Lap2000/songs/raw/main/hinh06.wav"));
                break;
            case 3:
                cauLuyenNghes.add(new CauLuyenNghe(1, idbo, "A", "B", "C", "D", "2", R.drawable.listening1, "https://github.com/Lap2000/songs/raw/main/hinh01.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(2, idbo, "A", "B", "C", "D", "4", R.drawable.listening2, "https://github.com/Lap2000/songs/raw/main/hinh02.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(3, idbo, "A", "B", "C", "D", "1", R.drawable.listening3, "https://github.com/Lap2000/songs/raw/main/hinh03.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(4, idbo, "A", "B", "C", "D", "4", R.drawable.listening4, "https://github.com/Lap2000/songs/raw/main/hinh04.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(5, idbo, "A", "B", "C", "D", "3", R.drawable.listening5, "https://github.com/Lap2000/songs/raw/main/hinh05.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(6, idbo, "A", "B", "C", "D", "3", R.drawable.listening6, "https://github.com/Lap2000/songs/raw/main/hinh06.wav"));
                break;
            case 4:
                cauLuyenNghes.add(new CauLuyenNghe(1, idbo, "A", "B", "C", "D", "2", R.drawable.listening1, "https://github.com/Lap2000/songs/raw/main/hinh01.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(2, idbo, "A", "B", "C", "D", "4", R.drawable.listening2, "https://github.com/Lap2000/songs/raw/main/hinh02.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(3, idbo, "A", "B", "C", "D", "1", R.drawable.listening3, "https://github.com/Lap2000/songs/raw/main/hinh03.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(4, idbo, "A", "B", "C", "D", "4", R.drawable.listening4, "https://github.com/Lap2000/songs/raw/main/hinh04.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(5, idbo, "A", "B", "C", "D", "3", R.drawable.listening5, "https://github.com/Lap2000/songs/raw/main/hinh05.wav"));
                cauLuyenNghes.add(new CauLuyenNghe(6, idbo, "A", "B", "C", "D", "3", R.drawable.listening6, "https://github.com/Lap2000/songs/raw/main/hinh06.wav"));
                break;
        }*/
    }

    public void LayUser() {
        try {
            database = Database.initDatabase(ListeningActivityAdmin.this, DATABASE_NAME);
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

    public void shownextquestion(int pos, ArrayList<CauLuyenNghe> cauLuyenNghes) {

        if (pos > 0) doStop();
//        database= Database.initDatabase(ListeningActivity.this,DATABASE_NAME);
//        String a=null;
//        Cursor cursor=database.rawQuery("SELECT * FROM LuyenNghe WHERE ID_Bo=?",new String[]{String.valueOf(idbo)});
//        txtquestcount.setText("Question: "+(questioncurrent+1)+"/"+cursor.getCount()+"");
        txtquestcount.setText("Question: " + (questioncurrent + 1) + "/" + cauLuyenNghes.size() + "");
        rdgchoices.clearCheck();
        btnop1.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop2.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop3.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop4.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
//        if(pos==cursor.getCount()){
        try {
            if (pos == cauLuyenNghes.size()) {
                //DB.capnhatdiem(DB.iduser, user.getPoint(), score);
                Intent intent = new Intent(ListeningActivityAdmin.this, FinishQuizLSActivityAdmin.class);
                intent.putExtra("score", score);
                intent.putExtra("questiontrue", questiontrue);
                intent.putExtra("qcount", pos);
                startActivity(intent);
            } else {
                String anh = cauLuyenNghes.get(pos).getHinhanh();
                /*Bitmap img = BitmapFactory.decodeByteArray(anh, 0, anh.length);
                imHA.setImageBitmap(img);*/
                //imHA.setImageResource(anh);
                Glide.with(this).load(anh).into(imHA);

                String URLaudio = cauLuyenNghes.get(pos).getAudio();
                URL = URLaudio;

                answer = Integer.valueOf(cauLuyenNghes.get(pos).getDapanTrue());
                btnop1.setText(cauLuyenNghes.get(pos).getDapanA());
                btnop2.setText(cauLuyenNghes.get(pos).getDapanB());
                btnop3.setText(cauLuyenNghes.get(pos).getDapanC());
                btnop4.setText(cauLuyenNghes.get(pos).getDapanD());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for(int i=pos;i<cursor.getCount();i++){
//            cursor.moveToPosition(i);
//            byte[] anh = cursor.getBlob(7);
//            Bitmap img= BitmapFactory.decodeByteArray(anh,0,anh.length);
//            imHA.setImageBitmap(img);
//            String URLaudio = cursor.getString(8);
//            URL = URLaudio;
//            //String questcontent=cursor.getString(8);
//            String op1=cursor.getString(2);
//            String op2=cursor.getString(3);
//            String op3=cursor.getString(4);
//            String op4=cursor.getString(5);
//            String ans=cursor.getString(6);
//            answer=Integer.valueOf(ans);
//            btnop1.setText(op1);
//            btnop2.setText(op2);
//            btnop3.setText(op3);
//            btnop4.setText(op4);
//
//            break;
//        }


    }

    public void checkans() {
        btnconfirm.setEnabled(false);
        if (btnop1.isChecked()) {
            if (1 == answer) {

                score += 5;
                questiontrue++;
            }
        }
        if (btnop2.isChecked()) {
            if (2 == answer) {

                score += 5;
                questiontrue++;
            }
        }
        if (btnop3.isChecked()) {
            if (3 == answer) {

                score += 5;
                questiontrue++;
            }
        }
        if (btnop4.isChecked()) {
            if (4 == answer) {

                score += 5;
                questiontrue++;
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

    private void doStart() {
        if (this.mediaPlayer.isPlaying()) {
            //this.mediaPlayer.stop();
            this.mediaPlayer.pause();
            this.mediaPlayer.reset();
        } else {
            this.mediaPlayer.start();
        }
    }

    private void doStop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
    }
}
