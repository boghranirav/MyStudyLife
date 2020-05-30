package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewSingleClassActivity extends AppCompatActivity {

    Button btn_edit;
    String classID,classSubID,aID,date_s,date_f;
    my_study_life_dbo db;
    TextView  txtSubjectN,txtAcademic,txtTime,txtBuilding,txtTeacher;
    List<ClassClasses> classList=new ArrayList<ClassClasses>();
    List<ClassSubjects> subNameList=new ArrayList<ClassSubjects>();
    List<ClassClassesTimeChild> timeList=new ArrayList<ClassClassesTimeChild>();
    TableLayout tblView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db=new my_study_life_dbo(getApplicationContext());
        SharedPreferences sharedPreferences3= getSharedPreferences("classInfoSP", Context.MODE_PRIVATE);
        classID=sharedPreferences3.getString("classIDSP","");
        classSubID=sharedPreferences3.getString("classsubIDSP","");


        SharedPreferences sharedPreferences= getSharedPreferences("academicInfo", Context.MODE_PRIVATE);
        aID=sharedPreferences.getString("saTID","");
        date_s=sharedPreferences.getString("startTDate","");
        date_f=sharedPreferences.getString("endTDate","");

        txtSubjectN=(TextView)findViewById(R.id.view_class_subject);
        txtAcademic=(TextView)findViewById(R.id.view_class_schedule);
        txtTime=(TextView)findViewById(R.id.view_class_time);
        txtBuilding=(TextView)findViewById(R.id.view_class_building);
        txtTeacher=(TextView)findViewById(R.id.view_class_teacher);
        tblView=(TableLayout)findViewById(R.id.table_classes_1);

        classList=db.displayClassesByClassID(Integer.parseInt(classID));
        subNameList=db.displaySubjectBySubID(String.valueOf(classSubID));
        timeList=db.displayTimingByCID(String.valueOf(classID));

        //Bacground COlor
        int r,g,b;
        r=subNameList.get(0).getSub_color_red();
        g=subNameList.get(0).getSub_color_green();
        b=subNameList.get(0).getSub_color_blue();
        tblView.setBackgroundColor(Color.rgb(r,g,b));
        txtSubjectN.setText("  "+subNameList.get(0).getSub_name()+" : "+classList.get(0).getClass_module());

        final DateFormat dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy");
        final Calendar c1 = Calendar.getInstance();
        final Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(dateFormatter.parse(date_f));
            c2.setTime(dateFormatter.parse(date_s));
            txtAcademic.setText("  "+c1.get(Calendar.YEAR)+"-"+c2.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtBuilding.setText(classList.get(0).getClass_building());
        txtTeacher.setText(classList.get(0).getClass_teacher());
        String stime="";
        String sD=timeList.get(0).getC_date();
        stime=timeList.get(0).getC_start_time()+"-"+timeList.get(0).getC_end_time()+"\n";
        if(timeList.get(0).getC_date() == null) {
            for (int i = 0; i < timeList.size(); i++) {
                stime += " " + timeList.get(i).getDay_name();
            }
        }else {
            stime += " " + timeList.get(0).getC_date();
        }

        txtTime.setText(stime);


        btn_edit=(Button)findViewById(R.id.view_class_task_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ViewSingleClassActivity.this,EditClassActivity.class);
                startActivity(i);
            }
        });
    }

}
