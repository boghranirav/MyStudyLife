package com.example.nirav.mystudylife;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.EditText;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.TextView;

public class EditAcademicTermActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    SimpleDateFormat dateFormatter;
    EditText txtTermName,txtSDate,txtEDate;
    TextView txtTermID;
    Button btnSave,btnDelete;

    my_study_life_dbo db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_academic_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Academic Term");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);

        txtTermID=(TextView)findViewById(R.id.sch_edit_term_id);
        txtTermName=(EditText)findViewById(R.id.sch_edit_term_name);
        txtSDate=(EditText)findViewById(R.id.edit_sch_term_fromdate);
        txtEDate=(EditText)findViewById(R.id.edit_sch_term_todate);
        btnSave=(Button)findViewById(R.id.edit_sch_term_save);
        btnDelete=(Button)findViewById(R.id.edit_sch_term_delete);

        txtTermID.setText(getIntent().getExtras().getString("termAID").toString());
        txtTermName.setText(getIntent().getExtras().getString("termAName").toString());
        txtSDate.setText(getIntent().getExtras().getString("termAStart").toString());
        txtEDate.setText(getIntent().getExtras().getString("termAAEnd").toString());

        db=new my_study_life_dbo(getApplicationContext());
        findViewsById();

        setDateTimeField();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassTermAcademic ct=new ClassTermAcademic();
                ct.setT_term_id(Integer.parseInt(txtTermID.getText().toString()));
                ct.setT_name(txtTermName.getText().toString());
                ct.setT_start_date(txtSDate.getText().toString());
                ct.setT_end_date(txtEDate.getText().toString());
                db.updateTerm(ct);
                Intent i=new Intent(EditAcademicTermActivity.this,EditAcademicActivity.class);
                startActivity(i);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.deleteTerm(Integer.parseInt(txtTermID.getText().toString()));
                Intent i=new Intent(EditAcademicTermActivity.this,EditAcademicActivity.class);
                startActivity(i);
            }
        });

    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.edit_sch_term_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.edit_sch_term_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Date d= null;
        Date d2= null;
        try {
            d = new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(txtSDate.getText().toString());
            d2 = new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(txtEDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(d);

        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendar.setTime(d2);

        toDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

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
