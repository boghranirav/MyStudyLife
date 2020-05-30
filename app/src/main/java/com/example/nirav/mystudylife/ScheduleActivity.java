package com.example.nirav.mystudylife;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    final Context context=this;
    my_study_life_dbo db;
    List<ClassAcademicYear> academicYears=new ArrayList<ClassAcademicYear>();
    ListView listView1;
    String aID,date_s="",date_f="";
    List<ClassClasses> classList=new ArrayList<ClassClasses>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        db=new my_study_life_dbo(getApplicationContext());
        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        aID=sharedPreferences.getString("sID","");
         date_s=sharedPreferences.getString("sDate","");
         date_f=sharedPreferences.getString("seDate","");

        if(date_f.equals("")){
            academicYears=db.displayAcademicYear();
            aID=String.valueOf(academicYears.get(0).getAcad_id());
            date_s=String.valueOf(academicYears.get(0).getA_start_date());
            date_f=String.valueOf(academicYears.get(0).getS_end_date());

        }
        Date d= null;
        try {
            d = new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(date_s.toString());
            Calendar c=Calendar.getInstance();
            c.setTime(d);
            String yearN=new SimpleDateFormat("yyyy").format(c.getTime());

            Date d2=new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(date_f.toString());
            Calendar c2=Calendar.getInstance();
            c2.setTime(d2);
            String yearN2=new SimpleDateFormat("yyyy").format(c2.getTime());

            if(yearN.equals(yearN2)){
                toolbar.setTitle("Schedule\n"+yearN);
            }
            else {
                toolbar.setTitle("Schedule\n"+yearN+" - "+yearN2);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //txtUser=(TextView)findViewById(R.id.textViewForUser);
        //txtUser.setText("ABC");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(ScheduleActivity.this,ScheduleSelectActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sch_add_classes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ScheduleActivity.this,AddClassesActivity.class);
                SharedPreferences sharedPreferences= getSharedPreferences("academicCInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.putString("saCTID",aID);
                editor.putString("startCTDate",date_s);
                editor.commit();
                startActivity(i);
            }
        });

        listView1=(ListView)findViewById(R.id.list_schedule_class);
        listView1.setAdapter(new AdapterClasses(getApplicationContext(),db.displayClasses(aID)));
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                classList=db.displayClasses(aID);
                SharedPreferences sharedPreferences3= getSharedPreferences("classInfoSP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3=sharedPreferences3.edit();
                editor3.clear();
                editor3.putString("classIDSP",String.valueOf(classList.get(i).getClass_id()));
                editor3.putString("classsubIDSP",String.valueOf(classList.get(i).getClass_subject_id()));
                editor3.commit();
                SharedPreferences sharedPreferences= getSharedPreferences("academicInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.putString("saTID",aID);
                editor.putString("startTDate",date_s);
                editor.putString("endTDate",date_f);
                editor.commit();
                Intent intent=new Intent(ScheduleActivity.this,ViewSingleClassActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedule_menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            SharedPreferences sharedPreferences= getSharedPreferences("academicInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.putString("saTID",aID);
            editor.putString("startTDate",date_s);
            editor.putString("endTDate",date_f);
            editor.commit();
            Intent i=new Intent(ScheduleActivity.this,EditAcademicActivity.class);
            startActivity(i);
            return true;
        }else
        if(id==R.id.action_new){
            Intent i=new Intent(ScheduleActivity.this,NewAcademicYearActivity.class);
            startActivity(i);
            return true;
        }
        else
        if(id==R.id.action_subj)
        {
            SharedPreferences sharedPreferences= getSharedPreferences("academicSubInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.putString("acSubTID",aID);
            editor.commit();
            Intent i=new Intent(ScheduleActivity.this,ViewSubjectsActivity.class);
            startActivity(i);
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }
}
