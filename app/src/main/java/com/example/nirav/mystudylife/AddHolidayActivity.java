package com.example.nirav.mystudylife;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddHolidayActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    SimpleDateFormat dateFormatter;

    EditText txtDateS,txtDateE,txtHName;
    Button btnAdd;
    my_study_life_dbo db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_holiday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Holiday");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);
        db=new my_study_life_dbo(getApplicationContext());

        findViewsById();

        setDateTimeField();

        txtDateE=(EditText)findViewById(R.id.add_holiday_end);
        txtDateS=(EditText)findViewById(R.id.add_holiday_start);
        txtHName=(EditText)findViewById(R.id.add_holiday_name);
        btnAdd=(Button)findViewById(R.id.add_holiday_save);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((txtHName.getText().toString().equals(""))||txtHName.getText().toString().equals(null)){
                    txtHName.setError("Enter Holiday Discription");
                }
                else
                    if((txtDateS.getText().toString().equals(""))||(txtDateS.getText().toString().equals(null))){
                        txtDateS.setError("Select Starting Date");
                    }else
                        if ((txtDateE.getText().toString().equals(""))||(txtDateE.getText().toString().equals(null))){
                            txtDateE.setError("Select Ending Date");
                        }
                else {
                    ClassHoliday ch = new ClassHoliday();
                    ch.setH_name(txtHName.getText().toString());
                    ch.setH_start_date(txtDateS.getText().toString());
                    ch.setH_end_date(txtDateE.getText().toString());
                    String cdate = DateFormat.getDateTimeInstance().format(new Date());
                    ch.setH_added_on(cdate);
                    db.addHoliday(ch);
                    Intent intent = new Intent(AddHolidayActivity.this, ViewHolidayActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.add_holiday_start);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.add_holiday_end);
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
