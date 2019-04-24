package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

class CustomDatabase extends SQLiteOpenHelper{

    static private String name="Admin.db", userTableName ="userData",classesTable="allClasses";
    static private int version=1;
    private Context context;

    public CustomDatabase(Context context) {
        super(context, name, null, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL("CREATE TABLE "+ userTableName +" (signed_in BOOLEAN,username VARCHAR(255) PRIMARY KEY,first_name VARCHAR(255),last_name VARCHAR(255),email VARCHAR(255),institution VARCHAR(255),designation VARCHAR(255)/*,time_zone VARCHAR(255)*/);");
            db.execSQL("CREATE TABLE "+classesTable+" (tableName VARCHAR(255) PRIMARY KEY,tableDes VARCHAR(255),routine VARCHAR(255));");
        }catch (Exception e){
            Toast.makeText(context, "Exception occured : "+e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ userTableName);
        db.execSQL("DROP TABLE IF EXISTS "+classesTable);
        onCreate(db);
    }

    public void clearCurrentUser(){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ userTableName);
        db.execSQL("CREATE TABLE "+ userTableName +" (signed_in BOOLEAN,username VARCHAR(255) PRIMARY KEY,first_name VARCHAR(255),last_name VARCHAR(255),email VARCHAR(255),institution VARCHAR(255),designation VARCHAR(255)/*,time_zone VARCHAR(255)*/);");
    }

    public void clearClassesTable(){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ classesTable);
        db.execSQL("CREATE TABLE "+classesTable+" (tableName VARCHAR(255) PRIMARY KEY,tableDes VARCHAR(255),routine VARCHAR(255));");
    }

    public void insertPartial(String fname, String lname, String email) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("signed_in",false);
        contentValues.put("first_name",fname);
        contentValues.put("last_name",lname);
        contentValues.put("email",email);

        sqLiteDatabase.insert(userTableName,null,contentValues);
    }

    public void insertUser(String username,String fname, String lname, String email,String institution,String designation) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("signed_in",true);
        contentValues.put("username",username);
        contentValues.put("first_name",fname);
        contentValues.put("last_name",lname);
        contentValues.put("email",email);
        contentValues.put("institution",institution);
        contentValues.put("designation",designation);

        sqLiteDatabase.insert(userTableName,null,contentValues);
    }

    public void createClass(String className,String des,String routine){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("tableName",className);
        contentValues.put("tableDes",des);
        contentValues.put("routine",routine);

        sqLiteDatabase.insert(classesTable,null,contentValues);

        Toast.makeText(context, "New class "+className+" is created", Toast.LENGTH_SHORT).show();

        String classNameUpdated=className.replaceAll(" ","_");

        sqLiteDatabase.execSQL("CREATE TABLE "+classNameUpdated+" (roll_no INTEGER PRIMARY KEY,name VARCHAR(255),admitted VARCHAR(255));");
        sqLiteDatabase.execSQL("CREATE TABLE "+classNameUpdated+"_attendanceSheet (dateString VARCHAR(255) PRIMARY KEY);");
    }

    public void insertStudent(String classTableName,String name,int roll){
        String classNameUpdated=classTableName.replaceAll(" ","_");

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("roll_no",roll);
        contentValues.put("name",name);
        contentValues.put("admitted",TimeGenerator.getDateString());

        sqLiteDatabase.insert(classNameUpdated,null,contentValues);

        Toast.makeText(context, "Admission of "+name+" successfull", Toast.LENGTH_SHORT).show();

        sqLiteDatabase.execSQL("CREATE TABLE "+classNameUpdated+"_"+roll+" (dateString VARCHAR(255) PRIMARY KEY,day VARCHAR(255),date INTEGER,month INTEGER,year INTEGER,status VARCHAR(255));");
    }

    public String[] getClasses() {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+classesTable,null);

        String[] classes=new String[cursor.getCount()];
        int i=0;

        while (cursor.moveToNext()){
            classes[i++]=cursor.getString(0);
        }

        return classes;
    }

    public Student[] getStudents(String className) {
        String classNameUpdated=className.replaceAll(" ","_");

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+classNameUpdated,null);

        Student[] students=new Student[cursor.getCount()];
        int i=0;

        while (cursor.moveToNext()){
            students[i]=new Student();
            students[i].roll=cursor.getInt(0);
            students[i].name=cursor.getString(1);
            students[i++].admitted=cursor.getString(2);
        }

        return students;
    }

    public Student getStudent(String className,int roll){
        Student[] students=getStudents(className);

        for(int i=0;i<students.length;i++){
            if(students[i].roll==roll){
                return students[i];
            }
        }

        return null;
    }


    public void setToadaysAttendance(String className, String[] attendanceSheet) {
        String classNameUpdated=className.replaceAll(" ","_");

        Student[] students=getStudents(className);

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();

        for(int i=0;i<students.length;i++){
            String studentTable=classNameUpdated+"_"+String.valueOf(students[i].roll);

            ContentValues contentValues=new ContentValues();
            contentValues.put("dateString",TimeGenerator.getDateString());
            contentValues.put("day",TimeGenerator.getDay());
            contentValues.put("date",TimeGenerator.getDate());
            contentValues.put("month",TimeGenerator.getMonth());
            contentValues.put("year",TimeGenerator.getYear());
            contentValues.put("status",attendanceSheet[i]);

            sqLiteDatabase.insert(studentTable,null,contentValues);
        }

        ContentValues contentValues=new ContentValues();
        contentValues.put("dateString",TimeGenerator.getDateString());

        sqLiteDatabase.insert(classNameUpdated+"_attendanceSheet",null,contentValues);

    }

    public StudentStatuses[] getToadaysAttendance(String className, String dateString) {
        String classNameUpdated=className.replaceAll(" ","_");

        Student[] students=getStudents(className);
        StudentStatuses[] status=new StudentStatuses[students.length];

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();

        for(int i=0;i<students.length;i++){
            String studentTable=classNameUpdated+"_"+String.valueOf(students[i].roll);

            Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+studentTable,null);

            status[i]=new StudentStatuses();
            status[i].name=students[i].name;
            status[i].roll=students[i].roll;

            while (cursor.moveToNext()){
                if(cursor.getString(0).matches(dateString)){
                    status[i].status=cursor.getString(5);
                    break;
                }
            }
        }

        return status;
    }

    public String[] getAttendanceDates(String className){
        String classNameUpdatedWithSheet=className.replaceAll(" ","_")+"_attendanceSheet";

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+classNameUpdatedWithSheet,null);

        String[] attendanceDates=new String[cursor.getCount()];
        if(cursor.getCount()==-1){
            return null;
        }

        int i=0;
        while (cursor.moveToNext()){
            attendanceDates[i++]=cursor.getString(0);
        }

        return attendanceDates;
    }

    public String getRoutine(String className){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+classesTable,null);

        String routine=null;

        while (cursor.moveToNext()){
            if(cursor.getString(0).matches(className)){
                routine=cursor.getString(2);
            }
        }

        return routine;
    }

    public AttenDanceRecord[] getStudentAttendanceRecord(String className, int studentRoll) {
        String classNameUpdated=className.replaceAll(" ","_");
        String studentTableName=classNameUpdated+"_"+String.valueOf(studentRoll);

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+studentTableName,null);

        AttenDanceRecord record[]=new AttenDanceRecord[cursor.getCount()];

        int i=0;
        while (cursor.moveToNext()){
            record[i]=new AttenDanceRecord();

            record[i].dateString=cursor.getString(0);
            record[i].month=cursor.getInt(3);
            record[i++].status=cursor.getString(5);
        }

        return record;
    }

    public void deleteStudent(String className, int roll) {
        String classNameUpdated=className.replace(" ","_");
        String studentTableName=classNameUpdated+"_"+String.valueOf(roll);

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String[] args={String.valueOf(roll)};
        sqLiteDatabase.delete(classNameUpdated,"roll_no = ?",args);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+studentTableName);
    }

    public void deleteClass(String className){

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String[] args={className};
        sqLiteDatabase.delete(classesTable,"tableName = ?",args);

        Student[] students=getStudents(className);
        String classNameUpdated=className.replace(" ","_");

        for(int i=0;i<students.length;i++){
            String studentTableName=classNameUpdated+"_"+String.valueOf(students[i].roll);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+studentTableName);
        }

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+classNameUpdated);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+classNameUpdated+"_attendanceSheet");
    }


    public boolean isSignedIn(){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+classesTable,null);

        cursor.moveToNext();
        return cursor.getExtras().getBoolean("signed_in");
    }

    public boolean isTakenTodaysAttendance(String className) {
        String classNameUpdatedWithSheet=className.replaceAll(" ","_")+"_attendanceSheet";

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+classNameUpdatedWithSheet,null);

        String dateString=TimeGenerator.getDateString();
        while (cursor.moveToNext()){
            if(cursor.getString(0).matches(dateString)){
                return true;
            }
        }

        return false;
    }

    public double getMonthAttendancePercentage(String className, int studentRoll, int position) {
        String classNameUpdated=className.replace(" ","_");
        String studentTableName=classNameUpdated+"_"+String.valueOf(studentRoll);

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+studentTableName,null);

        double present=0;

        while (cursor.moveToNext()){
            if(cursor.getInt(3)==position+1 && cursor.getString(5).matches("present")){
                present++;
            }
        }

        double numberOfTotalClassesThisMonth=getNumberOfTotalClassesThisMonth(className,position);

        if(numberOfTotalClassesThisMonth==0){
            return 0;
        }

        present=present/numberOfTotalClassesThisMonth*100;

        return present;
    }

    private double getNumberOfTotalClassesThisMonth(String className, int position) {
        String classNameUpdatedWithSheet=className.replaceAll(" ","_")+"_attendanceSheet";
        String monthName=context.getResources().getStringArray(R.array.months)[position];

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+classNameUpdatedWithSheet,null);

        double numOfClass=0;

        while (cursor.moveToNext()){
            if (cursor.getString(0).split(" ")[1].matches(monthName)){
                numOfClass++;
            }
        }

        return numOfClass;
    }

    public String getEmail() {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+userTableName,null);

        cursor.moveToNext();
        return cursor.getString(4);
    }

    public String getUserName() {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+userTableName,null);

        cursor.moveToNext();
        String userName=cursor.getString(2)+" "+cursor.getString(3);
        return userName;
    }

    public boolean isRollDuplicate(String className, int roll) {
        String classNameUpdated=className.replace(" ","_");

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+classNameUpdated,null);

        while (cursor.moveToNext()){
            if(cursor.getInt(0)==roll){
                return true;
            }
        }

        return false;
    }

    public void clearAll() {
        clearCurrentUser();

        String[] classes=getClasses();

        for(int i=0;i<classes.length;i++){
            deleteClass(classes[i]);
        }

        clearClassesTable();
    }
}

class Student{
    int roll;
    String name,admitted;
}

class AttenDanceRecord{
    String dateString;
    int month;
    String status;
}