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
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskViewSingleActivity extends AppCompatActivity {

    Button btn_edit;
    String taskID;
    my_study_life_dbo db;
    TextView txtSubTitle,txtSubName,txtDueDate,txtDetail,txtComp,txtCompOn;
    TableLayout subColor;
    SeekBar nPicker;
    List<ClassTasks> arrayTask=new ArrayList<ClassTasks>();
    List<ClassSubjects> arraySubject=new ArrayList<ClassSubjects>();
    List<ClassTasks> arrayTaskCom=new ArrayList<ClassTasks>();
    int progressChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_single);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences3= getSharedPreferences("taskInfo", Context.MODE_PRIVATE);
        taskID=sharedPreferences3.getString("taskID","");
        db=new my_study_life_dbo(getApplicationContext());

        subColor=(TableLayout)findViewById(R.id.task_color_subject);
        txtSubTitle=(TextView)findViewById(R.id.view_task_subject_title);
        txtSubName=(TextView)findViewById(R.id.view_task_subject_name);
        txtDueDate=(TextView)findViewById(R.id.view_task_due_date);
        txtDetail=(TextView)findViewById(R.id.view_task_subject_detail);
        txtComp=(TextView)findViewById(R.id.view_task_completed);
        txtCompOn=(TextView)findViewById(R.id.view_task_completed_on);
        btn_edit=(Button)findViewById(R.id.view_task_edit);
        nPicker=(SeekBar) findViewById(R.id.view_task_comp_status);

        arrayTask=db.displayTaskByTID(taskID);

        arraySubject=db.displaySubjectBySubID(String.valueOf(arrayTask.get(0).getT_subject_id()));
        int r,g,b;
        r=arraySubject.get(0).getSub_color_red();
        g=arraySubject.get(0).getSub_color_green();
        b=arraySubject.get(0).getSub_color_blue();
        subColor.setBackgroundColor(Color.rgb(r,g,b));
        txtCompOn.setTextColor(Color.rgb(r,g,b));
        txtSubTitle.setText("  "+arrayTask.get(0).getT_title());
        txtDueDate.setText(arrayTask.get(0).getT_due_date());
        txtDetail.setText(arrayTask.get(0).getT_detail());
        if (Integer.parseInt(arrayTask.get(0).getT_completed())==100){
            txtCompOn.setVisibility(View.VISIBLE);
            txtCompOn.setText("Completed On "+arrayTask.get(0).getT_completed_on());
        }
        txtComp.setText(arrayTask.get(0).getT_completed()+" %");

        nPicker.setProgress(Integer.parseInt(arrayTask.get(0).getT_completed()));

        nPicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChanged=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                db.updateTaskComplitedByTID(taskID,String.valueOf(progressChanged));
                txtComp.setText(progressChanged+" %");
                if (progressChanged==100){
                    arrayTaskCom=db.displayTaskByTID(taskID);
                    txtCompOn.setVisibility(View.VISIBLE);
                    txtCompOn.setText("Completed On "+arrayTaskCom.get(0).getT_completed_on());
                }else {
                    txtCompOn.setVisibility(View.GONE);
                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TaskViewSingleActivity.this,TaskEditActivity.class);
                startActivity(i);
            }
        });
    }

}
