package com.example.nirav.mystudylife;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAddActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText fromDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;

    EditText txtDueDate,txtTitle,txtDetail;
    Button btnSave;
    String acadID,subjectID,taskType;
    my_study_life_dbo db;
    Spinner spinner_sub,spinner_type;
    List<ClassSubjects> listSubject=new ArrayList<ClassSubjects>();
    List<String> listSub=new ArrayList<String>();
    List<Integer> listSubID=new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("sID","");
        db=new my_study_life_dbo(getApplicationContext());

        txtDueDate=(EditText)findViewById(R.id.task_new_due_date);
        txtTitle=(EditText)findViewById(R.id.task_new_title);
        txtDetail=(EditText)findViewById(R.id.task_new_detail);
        btnSave=(Button) findViewById(R.id.task_new_add);

        listSubject=db.displaySubjectByYear(acadID);

        for(int i=0;i<listSubject.size();i++){
            listSubID.add(listSubject.get(i).getSub_id());
            listSub.add(listSubject.get(i).getSub_name());
        }

        spinner_sub=(Spinner)findViewById(R.id.task_new_subject);
        ArrayAdapter<String> adapter=new  ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listSub);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter);

        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subjectID=String.valueOf(listSubID.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_type=(Spinner)findViewById(R.id.task_new_type);
        ArrayAdapter<CharSequence> adapter_t=ArrayAdapter.createFromResource(this,R.array.task_array,R.layout.support_simple_spinner_dropdown_item);
        adapter_t.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter_t);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:taskType="Assignment";
                        break;
                    case 1:taskType="Reminder";
                        break;
                    case 2:taskType="Revision";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);
        findViewsById();
        setDateTimeField();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtDueDate.getText().toString().equals("")||txtDueDate.getText().toString().equals(null)){
                    txtDueDate.setError("Select Date");
                }
                else {
                    ClassTasks ct = new ClassTasks();
                    ct.setT_subject_id(Integer.parseInt(subjectID));
                    ct.setT_type(taskType);
                    ct.setT_due_date(txtDueDate.getText().toString());
                    ct.setT_title(txtTitle.getText().toString());
                    ct.setT_detail(txtDetail.getText().toString());
                    String cdate = DateFormat.getDateTimeInstance().format(new Date());
                    ct.setT_added_on(cdate);
                    db.addNewTask(ct);

                    Intent intent = new Intent(TaskAddActivity.this, TaskViewActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.task_new_due_date);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        }
    }

}
