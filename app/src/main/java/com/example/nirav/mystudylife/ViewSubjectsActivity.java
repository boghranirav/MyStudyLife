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

public class ViewSubjectsActivity extends AppCompatActivity {

    my_study_life_dbo db;
    ListView listSub;
    String aID;
    List<ClassSubjects> arraySubject=new ArrayList<ClassSubjects>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subjects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Subjects");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ViewSubjectsActivity.this,AddSubjectActivity.class);
                startActivity(i);
            }
        });

        db=new my_study_life_dbo(getApplicationContext());
        listSub=(ListView)findViewById(R.id.shc_list_subjects);
        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        aID=sharedPreferences.getString("sID","");
        listSub.setAdapter(new AdapterSubjectDisplay(getApplicationContext(),db.displaySubjectByYear(aID)));

        listSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                arraySubject=db.displaySubjectByYear(aID);
                SharedPreferences sharedPreferences= getSharedPreferences("SubjectEditInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.putString("SubEditID",String.valueOf(arraySubject.get(i).getSub_id()));
                editor.putString("SubEditName",arraySubject.get(i).getSub_name());
                editor.putString("SubEditTermID",String.valueOf(arraySubject.get(i).getSub_term_id()));
                editor.putString("SubEditColorR",String.valueOf(arraySubject.get(i).getSub_color_red()));
                editor.putString("SubEditColorG",String.valueOf(arraySubject.get(i).getSub_color_green()));
                editor.putString("SubEditColorB",String.valueOf(arraySubject.get(i).getSub_color_blue()));
                editor.commit();

                Intent intent2=new Intent(ViewSubjectsActivity.this,EditSubjectActivity.class);
                startActivity(intent2);
            }
        });
    }

}
