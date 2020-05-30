package com.example.nirav.mystudylife;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class EditTimeActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText classTimeStart;
    private EditText classTimeEnd;

    private TimePickerDialog classTimePickerDialogFrom;
    private TimePickerDialog classTimePickerDialogTo;

    private  int mHour,mMinute;

    CheckBox chSun,chMon,chTue,chWed,chThu,chFri,chSat;
    EditText txtFrom,txtTo;
    Button btnSave;
    ArrayList<String> daysList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Time");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewsById();
        //time
        setTimeField();

        chSun=(CheckBox)findViewById(R.id.class_edit_sunday);
        chMon=(CheckBox)findViewById(R.id.class_edit_monday);
        chTue=(CheckBox)findViewById(R.id.class_edit_tuesday);
        chWed=(CheckBox)findViewById(R.id.class_edit_wednesday);
        chThu=(CheckBox)findViewById(R.id.class_edit_thursday);
        chFri=(CheckBox)findViewById(R.id.class_edit_friday);
        chSat=(CheckBox)findViewById(R.id.class_edit_saturday);
        txtFrom=(EditText) findViewById(R.id.class_edit_time_s);
        txtTo=(EditText) findViewById(R.id.class_edit_time_e);
        btnSave=(Button) findViewById(R.id.class_edit_time_save);

        try {
            daysList=getIntent().getExtras().getStringArrayList("timeEditArray");
            if(daysList.contains("Sunday")){
                chSun.setChecked(true);
            }
            if(daysList.contains("Monday")){
                chMon.setChecked(true);
            }
            if(daysList.contains("Tuesday")){
                chTue.setChecked(true);
            }
            if(daysList.contains("Wednesday")){
                chWed.setChecked(true);
            }
            if(daysList.contains("Thursday")){
                chThu.setChecked(true);
            }
            if(daysList.contains("Friday")){
                chFri.setChecked(true);
            }
            if(daysList.contains("Saturday")){
                chSat.setChecked(true);
            }
            txtFrom.setText(getIntent().getExtras().getString("timeEditFrom").toString());
            txtTo.setText(getIntent().getExtras().getString("timeEditTo").toString());
        }catch (Exception e){

        }


        chSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chSun.isChecked()){
                    daysList.add("Sunday");
                }else {
                    daysList.remove("Sunday");
                }
            }
        });

        chMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chMon.isChecked()){
                    daysList.add("Monday");
                }else {
                    daysList.remove("Monday");
                }
            }
        });

        chTue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chTue.isChecked()){
                    daysList.add("Tuesday");
                }else {
                    daysList.remove("Tuesday");
                }
            }
        });
        chWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chWed.isChecked()){
                    daysList.add("Wednesday");
                }else {
                    daysList.remove("Wednesday");
                }
            }
        });
        chThu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chThu.isChecked()){
                    daysList.add("Thursday");
                }else {
                    daysList.add("Thursday");
                }
            }
        });
        chFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chFri.isChecked()){
                    daysList.add("Friday");
                }else {
                    daysList.remove("Friday");
                }
            }
        });
        chSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chSat.isChecked()){
                    daysList.add("Saturday");
                }else {
                    daysList.remove("Saturday");
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditTimeActivity.this,EditClassActivity.class);
                intent.putExtra("editedTime",daysList);
                intent.putExtra("editedTimeFrom",txtFrom.getText().toString());
                intent.putExtra("editedTimeTo",txtTo.getText().toString());
                startActivity(intent);

            }
        });

    }

    private void findViewsById() {

        classTimeStart =(EditText)findViewById(R.id.class_edit_time_s);
        classTimeStart.setInputType(InputType.TYPE_NULL);

        classTimeEnd=(EditText)findViewById(R.id.class_edit_time_e);
        classTimeEnd.setInputType(InputType.TYPE_NULL);
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
       if(view==classTimeStart){
            classTimePickerDialogFrom.show();
        }if(view==classTimeEnd){
            classTimePickerDialogTo.show();;
        }
    }
}
