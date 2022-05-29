package com.example.projectse.admin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.projectse.R;

public class DialogAddListening extends Dialog {
    private Context context;
    TextView tvTitle;
    EditText edtAnswer, edtImgPreview, edtAudio;
    private AppCompatButton btnCancel;
    private AppCompatButton btnAdd;
    private IonClickAddListening ionClickAddListening;

    public DialogAddListening(Context context, IonClickAddListening ionClickAddListening) {
        super(context, R.style.BaseDialog);
        this.context = context;
        this.ionClickAddListening = ionClickAddListening;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_listening);

        edtAnswer = findViewById(R.id.edt_answer);
        edtImgPreview = findViewById(R.id.edt_imgPreview);
        edtAudio = findViewById(R.id.edt_audio);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAdd = findViewById(R.id.btn_add);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAnswer.getText().toString().trim().equals("") || edtImgPreview.getText().toString().trim().equals("") || edtAudio.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    ionClickAddListening.onClickAddListening(edtAnswer.getText().toString().trim(), edtImgPreview.getText().toString().trim(), edtAudio.getText().toString().trim());
                }
            }
        });
    }
}

