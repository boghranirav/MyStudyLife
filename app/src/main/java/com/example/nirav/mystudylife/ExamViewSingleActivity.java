package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamViewSingleActivity extends AppCompatActivity {

    Button exam_edit;
    String examID;
    my_study_life_dbo db;
    TableLayout btlColor1;
    TextView txtSubject,txtTime,txtDuration,txtLocation,txtSeat;
    Button btnEdit;
    List<ClassExam> arrayExam=new ArrayList<ClassExam>();
    List<ClassSubjects> arraySubject=new ArrayList<ClassSubjects>();
    String subjectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_view_single);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences3= getSharedPreferences("examInfo", Context.MODE_PRIVATE);
        examID=sharedPreferences3.getString("examID","");

        db=new my_study_life_dbo(getApplicationContext());

        btlColor1=(TableLayout)findViewById(R.id.exam_back_sub_color);
        txtSubject=(TextView)findViewById(R.id.view_exam_subject);
        txtTime=(TextView)findViewById(R.id.view_exam_time);
        txtDuration=(TextView)findViewById(R.id.view_exam_duration);
        txtLocation=(TextView)findViewById(R.id.view_exam_building);
        txtSeat=(TextView)findViewById(R.id.view_exam_seat);

        arrayExam=db.displayExamByEID(examID);
        subjectID=String.valueOf(arrayExam.get(0).getE_subject_id());
        arraySubject=db.displaySubjectBySubID(subjectID);
        int r,g,b;
        r=arraySubject.get(0).getSub_color_red();
        g=arraySubject.get(0).getSub_color_green();
        b=arraySubject.get(0).getSub_color_blue();

        String dateO=arrayExam.get(0).getE_start_time();
        String dd="";
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12hour=new SimpleDateFormat("hh:mm a");
            Date date= sdf.parse(dateO);
            dd=_12hour.format(date);

        }catch (Exception e){

        }

        btlColor1.setBackgroundColor(Color.rgb(r,g,b));
        txtSubject.setText("  "+arraySubject.get(0).getSub_name()+" : "+arrayExam.get(0).getE_module());
        txtTime.setText(dd+"\nOn "+arrayExam.get(0).getE_date());
        txtDuration.setText(arrayExam.get(0).getE_duration());
        txtLocation.setText(arrayExam.get(0).getE_room());
        txtSeat.setText(arrayExam.get(0).getE_seat());

        exam_edit=(Button)findViewById(R.id.view_exam_edit);
        exam_edit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i= new Intent(ExamViewSingleActivity.this,ExamEditActivity.class);
                startActivity(i);
            }
        });

    }

}
