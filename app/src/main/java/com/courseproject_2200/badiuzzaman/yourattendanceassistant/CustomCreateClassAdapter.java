package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;

class CustomCreateClassAdapter extends BaseAdapter{

    private Context context;
    private int parent;
    private String[] days;
    private boolean[] checkBoxIsChecked;
    private String[] startTimes;
    private String[] endTimes;


    CustomCreateClassAdapter(Context context,int parent,String[] days){
        this.context=context;
        this.parent=parent;
        this.days=days;

        checkBoxIsChecked=new boolean[days.length];
        for(int i=0;i<days.length;i++) checkBoxIsChecked[i]=false;

        startTimes=new String[days.length];
        endTimes=new  String[days.length];
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getRoutine(){
        StringBuilder stringBuilder=new StringBuilder();

        boolean nothingChecked=true;
        for(int i=0;i<getCount();i++){
            if(checkBoxIsChecked[i]){
                nothingChecked=false;

                stringBuilder.append(days[i],0,3);
                stringBuilder.append(" ");
                stringBuilder.append(startTimes[i]);
                stringBuilder.append(" ");
                stringBuilder.append(endTimes[i]);
                stringBuilder.append(" ");
            }
        }

        if (nothingChecked) return null;

        String routine=stringBuilder.toString();

        return routine;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(parent,viewGroup,false);
        }

        CheckBox checkBox=convertView.findViewById(R.id.create_class_day);
        checkBox.setText(days[position]);
        final Button startTime=convertView.findViewById(R.id.class_start_time_button),endTime=convertView.findViewById(R.id.class_end_time_button);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxIsChecked[position]=true;
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                }
                else {
                    checkBoxIsChecked[position]=false;
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timePicker=new TimePicker(context);

                int currentHour=timePicker.getCurrentHour();
                int currentMinute=timePicker.getCurrentMinute();

                TimePickerDialog timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTimes[position]=String.valueOf(hourOfDay)+":"+String.valueOf(minute);
                        startTime.setText(" START: "+startTimes[position]+" ");
                    }
                },currentHour,currentMinute,true);

                timePickerDialog.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timePicker=new TimePicker(context);

                int currentHour=timePicker.getCurrentHour();
                int currentMinute=timePicker.getCurrentMinute();

                TimePickerDialog timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTimes[position]=String.valueOf(hourOfDay)+":"+String.valueOf(minute);
                        endTime.setText(" END: "+endTimes[position]);
                    }
                },currentHour,currentMinute,true);

                timePickerDialog.show();
            }
        });

        return convertView;
    }
}
