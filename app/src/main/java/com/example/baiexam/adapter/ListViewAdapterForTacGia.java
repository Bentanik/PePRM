package com.example.baiexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.baiexam.R;
import com.example.baiexam.model.TacGia;

import java.util.List;

public class ListViewAdapterForTacGia extends BaseAdapter {
    private Context context;
    private List<TacGia> tacGiaList;

    public ListViewAdapterForTacGia(Context context, List<TacGia> tacGiaList) {
        this.context = context;
        this.tacGiaList = tacGiaList;
    }

    @Override
    public int getCount() {
        return tacGiaList.size();
    }

    @Override
    public Object getItem(int position) {
        return tacGiaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tac_gia, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TacGia tacGia = tacGiaList.get(position);
        holder.txtTenTacGia.setText(tacGia.getTenTacGia());
        holder.txtEmailTacGia.setText("Email: " + tacGia.getEmail());
        holder.txtDiaChiTacGia.setText("Address: " + tacGia.getDiaChi());
        holder.txtDienThoaiTacGia.setText("Phone: " + tacGia.getDienThoai());



        return convertView;
    }

    private static class ViewHolder {
        TextView txtTenTacGia, txtEmailTacGia, txtDiaChiTacGia, txtDienThoaiTacGia;
        ViewHolder(View view) {
            txtTenTacGia = view.findViewById(R.id.txtTenTacGia);
            txtEmailTacGia = view.findViewById(R.id.txtEmailTacGia);
            txtDiaChiTacGia = view.findViewById(R.id.txtDiaChiTacGia);
            txtDienThoaiTacGia = view.findViewById(R.id.txtDienThoaiTacGia);
        }
    }
}
