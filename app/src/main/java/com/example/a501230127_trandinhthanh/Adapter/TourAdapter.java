package com.example.a501230127_trandinhthanh.Adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.example.a501230127_trandinhthanh.Model.Tour;
import com.example.a501230127_trandinhthanh.R;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class TourAdapter extends BaseAdapter {

    Context context;
    int layoutItem;
    ArrayList<Tour> duLieu;

    public TourAdapter(Context context, int layoutItem, ArrayList<Tour> duLieu) {
        this.context = context;
        this.layoutItem = layoutItem;
        this.duLieu = duLieu;
    }

    @Override
    public int getCount() {
        return duLieu.size();
    }

    @Override
    public Tour getItem(int position) {
        return duLieu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutItem, parent, false);
        }

        ImageView imgHinh = convertView.findViewById(R.id.imgHinh);
        TextView tvMaTour = convertView.findViewById(R.id.tvMaTour);
        TextView tvTenTour = convertView.findViewById(R.id.tvTenTour);
        TextView tvGia = convertView.findViewById(R.id.tvGia);

        Tour tour = duLieu.get(position);
        tvMaTour.setText(tour.MaTour);
        tvTenTour.setText(tour.TenTour);
        tvGia.setText(String.valueOf(tour.Gia));

        String maTour = tour.MaTour.toUpperCase();
        if (maTour.contains("MB")) {
            imgHinh.setImageResource(R.drawable.plane);
        } else {
            imgHinh.setImageResource(R.drawable.xekhach);
        }

        return convertView;
    }
}
