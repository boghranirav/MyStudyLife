package com.example.nirav.mystudylife;



import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewAcademicYearActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    SimpleDateFormat dateFormatter;

    Button btn_save;
    EditText txt_f_date,txt_t_date;
    my_study_life_dbo db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_academic_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Academic Year");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);

        findViewsById();

        setDateTimeField();

        db=new my_study_life_dbo(getApplicationContext());
        txt_f_date=(EditText)findViewById(R.id.add_new_sch_fromdate);
        txt_t_date=(EditText)findViewById(R.id.add_new_sch_todate);
        btn_save=(Button)findViewById(R.id.add_new_sch_btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_f_date.getText().toString().equals("")||txt_f_date.getText().toString().equals(null)){
                    txt_f_date.setError("Select Date");
                }else
                    if(txt_t_date.getText().toString().equals("")||txt_t_date.getText().toString().equals(null)){
                        txt_t_date.setError("Select Date");
                    }
                else {
                        ClassAcademicYear ca = new ClassAcademicYear();
                        ca.setA_start_date(txt_f_date.getText().toString());
                        ca.setS_end_date(txt_t_date.getText().toString());
                        db.addNewAcademicYear(ca);

                        Intent i = new Intent(NewAcademicYearActivity.this, ScheduleActivity.class);
                        startActivity(i);
                    }
            }
        });


    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.add_new_sch_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.add_new_sch_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

}
