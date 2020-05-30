package com.example.nirav.mystudylife;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GeneralSettingActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText classTimeStart;

    private TimePickerDialog classTimePickerDialogFrom;

    private  int mHour,mMinute;

    my_study_life_dbo db;

    List<ClassGeneral> listG;
    EditText txt_d_time,txt_d_dur;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("General Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Spinner spinner_sub=(Spinner)findViewById(R.id.spin_f_day);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.start_day_name_array,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter);

        findViewsById();
        //time
        setTimeField();

        txt_d_time=(EditText)findViewById(R.id.txt_default_time);
        txt_d_dur=(EditText)findViewById(R.id.txt_def_duration);
        btn_save=(Button)findViewById(R.id.btn_save_general);

        db=new my_study_life_dbo(getApplicationContext());
        listG=db.displayGeneral();

        txt_d_time.setText(listG.get(0).getG_default_start_time());
        txt_d_dur.setText(listG.get(0).getG_default_duration());
        spinner_sub.setSelection(adapter.getPosition(listG.get(0).getG_first_date()));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassGeneral clg=new ClassGeneral();
                clg.setG_id(listG.get(0).g_id);
                clg.setG_first_date(spinner_sub.getSelectedItem().toString());
                clg.setG_default_start_time(txt_d_time.getText().toString());
                clg.setG_default_duration(txt_d_dur.getText().toString());

                db.updateGeneral(clg);
                Intent intentG=new Intent(GeneralSettingActivity.this,SettingActivity.class);
                startActivity(intentG);
            }
        });


    }

    private void findViewsById() {

        classTimeStart =(EditText)findViewById(R.id.txt_default_time);
        classTimeStart.setInputType(InputType.TYPE_NULL);

    }

    private void setTimeField(){
        classTimeStart.setOnClickListener(this);

   /*     SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm");
        Date d=null;
        try {
            d=simpleDateFormat.parse(txt_d_time.getText().toString());
        }catch (ParseException e){
            e.printStackTrace();
        }*/
        Calendar c=Calendar.getInstance();
        //c.setTime(d);

        mHour=c.get(Calendar.HOUR_OF_DAY);
        mMinute=c.get(Calendar.MINUTE);

        classTimePickerDialogFrom=new TimePickerDialog(this,new  TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                classTimeStart.setText(hourOfDay+":"+minutes);
            }
        },mHour,mMinute, false);


    }


    @Override
    public void onClick(View view) {
        if(view==classTimeStart){
            classTimePickerDialogFrom.show();
        }
    }

}
