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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditClassActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText classDateEtxt;
    private EditText classTimeStart;
    private EditText classTimeEnd;


    private DatePickerDialog classDatePickerDialog;
    private TimePickerDialog classTimePickerDialogFrom;
    private TimePickerDialog classTimePickerDialogTo;

    Button r_time,btnSave,btnDelete;
    EditText txtModule,txtRoom,txtBuilding,txtTeacher,txtCDate,txtTFrom,txtTTo;

    SimpleDateFormat dateFormatter;
    private  int mHour,mMinute;
    Spinner spinner_sub,spinner_time;
    List<String> listSub=new ArrayList<String>();
    List<ClassSubjects> arraySub=new ArrayList<ClassSubjects>();
    List<Integer> listSubID=new ArrayList<Integer>();
    String acadID,classID,classSubID,typeStatus,timeCFrom,timeCTo;
    my_study_life_dbo db;
    TableRow tblRBtn,tblNDate,tblNTime;
    List<ClassClasses> classesList=new ArrayList<ClassClasses>();
    List<ClassSubjects> subjectList=new ArrayList<ClassSubjects>();
    List<ClassClassesTimeMain> classTimeMainList=new ArrayList<ClassClassesTimeMain>();
    List<ClassClassesTimeChild> classTimeChileList=new ArrayList<ClassClassesTimeChild>();
    int timeID;
    ArrayList<String> timeList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Class");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db=new my_study_life_dbo(getApplicationContext());

        SharedPreferences sharedPreferences= getSharedPreferences("academicInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("saTID","");

        SharedPreferences sharedPreferences3= getSharedPreferences("classInfoSP", Context.MODE_PRIVATE);
        classID=sharedPreferences3.getString("classIDSP","");
        classSubID=sharedPreferences3.getString("classsubIDSP","");

        arraySub=db.displaySubjectByYear(acadID);

        for(int i=0;i<arraySub.size();i++){
            listSubID.add(arraySub.get(i).getSub_id());
            listSub.add(arraySub.get(i).getSub_name());
        }

        txtModule=(EditText)findViewById(R.id.class_edit_module);
        txtRoom=(EditText)findViewById(R.id.class_edit_room);
        txtBuilding=(EditText)findViewById(R.id.class_edit_building);
        txtTeacher=(EditText)findViewById(R.id.class_edit_teacher);
        txtCDate=(EditText)findViewById(R.id.class_edit_not_date);
        txtTFrom=(EditText)findViewById(R.id.class_edit_s_time);
        txtTTo=(EditText)findViewById(R.id.class_edit_e_time);
        btnSave=(Button) findViewById(R.id.class_edit_save);
        btnDelete=(Button) findViewById(R.id.class_edit_delete);
        tblRBtn=(TableRow)findViewById(R.id.table_row_btn_time);
        tblNDate=(TableRow)findViewById(R.id.table_row_date_not);
        tblNTime=(TableRow)findViewById(R.id.table_row_s_e_time);
        tblNDate.setVisibility(View.GONE);
        tblNTime.setVisibility(View.GONE);

        classesList=db.displayClassesByClassID(Integer.parseInt(classID));
        txtModule.setText(classesList.get(0).getClass_module());
        txtRoom.setText(classesList.get(0).getClass_room());
        txtBuilding.setText(classesList.get(0).getClass_building());
        txtTeacher.setText(classesList.get(0).getClass_teacher());
        subjectList=db.displaySubjectBySubID(classSubID);

        //for subjects
        spinner_sub=(Spinner)findViewById(R.id.class_edit_subject);
        ArrayAdapter<String> adapter=new  ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listSub);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter);

        spinner_sub.setSelection(adapter.getPosition(subjectList.get(0).getSub_name()));

        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classSubID=listSubID.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        spinner_time=(Spinner)findViewById(R.id.class_new_time_spinner);
        ArrayAdapter<CharSequence> adapter_time=ArrayAdapter.createFromResource(this,R.array.class_repeats,R.layout.support_simple_spinner_dropdown_item);
        adapter_time.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_time.setAdapter(adapter_time);

        classTimeMainList=db.displayMainTimeByCID(classID);
        if(getIntent().hasExtra("editedTimeFrom")){

        }else {
            spinner_time.setSelection(Integer.parseInt(classTimeMainList.get(0).getClass_type()));
        }
        timeID = classTimeMainList.get(0).getTime_id();

        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        typeStatus = "0";
                        tblRBtn.setVisibility(View.VISIBLE);
                        tblNDate.setVisibility(View.GONE);
                        tblNTime.setVisibility(View.GONE);
                        break;
                    case 1:
                        typeStatus = "1";
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

        classTimeChileList=db.displayTimingByCID(classID);

        try{
            if(getIntent().hasExtra("editedTimeFrom")) {
                timeList = getIntent().getExtras().getStringArrayList("editedTime");
                timeCFrom = getIntent().getExtras().getString("editedTimeFrom").toString();
                timeCTo = getIntent().getExtras().getString("editedTimeTo").toString();
                txtModule.setText(timeCFrom+"");
            }else
            {
                if(classTimeChileList.get(0).getC_date()==null){
                    for(int i=0;i<classTimeChileList.size();i++){
                        timeList.add(classTimeChileList.get(i).getDay_name());
                    }
                    timeCFrom=classTimeChileList.get(0).getC_start_time();
                    timeCTo=classTimeChileList.get(0).getC_end_time();
                }
                else {
                    txtCDate.setText(classTimeChileList.get(0).getC_date());
                    txtTFrom.setText(classTimeChileList.get(0).getC_start_time());
                    txtTTo.setText(classTimeChileList.get(0).getC_end_time());
                }
            }


        }catch (Exception e){

        }


        r_time=(Button)findViewById(R.id.class_edit_r_date);
        r_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(EditClassActivity.this,EditTimeActivity.class);
                i.putExtra("timeEditArray",timeList);
                i.putExtra("timeEditFrom",timeCFrom);
                i.putExtra("timeEditTo",timeCTo);
                startActivity(i);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClassClasses cc=new ClassClasses();
                cc.setClass_id(Integer.parseInt(classID));
                cc.setClass_subject_id(Integer.parseInt(classSubID));
                cc.setClass_module(txtModule.getText().toString());
                cc.setClass_room(txtRoom.getText().toString());
                cc.setClass_building(txtBuilding.getText().toString());
                cc.setClass_teacher(txtTeacher.getText().toString());

                if(spinner_time.getSelectedItem().equals("Repeats")){

                    ClassClassesTimeMain ccm=new ClassClassesTimeMain();
                    ccm.setTime_id(timeID);
                    ccm.setT_class_id(Integer.parseInt(classID));
                    ccm.setClass_type("0");

                    ClassClassesTimeChild ccc=new ClassClassesTimeChild();
                    ccc.setDay_id(classTimeChileList.get(0).getDay_id());
                    ccc.setTime_id(timeID);
                    ccc.setC_start_time(timeCFrom);
                    ccc.setC_end_time(timeCTo);

                    db.updateClassAndTime(cc,ccm,ccc,timeList);
                }
                else{
                    ClassClassesTimeMain ccm=new ClassClassesTimeMain();
                    ccm.setTime_id(timeID);
                    ccm.setT_class_id(Integer.parseInt(classID));
                    ccm.setClass_type("1");

                    ClassClassesTimeChild ccc=new ClassClassesTimeChild();
                    ccc.setDay_id(classTimeChileList.get(0).getDay_id());
                    ccc.setTime_id(timeID);
                    ccc.setC_start_time(txtTFrom.getText().toString());
                    ccc.setC_end_time(txtTTo.getText().toString());
                    ccc.setC_date(txtCDate.getText().toString());

                    db.updateClassAndTime(cc,ccm,ccc,null);

                }

                Intent intent=new Intent(EditClassActivity.this,ScheduleActivity.class);
                startActivity(intent);

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteClassTime(classID);
                Intent intent=new Intent(EditClassActivity.this,ScheduleActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViewsById() {
        classDateEtxt = (EditText) findViewById(R.id.class_edit_not_date);
        classDateEtxt.setInputType(InputType.TYPE_NULL);
        classDateEtxt.requestFocus();

        classTimeStart =(EditText)findViewById(R.id.class_edit_s_time);
        classTimeStart.setInputType(InputType.TYPE_NULL);

        classTimeEnd=(EditText)findViewById(R.id.class_edit_e_time);
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
