package com.example.nirav.mystudylife;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nirav on 12/11/16.
 */

public class AdapterTaskView extends ArrayAdapter<ClassTasks> {

        Context context;
    public AdapterTaskView(Context context, List<ClassTasks> objects) {
        super(context, R.layout.list_task_view_layout, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.list_task_view_layout,null);
        View rowView2=inflater.inflate(R.layout.layout_null_row,null);

        my_study_life_dbo db=new my_study_life_dbo(context);

        TextView txtTaskID=(TextView)rowView.findViewById(R.id.task_edit_id);
        TextView txtColor=(TextView)rowView.findViewById(R.id.task_subject_color);
        TextView txtTitle=(TextView)rowView.findViewById(R.id.task_subject_title);
        TextView txtDDate=(TextView)rowView.findViewById(R.id.task_subject_date);
        TextView txtSubject=(TextView)rowView.findViewById(R.id.task_subject_name);
        TextView txtComp=(TextView)rowView.findViewById(R.id.task_subject_complete);

        String ddate="";
        SimpleDateFormat sdformate=new SimpleDateFormat("E,MMM dd, yyyy");
        SimpleDateFormat outputDate=new SimpleDateFormat("MMM dd, yyyy");
        try{

            Date date= sdformate.parse(getItem(position).getT_due_date());
            ddate=outputDate.format(date);

        }catch (Exception e){

        }
        Date d=new Date();
        String cdate= outputDate.format(d.getTime());

        Date dateafter=null,datecurrent=null;
        try {
            dateafter= outputDate.parse(ddate);
            datecurrent=outputDate.parse(cdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ((datecurrent.before(dateafter)) || (datecurrent.equals(dateafter))) {

            List<ClassSubjects> subList = new ArrayList<ClassSubjects>();
            subList = db.displaySubjectBySubID(String.valueOf(getItem(position).getT_subject_id()));

            txtTaskID.setText("" + getItem(position).getTask_id());
            int r, g, b;
            r = subList.get(0).getSub_color_red();
            g = subList.get(0).getSub_color_green();
            b = subList.get(0).getSub_color_blue();
            txtColor.setBackgroundColor(Color.rgb(r, g, b));

            txtSubject.setText(subList.get(0).getSub_name() + " " + getItem(position).getT_type());
            txtTitle.setText(getItem(position).getT_title());
            String dateO = getItem(position).getT_due_date();
            String dd = "";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("E,MMM dd, yyyy");
                SimpleDateFormat _12hour = new SimpleDateFormat("dd MMM");
                Date date = sdf.parse(dateO);
                dd = _12hour.format(date);

            } catch (Exception e) {

            }

            txtDDate.setText(dd + "");
            if(getItem(position).getT_type().equals("Reminder")){
                txtComp.setText("");
            }else {
                txtComp.setText(getItem(position).getT_completed() + "%");
            }
            return rowView;
        }
        else {
            return rowView2;
        }

    }
}
