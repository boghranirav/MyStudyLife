package com.example.nirav.mystudylife;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSubjectActivity extends AppCompatActivity {

    Button btn_color,btnSave;
    final Context context=this;
    EditText txtSubName,txtTermId;
    int r,g,b;
    my_study_life_dbo db;
    String aID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Subject");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtSubName=(EditText)findViewById(R.id.add_new_subject_name);
        btn_color=(Button)findViewById(R.id.add_new_subject_color);
        btnSave=(Button)findViewById(R.id.add_new_subject_btn_save);
        txtTermId=(EditText)findViewById(R.id.add_new_subject_term_id);

        SharedPreferences sharedPreferences2= getSharedPreferences("academicSubTermInfo", Context.MODE_PRIVATE);
        aID=sharedPreferences2.getString("acSubTermTID","");
        String aName=sharedPreferences2.getString("acSubTermName","");
        txtTermId.setText(aName);
        db=new my_study_life_dbo(getApplicationContext());

        SharedPreferences sharedPreferences= getSharedPreferences("academicInfo", Context.MODE_PRIVATE);
        final String sId=sharedPreferences.getString("saTID","");

        txtTermId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(AddSubjectActivity.this,SelectSubjectTermActivity.class);
                startActivity(in);
            }
        });

        btn_color.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final ColorPicker colorPicker = new ColorPicker(AddSubjectActivity.this);
                colorPicker.setDefaultColorButton(Color.parseColor("#2062af")).setColumns(5).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {

                        r=Color.red(color);
                        g=Color.green(color);
                        b=Color.blue(color);
                        btn_color.setBackgroundColor(Color.rgb(r,g,b));
                      // will be fired only when OK button was tapped
                    }

                    @Override
                    public void onCancel() {

                    }
                }).setRoundColorButton(true).show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtSubName.getText().toString().equals(null)|| txtSubName.getText().toString().equals("")){
                    txtSubName.setError("Enter Subject Name");
                }else {
                    ClassSubjects cs = new ClassSubjects();
                    cs.setSub_term_id(Integer.parseInt(aID));
                    cs.setSub_name(txtSubName.getText().toString());
                    cs.setSub_color_red(r);
                    cs.setSub_color_green(g);
                    cs.setSub_color_blue(b);
                    db.addNewSubject(cs);

                    Intent intent = new Intent(AddSubjectActivity.this, ViewSubjectsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
