package com.example.projectse.user.ui.home;

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

import com.example.projectse.R;
import com.example.projectse.user.dienkhuyet.DienKhuyetActivity;
import com.example.projectse.user.hoctuvung.HocTuVungActivity;
import com.example.projectse.user.luyennghe.LuyenNgheActivity;
import com.example.projectse.user.sapxepcau.SapXepCauActivity;
import com.example.projectse.user.taikhoan.RankingActivity;
import com.example.projectse.user.tracnghiem.TracNghiemActivity;

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
                intent.putExtra("idSubjectCategory", 1);
                startActivity(intent);
            }
        });
        cardViewDienKhuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DienKhuyetActivity.class);
                intent.putExtra("idSubjectCategory", 5);
                startActivity(intent);
            }
        });
        cardViewTracNghiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TracNghiemActivity.class);
                intent.putExtra("idSubjectCategory", 2);
                startActivity(intent);
            }
        });
        cardViewSapXepCau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SapXepCauActivity.class);
                intent.putExtra("idSubjectCategory", 3);
                startActivity(intent);
            }
        });
        cardViewLuyenNghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LuyenNgheActivity.class);
                intent.putExtra("idSubjectCategory", 4);
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