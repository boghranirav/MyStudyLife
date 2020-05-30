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
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditHolidayActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    SimpleDateFormat dateFormatter;

    TextView txtHID;
    EditText txtHName,txtStartDate,txtEndDate;
    Button btnSave,btnDelete;
    my_study_life_dbo db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_holiday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Holiday");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);
        db=new my_study_life_dbo(getApplicationContext());
        txtHID=(TextView)findViewById(R.id.edit_holiday_id);
        txtHName=(EditText) findViewById(R.id.edit_holiday_name);
        txtStartDate=(EditText)findViewById(R.id.edit_holiday_start);
        txtEndDate=(EditText)findViewById(R.id.edit_holiday_end);
        btnSave=(Button)findViewById(R.id.edit_holiday_save);
        btnDelete=(Button)findViewById(R.id.edit_holiday_delete);

        txtHID.setText(getIntent().getExtras().getString("hID").toString());
        txtHName.setText(getIntent().getExtras().getString("hName").toString());
        txtStartDate.setText(getIntent().getExtras().getString("hSDate").toString());
        txtEndDate.setText(getIntent().getExtras().getString("hEDate").toString());

        findViewsById();
        setDateTimeField();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtHName.getText().toString().equals("") || txtHName.getText().toString().equals(null)){
                    txtHName.setError("Enter Holiday Discription");
                }
                else {
                    ClassHoliday ch = new ClassHoliday();
                    ch.setHoliday_id(Integer.parseInt(txtHID.getText().toString()));
                    ch.setH_name(txtHName.getText().toString());
                    ch.setH_start_date(txtStartDate.getText().toString());
                    ch.setH_end_date(txtEndDate.getText().toString());
                    db.updateHoliday(ch);
                    Intent intent = new Intent(EditHolidayActivity.this, ViewHolidayActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteHoliday(Integer.parseInt(txtHID.getText().toString()));
                Intent intent=new Intent(EditHolidayActivity.this,ViewHolidayActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.edit_holiday_start);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.edit_holiday_end);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);


        Date d= null;
        Date d2= null;
        try {
            d = new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(txtStartDate.getText().toString());
            d2 = new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(txtEndDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(d);
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendar.setTime(d2);
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
