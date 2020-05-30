package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PastExamActivity extends AppCompatActivity {

    String acadID;
    my_study_life_dbo db;
    ListView listViewExam;
    List<ClassExam> arrayExam=new ArrayList<ClassExam>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Exams");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("sID","");
        db=new my_study_life_dbo(getApplicationContext());


        listViewExam=(ListView)findViewById(R.id.past_exam_view);
        listViewExam.setAdapter(new AdapterExamPastView(getApplicationContext(),db.displayAllExam(acadID)));

        listViewExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                arrayExam=db.displayAllExam(acadID);
                SharedPreferences sharedPreferences3= getSharedPreferences("examInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3=sharedPreferences3.edit();
                editor3.clear();
                editor3.putString("examID",String.valueOf(arrayExam.get(i).getExma_id()));
                editor3.commit();
                Intent intent=new Intent(PastExamActivity.this,ExamViewSingleActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PastExamActivity.this,AddExamActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exam_menu_past, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exam_past_action_settings) {
            Intent i=new Intent(PastExamActivity.this,ExamViewActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
