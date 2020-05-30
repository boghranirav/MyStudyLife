package com.example.nirav.mystudylife;

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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EditSelectTermActivity extends AppCompatActivity {

    ListView listTerms;
    my_study_life_dbo db;
    List<ClassTermAcademic> listTerm=new ArrayList<ClassTermAcademic>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_select_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Select Term");
        setSupportActionBar(toolbar);

        listTerms=(ListView)findViewById(R.id.list_edit_term_to_select);

        db=new my_study_life_dbo(getApplicationContext());


        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        final String sId=sharedPreferences.getString("sID","");

        listTerms.setAdapter(new AdapterFillTerm(getApplicationContext(),db.displayTermByYear(sId)));


        listTerms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listTerm=db.displayTermByYear(sId);
                /*SharedPreferences sharedPreferences= getSharedPreferences("academicSubTermInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.putString("acSubTermTID",String.valueOf(listTerm.get(i).getT_term_id()));
                editor.putString("acSubTermName",listTerm.get(i).getT_name());
                editor.commit();
                */

                Intent intent=new Intent(EditSelectTermActivity.this,EditSubjectActivity.class);
                intent.putExtra("acSubTermTID",String.valueOf(listTerm.get(i).getT_term_id()));
                intent.putExtra("acSubTermName",listTerm.get(i).getT_name().toString());
                startActivity(intent);
            }
        });

    }

}
