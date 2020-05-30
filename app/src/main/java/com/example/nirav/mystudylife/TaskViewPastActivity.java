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
import android.widget.TextView;

public class TaskViewPastActivity extends AppCompatActivity {

    ListView listTasks;
    my_study_life_dbo db;
    String acadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_past);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("sID","");

        db=new my_study_life_dbo(getApplicationContext());
        listTasks=(ListView)findViewById(R.id.task_past_view);

        listTasks.setAdapter(new AdapterTaskPastView(getApplicationContext(),db.displayTask(acadID)));

        listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView txtV=(TextView)view.findViewById(R.id.task_edit_id);

                SharedPreferences sharedPreferences3= getSharedPreferences("taskInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3=sharedPreferences3.edit();
                editor3.clear();
                editor3.putString("taskID",txtV.getText().toString());
                editor3.commit();

                Intent intent=new Intent(TaskViewPastActivity.this,TaskViewSingleActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TaskViewPastActivity.this,TaskAddActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_menu_current, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.task_current_action_settings) {
            Intent i=new Intent(TaskViewPastActivity.this,TaskViewActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
