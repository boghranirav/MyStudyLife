package com.example.nirav.mystudylife;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

public class EditAcademicActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    SimpleDateFormat dateFormatter;

    my_study_life_dbo db;

    EditText txtStart,txtEnd;
    int ac_ID;
    Button btnTerm;
    TextView txtIDA;
    ListView lvTerm;
    List<ClassTermAcademic> listTerm=new ArrayList<ClassTermAcademic>();
    Button btnSave,btnDelete;
    List<ClassAcademicYear> listAcad=new ArrayList<ClassAcademicYear>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_academic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Academic Year");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("E,MMM dd, yyyy", Locale.US);

        txtIDA=(TextView)findViewById(R.id.edit_schtxt_id);
        txtStart=(EditText)findViewById(R.id.edit_schtxt_fromdate);
        txtEnd=(EditText)findViewById(R.id.edit_schetxt_todate);
        btnTerm=(Button)findViewById(R.id.edit_btn_newterm);
        lvTerm=(ListView)findViewById(R.id.edit_sch_term);
        btnSave=(Button)findViewById(R.id.edit_sch_save);
        btnDelete=(Button)findViewById(R.id.edit_sch_delete);


        db=new my_study_life_dbo(getApplicationContext());

            SharedPreferences sharedPreferences= getSharedPreferences("academicInfo", Context.MODE_PRIVATE);
            txtIDA.setText(sharedPreferences.getString("saTID",""));
            txtStart.setText(sharedPreferences.getString("startTDate",""));
            txtEnd.setText(sharedPreferences.getString("endTDate",""));

        lvTerm.setAdapter(new AdapterAcademicTerm(getApplicationContext(),db.displayTermByYear(txtIDA.getText().toString())));
        lvTerm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent3=new Intent(EditAcademicActivity.this,EditAcademicTermActivity.class);
                listTerm=db.displayTermByYear(txtIDA.getText().toString());
                intent3.putExtra("termAID",String.valueOf(listTerm.get(i).getT_term_id()));
                intent3.putExtra("termAName",String.valueOf(listTerm.get(i).getT_name()));
                intent3.putExtra("termAStart",String.valueOf(listTerm.get(i).getT_start_date()));
                intent3.putExtra("termAAEnd",String.valueOf(listTerm.get(i).getT_end_date()));
                startActivity(intent3);
            }
        });

        findViewsById();
        setDateTimeField();

        btnTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(EditAcademicActivity.this,NewAcademicTermActivity.class);
                i2.putExtra("eacID",txtIDA.getText().toString());
                startActivity(i2);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassAcademicYear ca=new ClassAcademicYear();
                ca.setAcad_id(Integer.parseInt(txtIDA.getText().toString()));
                ca.setA_start_date(txtStart.getText().toString());
                ca.setS_end_date(txtEnd.getText().toString());
                db.updateAcademicYear(ca);

                SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                listAcad=db.displayAcademicYearByID(Integer.parseInt(txtIDA.getText().toString()));
                editor.putString("sID",String.valueOf(listAcad.get(0).getAcad_id()));
                editor.putString("sDate",String.valueOf(listAcad.get(0).getA_start_date()));
                editor.putString("seDate",String.valueOf(listAcad.get(0).getS_end_date()));
                editor.commit();
                Intent i3=new Intent(EditAcademicActivity.this,ScheduleActivity.class);
                startActivity(i3);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAcademicYear(Integer.parseInt(txtIDA.getText().toString()));
                SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                listAcad=db.displayAcademicYear();
                editor.putString("sID",String.valueOf(listAcad.get(0).getAcad_id()));
                editor.putString("sDate",String.valueOf(listAcad.get(0).getA_start_date()));
                editor.putString("seDate",String.valueOf(listAcad.get(0).getS_end_date()));
                editor.commit();
                Intent i3=new Intent(EditAcademicActivity.this,ScheduleActivity.class);
                startActivity(i3);
            }
        });
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.edit_schtxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.edit_schetxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Date d= null;
        Date d2= null;
        try {
            d = new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(txtStart.getText().toString());
            d2 = new SimpleDateFormat("E,MMM dd, yyyy", Locale.ENGLISH).parse(txtEnd.getText().toString());
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
