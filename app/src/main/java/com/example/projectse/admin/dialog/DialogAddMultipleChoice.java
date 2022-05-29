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

public class DialogAddMultipleChoice extends Dialog {
    private Context context;
    TextView tvTitle;
    EditText edtQuestion, edtAnswer1, edtAnswer2, edtAnswer3, edtAnswer4, edtAnswer;
    private AppCompatButton btnCancel;
    private AppCompatButton btnAdd;
    private IonClickAddMultipleChoice ionClickAddMultipleChoice;

    public DialogAddMultipleChoice(Context context, IonClickAddMultipleChoice ionClickAddMultipleChoice) {
        super(context, R.style.BaseDialog);
        this.context = context;
        this.ionClickAddMultipleChoice = ionClickAddMultipleChoice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_multiple_choice);

        edtQuestion = findViewById(R.id.edt_question);
        edtAnswer1 = findViewById(R.id.edt_answer1);
        edtAnswer2 = findViewById(R.id.edt_answer2);
        edtAnswer3 = findViewById(R.id.edt_answer3);
        edtAnswer4 = findViewById(R.id.edt_answer4);
        edtAnswer = findViewById(R.id.edt_answer);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAdd = findViewById(R.id.btn_add);
        tvTitle = findViewById(R.id.tv_title);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtQuestion.getText().toString().trim().equals("") || edtAnswer1.getText().toString().trim().equals("") || edtAnswer2.getText().toString().trim().equals("") || edtAnswer3.getText().toString().trim().equals("") || edtAnswer4.getText().toString().trim().equals("") || edtAnswer.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    ionClickAddMultipleChoice.onClickAddMultipleChoice(edtQuestion.getText().toString().trim(), edtAnswer1.getText().toString().trim(), edtAnswer2.getText().toString().trim(), edtAnswer3.getText().toString().trim(), edtAnswer4.getText().toString().trim(), edtAnswer.getText().toString().trim());
                }
            }
        });
    }
}

