package com.example.nirav.mystudylife;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddExamActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText examDateEtxt;
    private EditText examTimeStart;

    private DatePickerDialog examDatePickerDialog;
    private TimePickerDialog examTimePickerDialogFrom;

    SimpleDateFormat dateFormatter;
    private  int mHour,mMinute;
    my_study_life_dbo db;
    String acadID;
    List<String> listSub=new ArrayList<String>();
    List<ClassSubjects> arraySub=new ArrayList<ClassSubjects>();
    List<Integer> listSubID=new ArrayList<Integer>();
    int subjectID;
    EditText txtModule,txtSDate,txtStartTime,txtDuration,txtRoom,txtSeatNo;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Exam");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("sID","");
        db=new my_study_life_dbo(getApplicationContext());

        arraySub=db.displaySubjectByYear(acadID);

        for(int i=0;i<arraySub.size();i++){
            listSubID.add(arraySub.get(i).getSub_id());
            listSub.add(arraySub.get(i).getSub_name());
        }

        txtModule=(EditText)findViewById(R.id.add_new_exam_module);
        txtSDate=(EditText)findViewById(R.id.add_new_exam_date);
        txtStartTime=(EditText)findViewById(R.id.add_new_exam_time);
        txtDuration=(EditText)findViewById(R.id.add_new_exam_duration);
        txtRoom=(EditText)findViewById(R.id.add_new_exam_seat);
        txtSeatNo=(EditText)findViewById(R.id.add_new_exam_room);
        btnSave=(Button)findViewById(R.id.add_exam_new_save);

        Spinner spinner_sub=(Spinner)findViewById(R.id.add_new_exam_subject);
        ArrayAdapter<String> adapter=new  ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listSub);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter);

        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subjectID=listSubID.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //set date  M-d-YYYY
        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);
        findViewsById();
        setDateTimeField();
        //time
        setTimeField();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtSDate.getText().toString().equals(null)|| txtSDate.getText().toString().equals("")){
                    txtSDate.setError("Select Date");
                }else
                if(txtStartTime.getText().toString().equals(null)|| txtStartTime.getText().toString().equals("")){
                    txtStartTime.setError("Select Time");
                }
                else {
                    ClassExam ce = new ClassExam();

                    ce.setE_subject_id(subjectID);
                    ce.setE_module(txtModule.getText().toString());
                    ce.setE_date(txtSDate.getText().toString());
                    ce.setE_start_time(txtStartTime.getText().toString());
                    ce.setE_duration(txtDuration.getText().toString());
                    ce.setE_room(txtRoom.getText().toString());
                    ce.setE_seat(txtSeatNo.getText().toString());
                    String cdate = DateFormat.getDateTimeInstance().format(new Date());
                    ce.setE_added_on(cdate);
                    db.addNewExam(ce);

                    Intent intent = new Intent(AddExamActivity.this, ExamViewActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void findViewsById() {
        examDateEtxt = (EditText) findViewById(R.id.add_new_exam_date);
        examDateEtxt.setInputType(InputType.TYPE_NULL);
        examDateEtxt.requestFocus();

        examTimeStart =(EditText)findViewById(R.id.add_new_exam_time);
        examTimeStart.setInputType(InputType.TYPE_NULL);

    }

    private void setDateTimeField() {
        examDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        examDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                examDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    private void setTimeField(){
        examTimeStart.setOnClickListener(this);

        Calendar c=Calendar.getInstance();
        mHour=c.get(Calendar.HOUR_OF_DAY);
        mMinute=c.get(Calendar.MINUTE);

        examTimePickerDialogFrom=new TimePickerDialog(this,new  TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                examTimeStart.setText(hourOfDay+":"+minutes);
            }
        },mHour,mMinute, false);


    }


    @Override
    public void onClick(View view) {
        if(view == examDateEtxt) {
            examDatePickerDialog.show();
        }if(view==examTimeStart){
            examTimePickerDialogFrom.show();
        }
    }

}
