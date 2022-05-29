package com.example.projectse.admin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.projectse.R;

public class DialogAddUnit extends Dialog {
    private Context context;
    private EditText edtName;
    private EditText edtImgPreview;
    private AppCompatButton btnCancel;
    private AppCompatButton btnAdd;
    private IonClickDialogAddUnit ionClickDialogAddUnit;

    public DialogAddUnit(Context context, IonClickDialogAddUnit ionClickDialogAddUnit) {
        super(context, R.style.BaseDialog);
        this.context = context;
        this.ionClickDialogAddUnit = ionClickDialogAddUnit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_unit);

        edtName = findViewById(R.id.edt_name);
        edtImgPreview = findViewById(R.id.edt_imgPreview);
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
                if (edtName.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    ionClickDialogAddUnit.onClickDialogAddUnit(edtName.getText().toString().trim(), edtImgPreview.getText().toString().trim());
                }
            }
        });
    }
}
