package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

class CustomHomeGridAdapter extends BaseAdapter{

    Context context;
    int[] icon;
    String[] label;

    CustomHomeGridAdapter(Context context,int[] icon,String[] label){
        this.context=context;
        this.icon=icon;
        this.label=label;
    }

    @Override
    public int getCount() {
        return icon.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.home_grid_view_sample,parent,false);
        }

        ImageView imageView=convertView.findViewById(R.id.home_page_icon);
        TextView textView=convertView.findViewById(R.id.home_page_label);

        imageView.setImageResource(icon[position]);
        textView.setText(label[position]);

        return convertView;
    }
}
