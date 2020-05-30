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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskViewActivity extends AppCompatActivity {

    ListView listTasks,listTaskOverDue;
    my_study_life_dbo db;
    TextView txtOverDue,txtUpcoming;
    String acadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("sID","");

        db=new my_study_life_dbo(getApplicationContext());
        listTasks=(ListView)findViewById(R.id.task_list_current_view);
        listTaskOverDue=(ListView)findViewById(R.id.task_list_overdue);
        txtUpcoming=(TextView)findViewById(R.id.task_upcoming);
        txtOverDue=(TextView)findViewById(R.id.task_overdue);

        listTaskOverDue.setAdapter(new AdapterTaskOverDueView(getApplicationContext(),db.displayTask(acadID)));
        if (listTaskOverDue.getAdapter().getCount()<=0){
            txtOverDue.setVisibility(View.GONE);
        }
        else {
            txtOverDue.setVisibility(View.VISIBLE);
        }
        setListViewHeigthBased(listTaskOverDue);
        listTaskOverDue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txtV=(TextView)view.findViewById(R.id.task_edit_id);

                SharedPreferences sharedPreferences3= getSharedPreferences("taskInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3=sharedPreferences3.edit();
                editor3.clear();
                editor3.putString("taskID",txtV.getText().toString());
                editor3.commit();

                Intent intent=new Intent(TaskViewActivity.this,TaskViewSingleActivity.class);
                //intent.putExtra("CID",ct.getTask_id());
                startActivity(intent);
            }
        });

        listTasks.setAdapter(new AdapterTaskView(getApplicationContext(),db.displayTask(acadID)));
        if (listTasks.getAdapter().getCount()<=0){
            txtUpcoming.setText("  No Upcoming Tasks");
        }
        else {
            txtUpcoming.setText("  Upcoming Tasks");
        }
        setListViewHeigthBased(listTasks);

        listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView txtV=(TextView)view.findViewById(R.id.task_edit_id);

                SharedPreferences sharedPreferences3= getSharedPreferences("taskInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3=sharedPreferences3.edit();
                editor3.clear();
                editor3.putString("taskID",txtV.getText().toString());
                editor3.commit();

                Intent intent=new Intent(TaskViewActivity.this,TaskViewSingleActivity.class);
                //intent.putExtra("CID",ct.getTask_id());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TaskViewActivity.this,TaskAddActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_menu_past, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.task_past_action_settings) {
            Intent i=new Intent(TaskViewActivity.this,TaskViewPastActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setListViewHeigthBased(ListView listView){
        ListAdapter listAdapter=listView.getAdapter();
        if(listAdapter==null){
            return;
        }
        int totalheight=0;
        for (int i=0;i<listAdapter.getCount();i++){
            View listItem=listAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalheight+=listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params=listView.getLayoutParams();
        params.height=totalheight+(listView.getDividerHeight())*(listAdapter.getCount()-1);
        listView.setLayoutParams(params);
    }
}
