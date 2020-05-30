package com.example.nirav.mystudylife;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nirav on 08/11/16.
 */

public class AdapterSubjectDisplay extends ArrayAdapter<ClassSubjects> {

        Context context;
    public AdapterSubjectDisplay(Context context, List<ClassSubjects> objects) {
        super(context, R.layout.list_subjects, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.list_subjects,null,true);

        TextView txtID=(TextView)rowView.findViewById(R.id.subject_txt_id);
        TextView txtColor=(TextView)rowView.findViewById(R.id.subject_txtcolor);
        TextView txtSubName=(TextView)rowView.findViewById(R.id.subject_txt_name);

        txtID.setText(String.valueOf(getItem(position).getSub_id()));
        //txtColor.setText(String.valueOf(getItem(position).getSub_color_red()));
        int r,g,b;
        r=getItem(position).getSub_color_red();
        g=getItem(position).getSub_color_green();
        b=getItem(position).getSub_color_blue();
        txtColor.setBackgroundColor(Color.rgb(r,g,b));
        txtSubName.setText(getItem(position).getSub_name());

        return rowView;
    }
}
