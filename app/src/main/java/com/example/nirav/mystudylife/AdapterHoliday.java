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
 * Created by Nirav on 08/11/16.
 */

public class AdapterHoliday extends ArrayAdapter<ClassHoliday> {

    Context context;
    public AdapterHoliday(Context context, List<ClassHoliday> objects) {
        super(context, R.layout.schedule_holidays, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.schedule_holidays,null,true);

        TextView txtHName=(TextView)rowView.findViewById(R.id.schdule_txtvholiday);
        TextView txtDate=(TextView)rowView.findViewById(R.id.schdule_txt_h_date);
        TextView txtHID=(TextView)rowView.findViewById(R.id.schdule_txtvholiday_id);

        txtHName.setText(getItem(position).getH_name());
        String sD=getItem(position).getH_start_date();
        String eD=getItem(position).getH_end_date();
        if(sD.equals(eD)){
            txtDate.setText(eD);
        }else {
            txtDate.setText(sD+" - "+eD);
        }
        txtHID.setText(String.valueOf(getItem(position).getHoliday_id()));
        return rowView;

    }
}
