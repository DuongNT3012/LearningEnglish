package com.example.projectse.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectse.MainActivity;
import com.example.projectse.R;
import com.example.projectse.dienkhuyet.DienKhuyetActivity;
import com.example.projectse.hoctuvung.HocTuVungActivity;
import com.example.projectse.luyennghe.LuyenNgheActivity;
import com.example.projectse.sapxepcau.SapXepCauActivity;
import com.example.projectse.taikhoan.RankingActivity;
import com.example.projectse.tracnghiem.TracNghiemActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    CardView cardViewHocTuVung, cardViewTracNghiem, cardViewSapXepCau, cardViewLuyenNghe, cardViewDienKhuyet, cardViewXepHang;
    ImageView imgview;
    final String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        cardViewHocTuVung = root.findViewById(R.id.cardViewHocTuVung);
        cardViewDienKhuyet = root.findViewById(R.id.cardViewDienKhuyet);
        cardViewTracNghiem = root.findViewById(R.id.cardViewTracNghiem);
        cardViewSapXepCau = root.findViewById(R.id.cardViewSapXepCau);
        cardViewLuyenNghe = root.findViewById(R.id.cardViewLuyenNghe);
        cardViewXepHang = root.findViewById(R.id.cardViewXepHang);
        cardViewHocTuVung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HocTuVungActivity.class);
                startActivity(intent);
            }
        });
        cardViewDienKhuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DienKhuyetActivity.class);
                startActivity(intent);
            }
        });
        cardViewTracNghiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TracNghiemActivity.class);
                startActivity(intent);
            }
        });
        cardViewSapXepCau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SapXepCauActivity.class);
                startActivity(intent);
            }
        });
        cardViewLuyenNghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LuyenNgheActivity.class);
                startActivity(intent);
            }
        });
        cardViewXepHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RankingActivity.class);
                startActivity(intent);
            }
        });

        //Permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1111);
        }

        return root;
    }


}