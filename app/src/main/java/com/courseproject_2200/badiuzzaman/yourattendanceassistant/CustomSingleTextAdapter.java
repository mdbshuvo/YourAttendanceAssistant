package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


class CustomSingleTextAdapter extends ArrayAdapter<String>{

    String[] data;
    Context context;
    int resource,textViewId;
    ArrayList<String> mOriginalValues;
    List<String> mObjects;


    public CustomSingleTextAdapter(@NonNull Context context, int resource, int textViewResourceId,String[] data) {
        super(context, resource, textViewResourceId, data);
        this.context=context;
        this.resource=resource;
        this.textViewId=textViewResourceId;
        this.data=data;
        mObjects= Arrays.asList(data);
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(resource,parent,false);
        }

        TextView textView=convertView.findViewById(textViewId);
        textView.setText(mObjects.get(position));

        return convertView;
    }


    public void performFiltering(String prefix) {
        //Toast.makeText(context, "Performing filtering", Toast.LENGTH_SHORT).show();

        if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<String>(mObjects);
        }

        if (prefix == null || prefix.length() == 0) {
            final ArrayList<String> list;
            list = new ArrayList<String>(mOriginalValues);
            mObjects = list;
        } else {
            final String prefixString = prefix.toLowerCase();

            final ArrayList<String> values;
            values = new ArrayList<String>(mOriginalValues);

            final int count = values.size();
            final ArrayList<String> newValues = new ArrayList<String>();

            for (int i = 0; i < count; i++) {
                final String value = values.get(i);
                final String valueText = value.toLowerCase();

                // First match against the whole, non-splitted value
                if (valueText.startsWith(prefixString)) {
                    newValues.add(value);
                } else {
                    final String[] words = valueText.split(" ");
                    for (String word : words) {
                        if (word.startsWith(prefixString)) {
                            newValues.add(value);
                            break;
                        }
                    }
                }
            }

            mObjects=newValues;

            for(int i=0;i<newValues.size();i++)
                Toast.makeText(context, mObjects.get(i), Toast.LENGTH_SHORT).show();
        }

        notifyDataSetChanged();
    }

}
