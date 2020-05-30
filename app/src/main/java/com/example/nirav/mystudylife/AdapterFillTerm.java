package com.example.nirav.mystudylife;

import android.content.Context;
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
 * Created by Nirav on 07/11/16.
 */

public class AdapterFillTerm extends ArrayAdapter<ClassTermAcademic> {

    Context context;
    public AdapterFillTerm(Context context, List<ClassTermAcademic> objects) {
        super(context, R.layout.spinner_subject_term, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.spinner_subject_term,null,true);

        TextView txtID=(TextView)rowView.findViewById(R.id.txt_spinner_sub_id);
        TextView txtName=(TextView)rowView.findViewById(R.id.txt_spinner_sub_name);

        txtID.setText(String.valueOf(getItem(position).getT_term_id()));
        txtName.setText(getItem(position).getT_name().toString());

        return rowView;
    }
}
