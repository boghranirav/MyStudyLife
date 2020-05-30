package com.example.nirav.mystudylife;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Nirav on 06/11/16.
 */

public class AdapterAcademicTerm extends ArrayAdapter<ClassTermAcademic> {

    Context context;
    public AdapterAcademicTerm(Context context, List<ClassTermAcademic> objects) {
        super(context, R.layout.sch_terms_list, objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.sch_terms_list,null,true);

        TextView txtID=(TextView)rowView.findViewById(R.id.sch_list_id);
        TextView txtNM=(TextView)rowView.findViewById(R.id.sch_list_title);
        TextView txtDate=(TextView)rowView.findViewById(R.id.sch_list_date);

        txtID.setText(String.valueOf(getItem(position).getT_term_id()));
        txtNM.setText(String.valueOf(getItem(position).getT_name().toString()));
        txtDate.setText(String.valueOf(getItem(position).getT_start_date().toString())+" - "+String.valueOf(getItem(position).getT_end_date().toString()));

        return  rowView;
    }
}
