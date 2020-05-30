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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewAcademicTermActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    SimpleDateFormat dateFormatter;
    Button btnSaveTerm;
    my_study_life_dbo db;
    EditText txtStart,txtEnd,txtName;
    TextView txtID;
    int nAID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_academic_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Academic Term");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);

        txtID=(TextView)findViewById(R.id.sch_new_term_id);

        txtID.setText(getIntent().getExtras().getString("eacID").toString());
        txtStart=(EditText)findViewById(R.id.add_new_sch_term_fromdate);
        txtEnd=(EditText)findViewById(R.id.add_new_sch_term_todate);
        txtName=(EditText)findViewById(R.id.sch_new_term_name);

        db=new my_study_life_dbo(getApplicationContext());

        btnSaveTerm=(Button)findViewById(R.id.add_new_schbtn_term_save);
        btnSaveTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassTermAcademic ct=new ClassTermAcademic();
                ct.setT_academic_id(Integer.parseInt(txtID.getText().toString()));
                ct.setT_start_date(txtStart.getText().toString());
                ct.setT_end_date(txtEnd.getText().toString());
                ct.setT_name(txtName.getText().toString());
                db.addNewAcademicTerm(ct);

                Intent intent1=new Intent(NewAcademicTermActivity.this,ScheduleActivity.class);

                startActivity(intent1);
            }
        });

        findViewsById();

        setDateTimeField();


    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.add_new_sch_term_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.add_new_sch_term_todate);
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
