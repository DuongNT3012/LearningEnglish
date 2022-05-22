package com.example.projectse.admin.bohoctap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectse.R;

import java.util.List;

public class BoHocTapAdapterAdmin extends BaseAdapter {
    private Context context;
    private int layout;
    private List<BoHocTapAdmin> boHocTapAdminList;

    public BoHocTapAdapterAdmin(Context context, int layout, List<BoHocTapAdmin> boHocTapAdminList) {
        this.context = context;
        this.layout = layout;
        this.boHocTapAdminList = boHocTapAdminList;
    }

    @Override
    public int getCount() {
        return boHocTapAdminList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtTenBo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BoHocTapAdapterAdmin.ViewHolder holder;
        if(convertView == null){
            holder = new BoHocTapAdapterAdmin.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtTenBo = (TextView) convertView.findViewById(R.id.tvTenBo);
            convertView.setTag(holder);
        }else {
            holder = (BoHocTapAdapterAdmin.ViewHolder) convertView.getTag();
        }
        BoHocTapAdmin BoHT = boHocTapAdminList.get(position);
        holder.txtTenBo.setText(BoHT.getTenBo());
        return convertView;
    }
}
