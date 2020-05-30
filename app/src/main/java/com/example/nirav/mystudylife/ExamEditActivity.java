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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExamEditActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText examDateEtxt;
    private EditText examTimeStart;

    private DatePickerDialog examDatePickerDialog;
    private TimePickerDialog examTimePickerDialogFrom;

    SimpleDateFormat dateFormatter;
    private  int mHour,mMinute;

    List<String> listSub=new ArrayList<String>();
    List<ClassSubjects> arraySub=new ArrayList<ClassSubjects>();
    List<Integer> listSubID=new ArrayList<Integer>();
    String examID,acadID;
    my_study_life_dbo db;
    Spinner spinner_sub;
    EditText txtModule,txtSDate,txtSTime,txtDuration,txtSeatNo,txtRoom;
    Button btnSave,btnDelete;
    List<ClassExam> arrayExam=new ArrayList<ClassExam>();
    String subjectID;
    List<ClassSubjects> arraySubForSpin=new ArrayList<ClassSubjects>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Exam");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences3= getSharedPreferences("examInfo", Context.MODE_PRIVATE);
        examID=sharedPreferences3.getString("examID","");

        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("sID","");

        db=new my_study_life_dbo(getApplicationContext());

        arraySub=db.displaySubjectByYear(acadID);

        for(int i=0;i<arraySub.size();i++){
            listSubID.add(arraySub.get(i).getSub_id());
            listSub.add(arraySub.get(i).getSub_name());
        }

        arrayExam=db.displayExamByEID(examID);
        txtModule=(EditText)findViewById(R.id.edit_exam_module);
        txtSDate=(EditText)findViewById(R.id.edit_exam_date);
        txtSTime=(EditText)findViewById(R.id.edit_exam_time);
        txtDuration=(EditText)findViewById(R.id.edit_exam_duration);
        txtSeatNo=(EditText)findViewById(R.id.edit_exam_seat);
        txtRoom=(EditText)findViewById(R.id.edit_exam_room);
        btnSave=(Button)findViewById(R.id.edit_exam_save);
        btnDelete=(Button)findViewById(R.id.edit_exam_delete);

        txtModule.setText(arrayExam.get(0).getE_module());
        subjectID=String.valueOf(arrayExam.get(0).getE_subject_id());
        txtSDate.setText(arrayExam.get(0).getE_date());
        txtSTime.setText(arrayExam.get(0).getE_start_time());
        txtDuration.setText(arrayExam.get(0).getE_duration());
        txtSeatNo.setText(arrayExam.get(0).getE_seat());
        txtRoom.setText(arrayExam.get(0).getE_room());

        spinner_sub=(Spinner)findViewById(R.id.edit_exam_subject);
        ArrayAdapter<String> adapter=new  ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listSub);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter);

        arraySubForSpin=db.displaySubjectBySubID(subjectID);
        spinner_sub.setSelection(adapter.getPosition(arraySubForSpin.get(0).getSub_name()));

        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subjectID=listSubID.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //set date
        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);
        findViewsById();
        setDateTimeField();
        //time
        setTimeField();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtSDate.getText().toString().equals("")||txtSDate.getText().toString().equals(null)){
                    txtSDate.setError("Select Date");
                }else
                if (txtSTime.getText().toString().equals("")||txtSTime.getText().toString().equals(null))
                {
                    txtSTime.setError("Select Time");
                }
                    else
                {
                    ClassExam ce = new ClassExam();
                    ce.setExma_id(Integer.parseInt(examID));
                    ce.setE_subject_id(Integer.parseInt(subjectID));
                    ce.setE_module(txtModule.getText().toString());
                    ce.setE_date(txtSDate.getText().toString());
                    ce.setE_start_time(txtSTime.getText().toString());
                    ce.setE_duration(txtDuration.getText().toString());
                    ce.setE_room(txtRoom.getText().toString());
                    ce.setE_seat(txtSeatNo.getText().toString());
                    db.updateExamByEID(ce);
                    Intent intent = new Intent(ExamEditActivity.this, ExamViewSingleActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteExamByEID(examID);
                Intent intent=new Intent(ExamEditActivity.this,ExamViewActivity.class);
                startActivity(intent);
            }
        });

    }
    private void findViewsById() {
        examDateEtxt = (EditText) findViewById(R.id.edit_exam_date);
        examDateEtxt.setInputType(InputType.TYPE_NULL);
        examDateEtxt.requestFocus();

        examTimeStart =(EditText)findViewById(R.id.edit_exam_time);
        examTimeStart.setInputType(InputType.TYPE_NULL);

    }

    private void setDateTimeField() {
        examDateEtxt.setOnClickListener(this);

        Date d= null;
        try {
            d = new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(txtSDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(d);

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

        Date d= null;
        try {
            d = new SimpleDateFormat("H:mm", Locale.ENGLISH).parse(txtSTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c=Calendar.getInstance();
        c.setTime(d);
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
