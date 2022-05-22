package com.example.projectse.hoctuvung;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.example.projectse.bohoctap.BoHocTap;
import com.example.projectse.bohoctap.BoHocTapAdapter;
import com.example.projectse.ui.BottomSheetMic;
import com.example.projectse.ui.IonClickBtsMic;
import com.example.projectse.ui.home.Database;
import com.example.projectse.ultils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DSTuVungActivity extends AppCompatActivity {

    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgback;
    GridView dstuvungs;
    Button Ontap;
    ArrayList<TuVung> DStuvung;
    ArrayList<String> listtu;
    DSTuVungAdapter adapter;
    private TextToSpeech tts;
    int idbo;
    StringBuilder text;
    BottomSheetMic bottomSheetMic;
    SpeechRecognizer speechRecognizer;
    Intent speechIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ds_tuvung);

        Intent intent = getIntent();
        idbo = intent.getIntExtra("idbo", 0);

        dstuvungs = (GridView) findViewById(R.id.lgvTuVung);
        Ontap = (Button) findViewById(R.id.btnOnTap);
        imgback = findViewById(R.id.imgVBackDSTV);
        DStuvung = new ArrayList<>();
        listtu = new ArrayList<>();
        AddArrayTV();
        //speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(
                this,
                new ComponentName(
                        "com.google.android.tts",
                        "com.google.android.apps.speech.tts.googletts.service.GoogleTTSRecognitionService"
                )
        );
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        adapter = new DSTuVungAdapter(DSTuVungActivity.this, R.layout.row_dstuvung, DStuvung, new IonClickItemVocab() {
            @Override
            public void onClickSpeaker(TuVung tuVung) {
                tts = new TextToSpeech(DSTuVungActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Toast.makeText(DSTuVungActivity.this, tuVung.getDapan(), Toast.LENGTH_SHORT).show();
                            textToSpeed(tuVung.getDapan());
                        }
                    }
                });
            }

            @Override
            public void onClickMic(TuVung tuVung) {
                //speedToText(tuVung);
                bottomSheetMic = new BottomSheetMic(DSTuVungActivity.this, new IonClickBtsMic() {
                    @Override
                    public void onClickBtsMic() {
                        speechIntent.putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        );
                        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
                        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                        speechRecognizer.startListening(speechIntent);
                        recognitionListener(tuVung);
                    }
                });
                bottomSheetMic.show(getSupportFragmentManager(), "BottomSheetMic");
                speechIntent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                );
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
                speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                speechRecognizer.startListening(speechIntent);
                recognitionListener(tuVung);
            }
        });
        dstuvungs.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Ontap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ontap = new Intent(DSTuVungActivity.this, WordMattersActivity.class);
                ontap.putExtra("idbo", idbo);
                ontap.putExtra("DStuvung", DStuvung);
                startActivity(ontap);
            }
        });
    }

    private void recognitionListener(TuVung tuVung) {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                if (BottomSheetMic.tv_text.getText().toString().toLowerCase().equals(tuVung.getDapan().toLowerCase())) {
                    Toast.makeText(DSTuVungActivity.this, "Phát âm chuẩn", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DSTuVungActivity.this, "Phát âm sai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> arrayList =
                        bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                BottomSheetMic.tv_text.setText(arrayList.get(0));
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private void textToSpeed(String s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = new Bundle();
            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, bundle, null);
        } else {
            HashMap<String, String> param = new HashMap<>();
            param.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, param);
        }
    }

    private void AddArrayTV() {
        /*database = Database.initDatabase(DSTuVungActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM TuVung WHERE ID_Bo = ?", new String[]{String.valueOf(idbo)});
        DStuvung.clear();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int idtu = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String dapan = cursor.getString(2);
            String dichnghia = cursor.getString(3);
            String loaitu = cursor.getString(4);
            String audio = cursor.getString(5);
            byte[] anh = cursor.getBlob(6);

            DStuvung.add(new TuVung(idtu, idbo, dapan, dichnghia, loaitu, audio, anh));
        }*/
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGetVocab, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String vocab = "";
                String type = "";
                String imgPreview = "";
                String translate = "";
                int idUnitCategory = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    DStuvung.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        vocab = jsonObject.getString("vocab");
                        type = jsonObject.getString("type");
                        imgPreview = jsonObject.getString("imgPreview");
                        translate = jsonObject.getString("translate");
                        idUnitCategory = jsonObject.getInt("idUnitCategory");
                        DStuvung.add(new TuVung(id, idUnitCategory, vocab, translate, type, "", imgPreview));
                    }
                    dstuvungs.setAdapter(adapter);
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
                param.put("idUnitCategory", String.valueOf(idbo));
                return param;
            }
        };
        requestQueue.add(stringRequest);

        /*DStuvung.clear();
        switch (idbo){
            case 1:
                DStuvung.add(new TuVung(1,idbo,"president","Tổng thống","Danh từ","", R.drawable.president));
                DStuvung.add(new TuVung(2,idbo,"customer","Khách hàng","Danh từ","", R.drawable.customer));
                DStuvung.add(new TuVung(3,idbo,"purchase","Mua","Động từ","", R.drawable.purchase));
                DStuvung.add(new TuVung(4,idbo,"item","Món hàng","Danh từ","", R.drawable.item));
                DStuvung.add(new TuVung(5,idbo,"consultant","Tư vấn viên","Danh từ","", R.drawable.consultant));
                break;
            case 2:
                DStuvung.add(new TuVung(1,idbo,"resign","Từ chức","Động từ","", R.drawable.resign));
                DStuvung.add(new TuVung(2,idbo,"payroll","Lương","Danh từ","", R.drawable.payroll));
                break;
            case 3:
                DStuvung.add(new TuVung(1,idbo,"advise","Khuyên bảo","Động từ","", R.drawable.advise));
                DStuvung.add(new TuVung(2,idbo,"leadership","Lãnh đạo","Danh từ","", R.drawable.leadership));
                break;
            case 4:
                DStuvung.add(new TuVung(1,idbo,"consider","Xem xét","Động từ","", R.drawable.consider));
                DStuvung.add(new TuVung(2,idbo,"contract","Hợp đồng","Danh từ","", R.drawable.contract));
                break;
        }*/
    }
}