package com.example.projectse.admin.hoctuvung;

import android.content.ComponentName;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
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
import com.example.projectse.admin.dialog.DialogAddVocab;
import com.example.projectse.admin.dialog.IonClickDialogAddUnit;
import com.example.projectse.admin.dialog.IonClickDialogAddVocab;
import com.example.projectse.admin.ui.BottomSheetMic;
import com.example.projectse.admin.ui.IonClickBtsMic;
import com.example.projectse.ultils.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DSTuVungActivityAdmin extends AppCompatActivity {

    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgback;
    GridView dstuvungs;
    Button Ontap;
    ArrayList<TuVung> DStuvung;
    ArrayList<String> listtu;
    DSTuVungAdapterAdmin adapter;
    private TextToSpeech tts;
    int idbo;
    StringBuilder text;
    BottomSheetMic bottomSheetMic;
    SpeechRecognizer speechRecognizer;
    Intent speechIntent;
    FloatingActionButton btnAdd;
    DialogAddVocab dialogAddVocab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ds_tuvung_admin);

        Intent intent = getIntent();
        idbo = intent.getIntExtra("idbo", 0);

        dstuvungs = (GridView) findViewById(R.id.lgvTuVung);
        Ontap = (Button) findViewById(R.id.btnOnTap);
        imgback = findViewById(R.id.imgVBackDSTV);
        btnAdd = findViewById(R.id.btn_add);
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

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Ontap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ontap = new Intent(DSTuVungActivityAdmin.this, WordMattersActivityAdmin.class);
                ontap.putExtra("idbo", idbo);
                ontap.putExtra("DStuvung", DStuvung);
                startActivity(ontap);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddVocab = new DialogAddVocab(DSTuVungActivityAdmin.this, "Add Vocabulary", "Add", new IonClickDialogAddVocab() {
                    @Override
                    public void onClickDialogAddVocab(String vocab, String type, String imgPreviewLink, String translate) {
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlAddVocab, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AddArrayTV();
                                dialogAddVocab.dismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> param = new HashMap<>();
                                param.put("vocab", vocab);
                                param.put("type", type);
                                param.put("imgPreview", imgPreviewLink);
                                param.put("translate", translate);
                                param.put("idUnitCategory", String.valueOf(idbo));
                                param.put("active", String.valueOf(0));
                                return param;
                            }
                        };
                        requestQueue1.add(stringRequest1);
                    }
                });
                dialogAddVocab.show();
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
                    Toast.makeText(DSTuVungActivityAdmin.this, "Phát âm chuẩn", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DSTuVungActivityAdmin.this, "Phát âm sai", Toast.LENGTH_SHORT).show();
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
                    adapter = new DSTuVungAdapterAdmin(DSTuVungActivityAdmin.this, R.layout.row_dstuvung_admin, DStuvung, new IonClickItemVocab() {
                        @Override
                        public void onClickSpeaker(TuVung tuVung) {
                            tts = new TextToSpeech(DSTuVungActivityAdmin.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        Toast.makeText(DSTuVungActivityAdmin.this, tuVung.getDapan(), Toast.LENGTH_SHORT).show();
                                        textToSpeed(tuVung.getDapan());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onClickMic(TuVung tuVung) {
                            //speedToText(tuVung);
                            bottomSheetMic = new BottomSheetMic(DSTuVungActivityAdmin.this, new IonClickBtsMic() {
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

                        @Override
                        public void onClickEdit(TuVung tuVung) {
                            dialogAddVocab = new DialogAddVocab(DSTuVungActivityAdmin.this, "Edit Vocab", "Edit", tuVung.getDapan(), tuVung.getLoaitu(), tuVung.getAnh(), tuVung.getDichnghia(), new IonClickDialogAddVocab() {
                                @Override
                                public void onClickDialogAddVocab(String vocab, String type, String imgPreviewLink, String translate) {
                                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlEditVocab, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            AddArrayTV();
                                            dialogAddVocab.dismiss();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            HashMap<String, String> param = new HashMap<>();
                                            param.put("id", String.valueOf(tuVung.getIdtu()));
                                            param.put("vocab", vocab);
                                            param.put("type", type);
                                            param.put("imgPreview", imgPreviewLink);
                                            param.put("translate", translate);
                                            param.put("idUnitCategory", String.valueOf(idbo));
                                            param.put("active", String.valueOf(0));
                                            return param;
                                        }
                                    };
                                    requestQueue1.add(stringRequest1);
                                }
                            });
                            dialogAddVocab.show();
                        }

                        @Override
                        public void onClickDelete(TuVung tuVung) {
                            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlDeleteVocab, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    AddArrayTV();
                                    Toast.makeText(DSTuVungActivityAdmin.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<>();
                                    param.put("id", String.valueOf(tuVung.getIdtu()));
                                    param.put("active", String.valueOf(1));
                                    return param;
                                }
                            };
                            requestQueue1.add(stringRequest1);
                        }
                    });
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
    }
}