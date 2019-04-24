package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

class CustomClassStudentsEditAdapter extends BaseAdapter{

    private Context context;
    private int parent;
    private Student[] students;
    private String className;
    private boolean canEdit;

    public CustomClassStudentsEditAdapter(Context context, int parent, Student[] students, String className, boolean canEdit) {
        this.context=context;
        this.parent=parent;
        this.students=students;
        this.className=className;
        this.canEdit=canEdit;
    }

    @Override
    public int getCount() {
        return students.length;
    }

    @Override
    public Object getItem(int position) {
        return students[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(parent,viewGroup,false);
        }

        TextView roll,name;
        final Button delete;

        roll=convertView.findViewById(R.id.student_roll);
        name=convertView.findViewById(R.id.student_name);
        delete=convertView.findViewById(R.id.student_delete);

        roll.setText(String.valueOf(students[position].roll));
        name.setText(students[position].name);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Warning");
                builder.setIcon(R.drawable.ic_warning_black_24dp);
                builder.setMessage("Are you sure you want to delete this student? This process cannot be undone!");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomDatabase database=new CustomDatabase(context);

                        database.deleteStudent(className,students[position].roll);
                        delete.setText("deleted");
                        delete.setBackgroundColor(Color.GRAY);

                        Toast.makeText(context, "Deleted "+students[position].name, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
        delete.setEnabled(canEdit);

        return convertView;
    }
}
