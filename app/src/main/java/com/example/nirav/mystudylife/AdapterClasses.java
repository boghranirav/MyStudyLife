package com.example.nirav.mystudylife;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirav on 10/11/16.
 */

public class AdapterClasses extends ArrayAdapter<ClassClasses> {

    Context context;
    public AdapterClasses(Context context,List<ClassClasses> objects) {
        super(context, R.layout.layout_classes_for_schedule, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.layout_classes_for_schedule,null,true);

        my_study_life_dbo db=new my_study_life_dbo(context);

        TextView txtColor=(TextView)rowView.findViewById(R.id.view_class_subject_color);
        TextView txtID=(TextView)rowView.findViewById(R.id.view_class_subject_id);
        TextView txtSubject=(TextView)rowView.findViewById(R.id.view_class_subject_name);
        TextView txtTeacher=(TextView)rowView.findViewById(R.id.view_class_subject_teacher);
        TextView txtTime=(TextView)rowView.findViewById(R.id.view_class_subject_time);

        List<ClassSubjects> subList=new ArrayList<ClassSubjects>();
        subList=db.displaySubjectBySubID(String.valueOf(getItem(position).getClass_subject_id()));
        int r,g,b;
        r=subList.get(0).getSub_color_red();
        g=subList.get(0).getSub_color_green();
        b=subList.get(0).getSub_color_blue();
        txtColor.setBackgroundColor(Color.rgb(r,g,b));

        txtID.setText(String.valueOf(getItem(position).getClass_id()));
        if((getItem(position).getClass_module().toString()).equals(null)){
            txtSubject.setText(subList.get(0).getSub_name());
        }else {
            txtSubject.setText(subList.get(0).getSub_name() + " : " + getItem(position).getClass_module());
        }
        txtTeacher.setText(getItem(position).getClass_teacher());

        List<ClassClassesTimeChild> timeList=new ArrayList<ClassClassesTimeChild>();
        timeList=db.displayTimingByCID(String.valueOf(getItem(position).getClass_id()));
        String stime="";
        stime=timeList.get(0).getC_start_time()+" to "+timeList.get(0).getC_end_time();
        if(timeList.get(0).getC_date()==null) {
            for (int i = 0; i < timeList.size(); i++) {
                stime += " " + timeList.get(i).getDay_name();
            }
        }
        else
        {
            stime += " " + timeList.get(0).getC_date();
        }
        txtTime.setText(stime);

        return rowView;
    }
}
