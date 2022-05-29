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

public class DialogAddArrangeSentence extends Dialog {
    private Context context;
    TextView tvTitle;
    EditText edtAnswer, edtPart1, edtPart2, edtPart3, edtPart4;
    private AppCompatButton btnCancel;
    private AppCompatButton btnAdd;
    private IonClickAddArrangeSentence ionClickAddArrangeSentence;

    public DialogAddArrangeSentence(Context context, IonClickAddArrangeSentence ionClickAddArrangeSentence) {
        super(context, R.style.BaseDialog);
        this.context = context;
        this.ionClickAddArrangeSentence = ionClickAddArrangeSentence;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_arrange_sentence);

        edtAnswer = findViewById(R.id.edt_answer);
        edtPart1 = findViewById(R.id.edt_part1);
        edtPart2 = findViewById(R.id.edt_part2);
        edtPart3 = findViewById(R.id.edt_part3);
        edtPart4 = findViewById(R.id.edt_part4);
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
                if (edtAnswer.getText().toString().trim().equals("") || edtPart1.getText().toString().trim().equals("") || edtPart2.getText().toString().trim().equals("") || edtPart3.getText().toString().trim().equals("") || edtPart4.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    ionClickAddArrangeSentence.onClickAddArrangeSentence(edtAnswer.getText().toString().trim(), edtPart1.getText().toString().trim(), edtPart2.getText().toString().trim(), edtPart3.getText().toString().trim(), edtPart4.getText().toString().trim());
                }
            }
        });
    }
}

