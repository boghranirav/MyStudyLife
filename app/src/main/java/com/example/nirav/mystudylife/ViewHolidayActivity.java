package com.example.nirav.mystudylife;

import android.content.Intent;
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

public class ViewHolidayActivity extends AppCompatActivity {

    ListView listHoliday;
    my_study_life_dbo db;
    List<ClassHoliday> arrayHoliday=new ArrayList<ClassHoliday>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_holiday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Holiday");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listHoliday=(ListView)findViewById(R.id.list_holiday_info);
        db=new my_study_life_dbo(getApplicationContext());
        listHoliday.setAdapter(new AdapterHoliday(getApplicationContext(),db.displayHoliday()));

        listHoliday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                arrayHoliday=db.displayHoliday();
                Intent intent=new Intent(ViewHolidayActivity.this,EditHolidayActivity.class);
                intent.putExtra("hID",String.valueOf(arrayHoliday.get(i).getHoliday_id()));
                intent.putExtra("hName",arrayHoliday.get(i).getH_name());
                intent.putExtra("hSDate",arrayHoliday.get(i).getH_start_date());
                intent.putExtra("hEDate",arrayHoliday.get(i).getH_end_date());
                startActivity(intent);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewHolidayActivity.this,AddHolidayActivity.class);
                startActivity(intent);
            }
        });
    }

}
