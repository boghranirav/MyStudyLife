package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditSubjectActivity extends AppCompatActivity {

    Button btn_color,btnSave,btndelete;
    final Context context=this;
    EditText txtSubName,txtTermId;
    int r,g,b;
    my_study_life_dbo db;
    TextView txtSID;
    String aID;
    List<ClassTermAcademic> listTerm=new ArrayList<ClassTermAcademic>();

    int termID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Subject");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences= getSharedPreferences("SubjectEditInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        db=new my_study_life_dbo(getApplicationContext());
        btn_color=(Button)findViewById(R.id.edit_new_subject_color);
        btnSave=(Button)findViewById(R.id.edit_new_subject_btn_save);
        btndelete=(Button)findViewById(R.id.edit_new_subject_btn_delete);

        txtSID=(TextView)findViewById(R.id.edit_new_subject_sub_id);
        txtSubName=(EditText)findViewById(R.id.edit_new_subject_name);
        txtTermId=(EditText)findViewById(R.id.edit_new_subject_term_id);

        listTerm=db.displayTermByID(sharedPreferences.getString("SubEditTermID",""));
        txtSID.setText(sharedPreferences.getString("SubEditID",""));
        txtSubName.setText(sharedPreferences.getString("SubEditName",""));

        try {
            txtTermId.setText(getIntent().getExtras().getString("acSubTermName").toString());
            termID=Integer.parseInt(getIntent().getExtras().getString("acSubTermTID").toString());

        }catch (NullPointerException e) {
            termID=listTerm.get(0).getT_term_id();
            txtTermId.setText(listTerm.get(0).getT_name());
        }


        final StringBuilder builder=new StringBuilder();
        builder.append("#");
        builder.append(Integer.toHexString(Integer.parseInt(sharedPreferences.getString("SubEditColorR","").toString())));
        builder.append(Integer.toHexString(Integer.parseInt(sharedPreferences.getString("SubEditColorG","").toString())));
        builder.append(Integer.toHexString(Integer.parseInt(sharedPreferences.getString("SubEditColorB","").toString())));

        try {
            btn_color.setBackgroundColor(Color.parseColor(builder.toString()));
            r = Integer.parseInt(sharedPreferences.getString("SubEditColorR", "").toString());
            g = Integer.parseInt(sharedPreferences.getString("SubEditColorG", "").toString());
            b = Integer.parseInt(sharedPreferences.getString("SubEditColorB", "").toString());
        }catch (Exception e){

        }
        txtTermId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4=new Intent(EditSubjectActivity.this,EditSelectTermActivity.class);
                startActivity(intent4);
            }
        });

        btn_color.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final ColorPicker colorPicker = new ColorPicker(EditSubjectActivity.this);
                colorPicker.setDefaultColorButton(Color.parseColor(builder.toString())).setColumns(5).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
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

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteSubject(Integer.parseInt(txtSID.getText().toString()));
                Intent intent3=new Intent(EditSubjectActivity.this,ViewSubjectsActivity.class);
                startActivity(intent3);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtSubName.getText().toString().equals("") ||txtSubName.getText().toString().equals(null)){
                    txtSubName.setError("Enter Subject Name");
                }
                else {
                    ClassSubjects cs = new ClassSubjects();
                    cs.setSub_id(Integer.parseInt(txtSID.getText().toString()));
                    cs.setSub_term_id(termID);
                    cs.setSub_name(txtSubName.getText().toString());
                    cs.setSub_color_red(r);
                    cs.setSub_color_green(g);
                    cs.setSub_color_blue(b);
                    db.updateSubject(cs);

                    Intent intent = new Intent(EditSubjectActivity.this, ViewSubjectsActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

}
