package com.example.baiexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.baiexam.R;
import com.example.baiexam.model.Sach;

import java.util.List;

public class ListViewAdapterForSach extends BaseAdapter {
    private Context context;
    private List<Sach> sachList;

    public ListViewAdapterForSach(Context context, List<Sach> sachList) {
        this.context = context;
        this.sachList = sachList;
    }

    @Override
    public int getCount() {
        return sachList.size();
    }

    @Override
    public Object getItem(int position) {
        return sachList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sach, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Sach sach = sachList.get(position);
        holder.txtTenSach.setText(sach.getTenSach());
        holder.txtTheLoai.setText("Genre: " + sach.getTheLoai());
        holder.txtNgayXB.setText("Published: " + sach.getNgayXB());

        return convertView;
    }

    static class ViewHolder {
        TextView txtTenSach, txtTheLoai, txtNgayXB;
        ViewHolder(View view) {
            txtTenSach = view.findViewById(R.id.tvBookName);
            txtTheLoai = view.findViewById(R.id.tvBookGenre);
            txtNgayXB = view.findViewById(R.id.tvBookDate);
        }
    }
}
