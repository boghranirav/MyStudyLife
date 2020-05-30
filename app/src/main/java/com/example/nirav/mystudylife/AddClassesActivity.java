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
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class AddClassesActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText classDateEtxt;
    private EditText classTimeStart;
    private EditText classTimeEnd;


    private DatePickerDialog classDatePickerDialog;
    private TimePickerDialog classTimePickerDialogFrom;
    private TimePickerDialog classTimePickerDialogTo;

    SimpleDateFormat dateFormatter;
    private  int mHour,mMinute;
    Button c_time;
    String acadID;
    my_study_life_dbo db;
    List<String> listSub=new ArrayList<String>();
    List<ClassSubjects> arraySub=new ArrayList<ClassSubjects>();
    List<Integer> listSubID=new ArrayList<Integer>();
    int subjectID;
    Spinner spinner_sub,spinner_time;
    EditText txtModule,txtRoom,txtBuilding,txtTeacher,txtNRDate,txtStartTime,txtEndTime;
    Button btnAddTime,btnSave;
    TableRow tblRBtn,tblNDate,tblNTime;
    ArrayList<String> timeList=new ArrayList<String>();
    String typeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Class");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences= getSharedPreferences("academicCInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("saCTID","");
        db=new my_study_life_dbo(getApplicationContext());

        arraySub=db.displaySubjectByYear(acadID);

        for(int i=0;i<arraySub.size();i++){
            listSubID.add(arraySub.get(i).getSub_id());
            listSub.add(arraySub.get(i).getSub_name());
        }

        txtModule=(EditText)findViewById(R.id.class_new_module);
        txtRoom=(EditText)findViewById(R.id.class_new_room);
        txtBuilding=(EditText)findViewById(R.id.class_new_building);
        txtTeacher=(EditText)findViewById(R.id.class_new_teacher);
        txtNRDate=(EditText)findViewById(R.id.class_new_not_date);
        txtStartTime=(EditText)findViewById(R.id.class_new_s_time);
        txtEndTime=(EditText)findViewById(R.id.class_new_e_time);
        btnAddTime=(Button)findViewById(R.id.class_new_r_time);
        btnSave=(Button)findViewById(R.id.class_new_save);
        tblRBtn=(TableRow)findViewById(R.id.table_row_new_time);
        tblNDate=(TableRow)findViewById(R.id.table_row_not_date);
        tblNTime=(TableRow)findViewById(R.id.table_row_new_date_time);
        tblNDate.setVisibility(View.GONE);
        tblNTime.setVisibility(View.GONE);

        try {
            //spinner_sub.setSelection(subjectID);
            //intent.putExtra("classSID",subjectID);
            txtModule.setText(getIntent().getExtras().getString("classModule").toString());
            txtRoom.setText(getIntent().getExtras().getString("classRoom").toString());
            txtBuilding.setText(getIntent().getExtras().getString("classBuilding").toString());
            txtTeacher.setText(getIntent().getExtras().getString("classTeacher").toString());

        }catch (Exception e){

        }

        //for subjects
        spinner_sub=(Spinner)findViewById(R.id.class_new_subject);
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

        //for timings
        spinner_time=(Spinner)findViewById(R.id.class_new_time);
        ArrayAdapter<CharSequence> adapter_time=ArrayAdapter.createFromResource(this,R.array.class_repeats,R.layout.support_simple_spinner_dropdown_item);
        adapter_time.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_time.setAdapter(adapter_time);

        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 0:
                        typeStatus="0";
                        tblRBtn.setVisibility(View.VISIBLE);
                        tblNDate.setVisibility(View.GONE);
                        tblNTime.setVisibility(View.GONE);
                        break;
                    case 1:
                        typeStatus="1";
                        tblRBtn.setVisibility(View.GONE);
                        tblNDate.setVisibility(View.VISIBLE);
                        tblNTime.setVisibility(View.VISIBLE);
                        break;
                }
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


        c_time=(Button)findViewById(R.id.class_new_r_time);
        c_time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String sd="";
                String ed="";
                try {
                    timeList=getIntent().getExtras().getStringArrayList("timeArray");
                    sd=getIntent().getExtras().getString("timeFrom");
                    ed=getIntent().getExtras().getString("timeTo");

                }catch (Exception e){

                }
                Intent intent=new Intent(AddClassesActivity.this,ClassTimeActivity.class);
                intent.putExtra("classSID",subjectID);
                intent.putExtra("classModule",txtModule.getText().toString());
                intent.putExtra("classRoom",txtRoom.getText().toString());
                intent.putExtra("classBuilding",txtBuilding.getText().toString());
                intent.putExtra("classTeacher",txtTeacher.getText().toString());
                intent.putExtra("timeArray",timeList);
                intent.putExtra("timeFrom",sd);
                intent.putExtra("timeTo",ed);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typeStatus.equals("0")){
                    Boolean b=false;

                    try {
                        if (getIntent().getExtras().containsKey("timeArray")) {
                            b = true;
                        }
                        else
                        {
                            b=false;
                        }
                    }catch (Exception e){

                    }

                    if(b==false) {
                        Toast.makeText(getApplicationContext(),"Select Date And Time",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        ClassClasses cc=new ClassClasses();

                        cc.setClass_building(txtBuilding.getText().toString());
                        cc.setClass_module(txtModule.getText().toString());
                        cc.setClass_room(txtRoom.getText().toString());
                        cc.setClass_teacher(txtTeacher.getText().toString());
                        cc.setClass_subject_id(subjectID);

                        ClassClassesTimeChild ctc=new ClassClassesTimeChild();
                        ctc.setC_start_time(getIntent().getExtras().getString("timeFrom").toString());
                        ctc.setC_end_time(getIntent().getExtras().getString("timeTo").toString());

                        db.addClass(cc, typeStatus, ctc, getIntent().getExtras().getStringArrayList("timeArray"));
                        Intent intent = new Intent(AddClassesActivity.this, ScheduleActivity.class);
                        startActivity(intent);
                    }

                }else if(typeStatus.equals("1")){

                    if(txtNRDate.getText().toString().equals("") || txtNRDate.getText().toString().equals(null)){
                        txtNRDate.setError("Select Date");
                    }
                    else
                    if(txtStartTime.getText().toString().equals("") || txtStartTime.getText().toString().equals(null)){
                        txtStartTime.setError("Select Starting Date");
                    }
                    else
                    if(txtEndTime.getText().toString().equals("") || txtEndTime.getText().toString().equals(null)){
                        txtEndTime.setError("Select Date");
                    }
                    else {
                        ClassClasses cc = new ClassClasses();
                        cc.setClass_building(txtBuilding.getText().toString());
                        cc.setClass_module(txtModule.getText().toString());
                        cc.setClass_room(txtModule.getText().toString());
                        cc.setClass_teacher(txtModule.getText().toString());
                        cc.setClass_subject_id(subjectID);

                        ClassClassesTimeChild ctc = new ClassClassesTimeChild();
                        ctc.setC_date(txtNRDate.getText().toString());
                        ctc.setC_start_time(txtStartTime.getText().toString());
                        ctc.setC_end_time(txtEndTime.getText().toString());


                        db.addClass(cc, typeStatus, ctc, null);
                        Intent intent = new Intent(AddClassesActivity.this, ScheduleActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


    }

    private void findViewsById() {
        classDateEtxt = (EditText) findViewById(R.id.class_new_not_date);
        classDateEtxt.setInputType(InputType.TYPE_NULL);
        classDateEtxt.requestFocus();

        classTimeStart =(EditText)findViewById(R.id.class_new_s_time);
        classTimeStart.setInputType(InputType.TYPE_NULL);

        classTimeEnd=(EditText)findViewById(R.id.class_new_e_time);
        classTimeEnd.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        classDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        classDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                classDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    private void setTimeField(){
        classTimeStart.setOnClickListener(this);
        classTimeEnd.setOnClickListener(this);

        Calendar c=Calendar.getInstance();
        mHour=c.get(Calendar.HOUR_OF_DAY);
        mMinute=c.get(Calendar.MINUTE);

        classTimePickerDialogFrom=new TimePickerDialog(this,new  TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                classTimeStart.setText(hourOfDay+":"+minutes);
            }
        },mHour,mMinute, false);


        classTimePickerDialogTo=new TimePickerDialog(this,new  TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                classTimeEnd.setText(hourOfDay+":"+minutes);
            }
        },mHour,mMinute, false);

    }


    @Override
    public void onClick(View view) {
        if(view == classDateEtxt) {
            classDatePickerDialog.show();
        }if(view==classTimeStart){
            classTimePickerDialogFrom.show();
        }if(view==classTimeEnd){
            classTimePickerDialogTo.show();;
        }
    }

}
