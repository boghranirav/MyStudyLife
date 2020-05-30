package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExamViewActivity extends AppCompatActivity {

    String acadID;
    my_study_life_dbo db;
    ListView listViewExam;
    List<ClassExam> arrayExam=new ArrayList<ClassExam>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Exams");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("sID","");
        db=new my_study_life_dbo(getApplicationContext());


        listViewExam=(ListView)findViewById(R.id.add_exam_list);
        listViewExam.setAdapter(new AdapterExamView(getApplicationContext(),db.displayAllExam(acadID)));

        listViewExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView txtV=(TextView)view.findViewById(R.id.exam_view_id);

                SharedPreferences sharedPreferences3= getSharedPreferences("examInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3=sharedPreferences3.edit();
                editor3.clear();
                editor3.putString("examID",txtV.getText().toString());
                editor3.commit();
                Intent intent=new Intent(ExamViewActivity.this,ExamViewSingleActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ExamViewActivity.this,AddExamActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exam_menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exam_action_settings) {
            Intent i=new Intent(ExamViewActivity.this,PastExamActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
