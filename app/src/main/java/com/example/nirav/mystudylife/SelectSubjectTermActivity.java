package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectSubjectTermActivity extends AppCompatActivity {

    ListView listView;

    my_study_life_dbo db;
    List<ClassTermAcademic> listTerm=new ArrayList<ClassTermAcademic>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject_term);

        listView=(ListView)findViewById(R.id.list_term_to_select);
        db=new my_study_life_dbo(getApplicationContext());

        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        final String sId=sharedPreferences.getString("sID","");

        listView.setAdapter(new AdapterFillTerm(getApplicationContext(),db.displayTermByYear(sId)));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                listTerm=db.displayTermByYear(sId);
                SharedPreferences sharedPreferences= getSharedPreferences("academicSubTermInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.putString("acSubTermTID",String.valueOf(listTerm.get(i).getT_term_id()));
                editor.putString("acSubTermName",listTerm.get(i).getT_name());
                editor.commit();

                Intent intent=new Intent(SelectSubjectTermActivity.this,AddSubjectActivity.class);
                startActivity(intent);
            }
        });

    }
}
