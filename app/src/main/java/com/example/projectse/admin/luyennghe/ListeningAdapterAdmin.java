package com.example.projectse.admin.luyennghe;

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

public class ListeningAdapterAdmin extends RecyclerView.Adapter<ListeningAdapterAdmin.ViewHolder> {
    private ArrayList<CauLuyenNghe> cauLuyenNghes;
    private IonClickItemListening ionClickItemListening;

    public ListeningAdapterAdmin(ArrayList<CauLuyenNghe> cauLuyenNghes, IonClickItemListening ionClickItemListening) {
        this.cauLuyenNghes = cauLuyenNghes;
        this.ionClickItemListening = ionClickItemListening;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listening_admin, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvTitle.setText("Listening " + (position + 1));
        holder.edtAnswer.setText(cauLuyenNghes.get(position).getDapanTrue());
        holder.edtImgPreview.setText(cauLuyenNghes.get(position).getHinhanh());
        holder.edtAudio.setText(cauLuyenNghes.get(position).getAudio());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemListening.onClickEdit(cauLuyenNghes.get(position), holder.edtAnswer.getText().toString().trim(), holder.edtImgPreview.getText().toString().trim(), holder.edtAudio.getText().toString().trim());
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItemListening.onClickDelete(cauLuyenNghes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cauLuyenNghes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        EditText edtAnswer, edtImgPreview, edtAudio;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            edtAnswer = itemView.findViewById(R.id.edt_answer);
            edtImgPreview = itemView.findViewById(R.id.edt_imgPreview);
            edtAudio = itemView.findViewById(R.id.edt_audio);
            imgEdit = itemView.findViewById(R.id.img_edit);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }
}
interface IonClickItemListening {
    void onClickEdit(CauLuyenNghe cauLuyenNghe, String answer, String imgPreview, String audio);

    void onClickDelete(CauLuyenNghe cauLuyenNghe);
}

