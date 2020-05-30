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

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Nirav on 12/11/16.
 */

public class AdapterExamView extends ArrayAdapter<ClassExam>{

        Context context;
    public AdapterExamView(Context context, List<ClassExam> objects) {
        super(context, R.layout.list_exam_view_layout, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.list_exam_view_layout,null);
        View rowView2=inflater.inflate(R.layout.layout_null_row,null);
        rowView2.setVisibility(View.GONE);


        my_study_life_dbo db=new my_study_life_dbo(context);

        TextView txtEID=(TextView)rowView.findViewById(R.id.exam_view_id);
        TextView txtColor=(TextView)rowView.findViewById(R.id.exam_subject_color);
        TextView txtSubject=(TextView)rowView.findViewById(R.id.exam_subject_title);
        TextView txtDuration=(TextView)rowView.findViewById(R.id.exam_subject_duration);
        TextView txtTime=(TextView)rowView.findViewById(R.id.exam_subject_date_time);

        String ddate="";
        SimpleDateFormat sdformate=new SimpleDateFormat("E,MMM dd, yyyy");
        SimpleDateFormat outputDate=new SimpleDateFormat("MMM dd, yyyy");
        try{

            Date date= sdformate.parse(getItem(position).getE_date());
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
            subList = db.displaySubjectBySubID(String.valueOf(getItem(position).getE_subject_id()));
            int r, g, b;
            r = subList.get(0).getSub_color_red();
            g = subList.get(0).getSub_color_green();
            b = subList.get(0).getSub_color_blue();
            txtColor.setBackgroundColor(Color.rgb(r, g, b));
            txtEID.setText(getItem(position).getExma_id() + "");
            txtDuration.setText(String.valueOf(getItem(position).getE_duration()) + " Min.");
            txtSubject.setText(subList.get(0).getSub_name() + " : " + getItem(position).getE_module());
            String dateO = getItem(position).getE_start_time();
            String dd = "";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12hour = new SimpleDateFormat("hh:mm a");
                Date date = sdf.parse(dateO);
                dd = _12hour.format(date);

            } catch (Exception e) {

            }

            txtTime.setText(dd + " On " + getItem(position).getE_date());
            return rowView;
        }
        else {
           return rowView2;
        }


    }
}
