package com.example.nirav.mystudylife;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nirav on 15/11/16.
 */

public class AdapterClassDash extends ArrayAdapter<ClassClasses> {

        Context context;
    public AdapterClassDash(Context context,List<ClassClasses> objects) {
        super(context, R.layout.schedule_classes, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.schedule_classes,null);

        my_study_life_dbo db=new my_study_life_dbo(context);
        TextView txtColor=(TextView)rowView.findViewById(R.id.schdule_txtvtaskcolor);
        TextView txtSubject=(TextView)rowView.findViewById(R.id.schdule_txtvtasktitle);
        TextView txtTime=(TextView)rowView.findViewById(R.id.schdule_txttime);
        TextView txtID=(TextView)rowView.findViewById(R.id.schdule_class_id);

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

        List<ClassClassesTimeChild> timeList=new ArrayList<ClassClassesTimeChild>();
        timeList=db.displayTimingByCID(String.valueOf(getItem(position).getClass_id()));
        String stime="";
        String dateO = timeList.get(0).getC_start_time();
        String timeEnd=timeList.get(0).getC_end_time();
        String dd = "",dEnd="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12hour = new SimpleDateFormat("hh:mm a");
            Date date = sdf.parse(dateO);
            Date d2=sdf.parse(timeEnd);
            dd = _12hour.format(date);
            dEnd=_12hour.format(d2);

        } catch (Exception e) {

        }

        stime=dd+" to "+dEnd;

        txtTime.setText(stime);

        return rowView;
    }
}
