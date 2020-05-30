package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleSelectActivity extends AppCompatActivity {

    ListView lv;
    my_study_life_dbo db;
    List<ClassAcademicYear> listAcad=new ArrayList<ClassAcademicYear>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Academic Year/Term");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db=new my_study_life_dbo(getApplicationContext());

        lv=(ListView)findViewById(R.id.list_display_acad_year);

        lv.setAdapter(new ScheduleDisplayAdapter(getApplicationContext(),db.displayAcademicYear()));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                listAcad=db.displayAcademicYear();
                editor.putString("sID",String.valueOf(listAcad.get(i).getAcad_id()));
                editor.putString("sDate",String.valueOf(listAcad.get(i).getA_start_date()));
                editor.putString("seDate",String.valueOf(listAcad.get(i).getS_end_date()));
                editor.commit();

                Intent intent=new Intent(ScheduleSelectActivity.this,ScheduleActivity.class);
                startActivity(intent);
            }
        });

    }

}
