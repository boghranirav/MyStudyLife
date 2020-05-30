package com.example.nirav.mystudylife;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nirav on 05/11/16.
 */

public class ScheduleDisplayAdapter extends ArrayAdapter<ClassAcademicYear> {

    Context context;

    public ScheduleDisplayAdapter(Context context, List<ClassAcademicYear> objects) {
        super(context, R.layout.layout_schedule_list, objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.layout_schedule_list,null,true);

        TextView txtID=(TextView)rowView.findViewById(R.id.txt_sch_id);
        TextView txtYear=(TextView)rowView.findViewById(R.id.txt_sch_display);
        TextView txtDate=(TextView)rowView.findViewById(R.id.txt_sch_dis_date);

        txtID.setText(String.valueOf(getItem(position).getAcad_id()));

        try {
            Date d=new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(getItem(position).getA_start_date().toString());
            Calendar c=Calendar.getInstance();
            c.setTime(d);
            String yearN=new SimpleDateFormat("yyyy").format(c.getTime());
            String montN=new SimpleDateFormat("MMM dd yyyy").format(c.getTime());

            Date d2=new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(getItem(position).getS_end_date().toString());
            Calendar c2=Calendar.getInstance();
            c2.setTime(d2);
            String yearN2=new SimpleDateFormat("yyyy").format(c2.getTime());
            String montN2=new SimpleDateFormat("MMM dd yyyy").format(c2.getTime());

            if(yearN.equals(yearN2)) {
                txtYear.setText(yearN);
            }
            else {
                txtYear.setText(yearN + " - " + yearN2);

            }
            txtDate.setText(montN+" - "+montN2);

        } catch (ParseException e) {
            e.printStackTrace();
        }




        return rowView;
    }
}
