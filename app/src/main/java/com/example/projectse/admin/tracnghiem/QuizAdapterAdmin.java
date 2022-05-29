package com.example.projectse.admin.tracnghiem;

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

import java.util.ArrayList;

public class QuizAdapterAdmin extends RecyclerView.Adapter<QuizAdapterAdmin.ViewHolder> {
    private ArrayList<CauTracNghiem> cauTracNghiems;
    private IonClickItemMultipleChoice ionClickItemMultipleChoice;

    public QuizAdapterAdmin(ArrayList<CauTracNghiem> cauTracNghiems, IonClickItemMultipleChoice ionClickItemMultipleChoice) {
        this.cauTracNghiems = cauTracNghiems;
        this.ionClickItemMultipleChoice = ionClickItemMultipleChoice;
    }

    @NonNull
    @Override
    public QuizAdapterAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multiple_choice_admin, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapterAdmin.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvTitle.setText("Question " + (position + 1));
        holder.edtQuestion.setText(cauTracNghiems.get(position).getNoidung());
        holder.edtAnswer1.setText(cauTracNghiems.get(position).getDapanA());
        holder.edtAnswer2.setText(cauTracNghiems.get(position).getDapanB());
        holder.edtAnswer3.setText(cauTracNghiems.get(position).getDapanC());
        holder.edtAnswer4.setText(cauTracNghiems.get(position).getDapanD());
        holder.edtAnswer.setText(cauTracNghiems.get(position).getDapanTrue());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemMultipleChoice.onClickEdit(cauTracNghiems.get(position), holder.edtQuestion.getText().toString().trim(), holder.edtAnswer1.getText().toString().trim(), holder.edtAnswer2.getText().toString().trim(), holder.edtAnswer3.getText().toString().trim(), holder.edtAnswer4.getText().toString().trim(), holder.edtAnswer.getText().toString().trim());
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemMultipleChoice.onClickDelete(cauTracNghiems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cauTracNghiems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        EditText edtQuestion, edtAnswer1, edtAnswer2, edtAnswer3, edtAnswer4, edtAnswer;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            edtQuestion = itemView.findViewById(R.id.edt_question);
            edtAnswer1 = itemView.findViewById(R.id.edt_answer1);
            edtAnswer2 = itemView.findViewById(R.id.edt_answer2);
            edtAnswer3 = itemView.findViewById(R.id.edt_answer3);
            edtAnswer4 = itemView.findViewById(R.id.edt_answer4);
            edtAnswer = itemView.findViewById(R.id.edt_answer);
            imgEdit = itemView.findViewById(R.id.img_edit);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }
}

interface IonClickItemMultipleChoice {
    void onClickEdit(CauTracNghiem cauTracNghiem, String ques, String a, String b, String c, String d, String ans);

    void onClickDelete(CauTracNghiem cauTracNghiem);
}
