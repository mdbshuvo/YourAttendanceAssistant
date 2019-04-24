package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeGenerator {

    public static String[] splittedString;
    public static Date date;
    //Tue Nov 06 15:11:54 GMT+08:00 2018

    public static void updateStringArray(){
        date=Calendar.getInstance().getTime();
        String premetiveTime=date.toString();
        splittedString=premetiveTime.split(" ");
    }

    public static String getDateString(){
        updateStringArray();

        String finalString=splittedString[0]+" "+splittedString[1]+" "+splittedString[2]+" "+splittedString[5];
        return finalString;
    }

    public static String getTimeString24(){
        updateStringArray();

        return splittedString[3];
    }

    public static String getTimeString12(){
        updateStringArray();

        String[] splittedTime=splittedString[3].split(":");
        int hour=Integer.parseInt(splittedTime[0]);

        if(hour==0) return splittedString[3].replaceFirst("00","12")+" am";
        else if(hour<12) return splittedString[3]+" am";
        else if(hour==12) return splittedString[3]+" pm";
        else if(hour<22) return splittedString[3].replaceFirst(splittedTime[0],"0"+String.valueOf(hour-12))+" pm";
        else return splittedString[3].replaceFirst(splittedTime[0],String.valueOf(hour-12))+" pm";
    }

    public static int getHour(){
        updateStringArray();
        return date.getHours();
    }

    public static int getMinute(){
        updateStringArray();
        return date.getMinutes();
    }

    public static int getDate(){
        updateStringArray();
        return date.getDate();
    }

    public static String getDay(){
        updateStringArray();
        return splittedString[0];
    }

    public static int getYear(){
        updateStringArray();
        return Integer.parseInt(splittedString[5]);
    }

    public static int getMonth(){
        updateStringArray();
        return date.getMonth()+1;
    }

}
