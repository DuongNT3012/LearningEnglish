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

public class DialogAddFillBlank extends Dialog {
    private Context context;
    TextView tvTitle;
    EditText edtAnswer, edtQuestion, edtSuggest;
    private AppCompatButton btnCancel;
    private AppCompatButton btnAdd;
    private IonClickAddFillBlank ionClickAddFillBlank;

    public DialogAddFillBlank(Context context, IonClickAddFillBlank ionClickAddFillBlank) {
        super(context, R.style.BaseDialog);
        this.context = context;
        this.ionClickAddFillBlank = ionClickAddFillBlank;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_fillblank);

        edtQuestion = findViewById(R.id.edt_question);
        edtAnswer = findViewById(R.id.edt_answer);
        edtSuggest = findViewById(R.id.edt_suggest);
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
                if (edtAnswer.getText().toString().trim().equals("") || edtQuestion.getText().toString().trim().equals("") || edtSuggest.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    ionClickAddFillBlank.onClickAddFillBlank(edtQuestion.getText().toString().trim(), edtAnswer.getText().toString().trim(), edtSuggest.getText().toString().trim());
                }
            }
        });
    }
}

