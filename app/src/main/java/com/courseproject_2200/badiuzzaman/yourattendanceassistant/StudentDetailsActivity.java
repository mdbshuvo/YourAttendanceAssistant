package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class StudentDetailsActivity extends AppCompatActivity {

    private String className;
    private int studentRoll;
    private CustomDatabase database;
    Student student;
    TextView attendanceText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        getSupportActionBar().setTitle("Student Details");

        Bundle bundle=getIntent().getExtras();
        className=bundle.getString("className");
        studentRoll=bundle.getInt("studentRoll");

        database=new CustomDatabase(this);

        student=database.getStudent(className,studentRoll);

        TextView nameText,rollText,admittedText,classText;

        nameText=findViewById(R.id.name_text);
        rollText=findViewById(R.id.roll_text);
        admittedText=findViewById(R.id.admitted_text);
        attendanceText=findViewById(R.id.attendance_text);
        classText=findViewById(R.id.class_text);

        nameText.setText(student.name);
        rollText.setText(String.valueOf(studentRoll));
        admittedText.setText(student.admitted);
        classText.setText(className);

        double percentage=getAttendancePercentage();
        if(percentage==-1){
            attendanceText.setText("No record found");
        }else {
            attendanceText.setText(String.valueOf(percentage)+"%");
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, database.getMonthAttendancePercentage(className,studentRoll,0)),
                new DataPoint(1, database.getMonthAttendancePercentage(className,studentRoll,1)),
                new DataPoint(2, database.getMonthAttendancePercentage(className,studentRoll,2)),
                new DataPoint(3, database.getMonthAttendancePercentage(className,studentRoll,3)),
                new DataPoint(4, database.getMonthAttendancePercentage(className,studentRoll,4)),
                new DataPoint(5, database.getMonthAttendancePercentage(className,studentRoll,5)),
                new DataPoint(6, database.getMonthAttendancePercentage(className,studentRoll,6)),
                new DataPoint(7, database.getMonthAttendancePercentage(className,studentRoll,7)),
                new DataPoint(8, database.getMonthAttendancePercentage(className,studentRoll,8)),
                new DataPoint(9, database.getMonthAttendancePercentage(className,studentRoll,9)),
                new DataPoint(10, database.getMonthAttendancePercentage(className,studentRoll,10)),
                new DataPoint(11, database.getMonthAttendancePercentage(className,studentRoll,11))
        });

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(11);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        //series.setSpacing(50);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
//series.setValuesOnTopSize(50);

        graph.addSeries(series);

        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(getResources().getStringArray(R.array.months));

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private double getAttendancePercentage() {
        double percentage;

        AttenDanceRecord[] totalRecord=database.getStudentAttendanceRecord(className,studentRoll);

        if(totalRecord.length==0) return -1;

        double wasPresent=0;
        for(AttenDanceRecord singleRecord : totalRecord){
            if(singleRecord.status.matches("present")){
                wasPresent++;
            }
        }

        percentage=wasPresent/(double)totalRecord.length*100.0;

        //Toast.makeText(this, "wasPresent :"+wasPresent+"\ntotalRecord : "+totalRecord.length+"\nPercentage : "+percentage, Toast.LENGTH_SHORT).show();

        return percentage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.student_delete_menu){
            AlertDialog.Builder builder=new AlertDialog.Builder(StudentDetailsActivity.this);
            builder.setTitle("Warning");
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage("Are you sure you want to delete this student? This process cannot be undone!");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CustomDatabase database=new CustomDatabase(StudentDetailsActivity.this);

                    database.deleteStudent(className,studentRoll);

                    Toast.makeText(StudentDetailsActivity.this, "Deleted "+student.name, Toast.LENGTH_SHORT).show();
                    setResult(1,new Intent(StudentDetailsActivity.this,StudentsActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        return true;
    }
}
