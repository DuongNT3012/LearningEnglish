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

public class DialogAddVocab extends Dialog {
    private Context context;
    private EditText edtVocabType;
    private EditText edtVocab;
    private EditText edtImgPreview;
    private EditText edtTranslate;
    private AppCompatButton btnCancel;
    private AppCompatButton btnAdd;
    private IonClickDialogAddVocab ionClickDialogAddVocab;
    private String title, addOrEdit, vocab, type, imgPreview, translate;
    private TextView tvTitle;

    public DialogAddVocab(Context context, String title, String addOrEdit, IonClickDialogAddVocab ionClickDialogAddVocab) {
        super(context, R.style.BaseDialog);
        this.context = context;
        this.ionClickDialogAddVocab = ionClickDialogAddVocab;
        this.title = title;
        this.addOrEdit = addOrEdit;
    }

    public DialogAddVocab(Context context, String title, String addOrEdit, String vocab, String type, String imgPreview, String translate, IonClickDialogAddVocab ionClickDialogAddVocab) {
        super(context, R.style.BaseDialog);
        this.context = context;
        this.ionClickDialogAddVocab = ionClickDialogAddVocab;
        this.title = title;
        this.addOrEdit = addOrEdit;
        this.vocab = vocab;
        this.type = type;
        this.imgPreview = imgPreview;
        this.translate = translate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_vocab);

        edtVocabType = findViewById(R.id.edt_type);
        edtImgPreview = findViewById(R.id.edt_imgPreview);
        edtVocab = findViewById(R.id.edt_vocab);
        edtTranslate = findViewById(R.id.edt_translate);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAdd = findViewById(R.id.btn_add);
        tvTitle = findViewById(R.id.tv_title);

        tvTitle.setText(title);
        btnAdd.setText(addOrEdit);
        edtVocab.setText(vocab);
        edtVocabType.setText(type);
        edtImgPreview.setText(imgPreview);
        edtTranslate.setText(translate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtVocab.getText().toString().trim().equals("") || edtVocabType.getText().toString().trim().equals("") || edtTranslate.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    ionClickDialogAddVocab.onClickDialogAddVocab(edtVocab.getText().toString().trim(), edtVocabType.getText().toString().trim(), edtImgPreview.getText().toString().trim(), edtTranslate.getText().toString().trim());
                }
            }
        });
    }
}
