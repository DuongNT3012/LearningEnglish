package com.example.projectse.admin.dienkhuyet;

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
import com.example.projectse.admin.luyennghe.CauLuyenNghe;

import java.util.ArrayList;

public class FillBlankAdapterAdmin extends RecyclerView.Adapter<FillBlankAdapterAdmin.ViewHolder> {
    private ArrayList<CauDienKhuyet> cauDienKhuyets;
    private IonClickItemFillBlank ionClickItemFillBlank;

    public FillBlankAdapterAdmin(ArrayList<CauDienKhuyet> cauDienKhuyets, IonClickItemFillBlank ionClickItemFillBlank) {
        this.cauDienKhuyets = cauDienKhuyets;
        this.ionClickItemFillBlank = ionClickItemFillBlank;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fillblank_admin, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvTitle.setText("Fill blank " + (position + 1));
        holder.edtAnswer.setText(cauDienKhuyets.get(position).getDapan());
        holder.edtQuestion.setText(cauDienKhuyets.get(position).getCauHoi());
        holder.edtSuggest.setText(cauDienKhuyets.get(position).getGoiY());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemFillBlank.onClickEdit(cauDienKhuyets.get(position), holder.edtQuestion.getText().toString().trim(), holder.edtAnswer.getText().toString().trim(), holder.edtSuggest.getText().toString().trim());
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemFillBlank.onClickDelete(cauDienKhuyets.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cauDienKhuyets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        EditText edtAnswer, edtQuestion, edtSuggest;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            edtAnswer = itemView.findViewById(R.id.edt_answer);
            edtQuestion = itemView.findViewById(R.id.edt_question);
            edtSuggest = itemView.findViewById(R.id.edt_suggest);
            imgEdit = itemView.findViewById(R.id.img_edit);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }
}

interface IonClickItemFillBlank {
    void onClickEdit(CauDienKhuyet cauDienKhuyet, String ques, String ans, String suggest);

    void onClickDelete(CauDienKhuyet cauDienKhuyet);
}

