package com.example.projectse.admin.bohoctap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectse.R;

import java.util.List;

public class BoHocTapAdapterAdmin extends RecyclerView.Adapter<BoHocTapAdapterAdmin.ViewHolder> {
    private Context context;
    private int layout;
    private List<BoHocTapAdmin> boHocTapAdminList;
    private IonClickItemUnit ionClickItemUnit;

    public BoHocTapAdapterAdmin(Context context, int layout, List<BoHocTapAdmin> boHocTapAdminList, IonClickItemUnit ionClickItemUnit) {
        this.context = context;
        this.layout = layout;
        this.boHocTapAdminList = boHocTapAdminList;
        this.ionClickItemUnit = ionClickItemUnit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BoHocTapAdmin BoHT = boHocTapAdminList.get(position);
        holder.txtTenBo.setText(BoHT.getTenBo());
        Glide.with(context).load(BoHT.getImgPreview()).into(holder.imageView4);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemUnit.onClickItemUnit(position);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemUnit.onClickDelete(BoHT.getIdBo());
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return boHocTapAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenBo;
        ImageView imgDelete, imageView4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenBo = itemView.findViewById(R.id.tvTenBo);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imageView4 = itemView.findViewById(R.id.imageView4);
        }
    }
}

