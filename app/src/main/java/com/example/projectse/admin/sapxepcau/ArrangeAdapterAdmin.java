package com.example.projectse.admin.sapxepcau;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectse.R;
import com.example.projectse.admin.tracnghiem.CauTracNghiem;
import com.example.projectse.admin.tracnghiem.QuizAdapterAdmin;

import java.util.ArrayList;

public class ArrangeAdapterAdmin  extends RecyclerView.Adapter<ArrangeAdapterAdmin.ViewHolder> {
    private ArrayList<CauSapXep> cauSapXeps;
    private IonClickItemArrange ionClickItemArrange;

    public ArrangeAdapterAdmin(ArrayList<CauSapXep> cauSapXeps, IonClickItemArrange ionClickItemArrange) {
        this.cauSapXeps = cauSapXeps;
        this.ionClickItemArrange = ionClickItemArrange;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arrange_admin, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvTitle.setText("Arrange Sentence " + (position + 1));
        holder.edtAnswer.setText(cauSapXeps.get(position).getDapan());
        holder.edtPart1.setText(cauSapXeps.get(position).getPart1());
        holder.edtPart2.setText(cauSapXeps.get(position).getPart2());
        holder.edtPart3.setText(cauSapXeps.get(position).getPart3());
        holder.edtPart4.setText(cauSapXeps.get(position).getPart4());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemArrange.onClickEdit(cauSapXeps.get(position), holder.edtAnswer.getText().toString().trim(), holder.edtPart1.getText().toString().trim(), holder.edtPart2.getText().toString().trim(), holder.edtPart3.getText().toString().trim(), holder.edtPart4.getText().toString().trim());
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemArrange.onClickDelete(cauSapXeps.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cauSapXeps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        EditText edtAnswer, edtPart1, edtPart2, edtPart3, edtPart4;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            edtAnswer = itemView.findViewById(R.id.edt_answer);
            edtPart1 = itemView.findViewById(R.id.edt_part1);
            edtPart2 = itemView.findViewById(R.id.edt_part2);
            edtPart3 = itemView.findViewById(R.id.edt_part3);
            edtPart4 = itemView.findViewById(R.id.edt_part4);
            imgEdit = itemView.findViewById(R.id.img_edit);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }
}

interface IonClickItemArrange {
    void onClickEdit(CauSapXep cauSapXep, String answer, String a, String b, String c, String d);

    void onClickDelete(CauSapXep cauSapXep);
}
