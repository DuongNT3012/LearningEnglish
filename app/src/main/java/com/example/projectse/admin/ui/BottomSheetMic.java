package com.example.projectse.admin.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectse.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetMic extends BottomSheetDialogFragment {
    BottomSheetDialog bottomSheetDialog;
    Context context;
    public static TextView tv_text;
    ImageView imgMic, imgClose;
    IonClickBtsMic ionClickBtsMic;

    public BottomSheetMic(Context context, IonClickBtsMic ionClickBtsMic) {
        this.context = context;
        this.ionClickBtsMic = ionClickBtsMic;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_mic, null);
        bottomSheetDialog.setContentView(view);
        tv_text = view.findViewById(R.id.tv_text);
        imgMic = view.findViewById(R.id.img_mic);
        imgClose = view.findViewById(R.id.img_close);

        imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickBtsMic.onClickBtsMic();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return bottomSheetDialog;
    }
}

