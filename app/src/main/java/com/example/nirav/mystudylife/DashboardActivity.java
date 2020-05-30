package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtUser;
    TextView txtCDate;
    my_study_life_dbo db;
    Button btnToday,btnTask,btnClasses,btnExam;
    int count=0,countOverDue=0,classesToday=0,classesTomorrow=0,examupcoming=0;
    String cdate,acadID;
    List<ClassTasks> listTaskOverDue=new ArrayList<ClassTasks>();
    List<ClassClassesTimeChild> listClassesRemain=new ArrayList<ClassClassesTimeChild>();
    List<ClassClassesTimeChild> listClassesTomorrow=new ArrayList<ClassClassesTimeChild>();
    List<ClassExam> listExamUpcoming=new ArrayList<ClassExam>();
    ListView listClasses,listTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);

        SimpleDateFormat outputDate=new SimpleDateFormat("E,MMM dd, yyyy");
        Date d=new Date();
        cdate= outputDate.format(d.getTime());

        txtCDate=(TextView)findViewById(R.id.desh_today_date);
        btnToday=(Button)findViewById(R.id.deshbtntoday);
        btnTask=(Button)findViewById(R.id.deshbtntasks);
        btnClasses=(Button)findViewById(R.id.deshbtnclasses);
        btnExam=(Button)findViewById(R.id.deshbtnexams);
        listClasses=(ListView)findViewById(R.id.deshlistclass);
        listTasks=(ListView)findViewById(R.id.deshlisttasks);
        db=new my_study_life_dbo(getApplicationContext());

        txtCDate.setText("Today - "+cdate);
        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        acadID=sharedPreferences.getString("sID","");

        int todayTask=db.countTaskToday(cdate);
        int todayClass=db.countClassesToday(cdate);
        int todayExam=db.countExamToday(cdate);
        listTaskOverDue=db.displayTask(acadID);
        for (int i=0;i<listTaskOverDue.size();i++){
            String ddate="";
            SimpleDateFormat sdformate=new SimpleDateFormat("E,MMM dd, yyyy");
            try{
                Date date= sdformate.parse(listTaskOverDue.get(i).getT_due_date());
                ddate=outputDate.format(date);
            }catch (Exception e){}
            Date dateafter=null,datecurrent=null;
            try {
                dateafter= sdformate.parse(ddate);
                datecurrent=sdformate.parse(cdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((datecurrent.after(dateafter)) && (Integer.parseInt(listTaskOverDue.get(i).getT_completed())<100)) {
                countOverDue++;
            }
        }

        btnToday.setText("Today\n\n"+todayExam+" exams Today");
        btnTask.setText("Tasks"+"\n\n"+todayTask+" tasks due Today\n"+countOverDue+" tasks OverDue");


        String dayString="";
        Date daycur=null;
        try {
            Calendar calendar=Calendar.getInstance();
            daycur=calendar.getTime();
            dayString=new SimpleDateFormat("EEEE").format(daycur);
        }catch (Exception e){}

        SimpleDateFormat outputDate2=new SimpleDateFormat("E,MMM dd, yyyy");
        Calendar calendar2=Calendar.getInstance();
        Date today=calendar2.getTime();
        calendar2.add(Calendar.DAY_OF_YEAR,1);
        String tomorrow2= outputDate.format(calendar2.getTime());
        String dayTow=new SimpleDateFormat("EEEE").format(calendar2.getTime());

        btnExam.setText(""+tomorrow2+"\n"+dayTow);
        listClassesRemain=db.displayTimingByAID(acadID,dayString,cdate);
        listClassesTomorrow=db.displayTimingByAID(acadID,dayTow,tomorrow2);
        classesTomorrow=listClassesTomorrow.size();

        for (int i=0;i<listClassesRemain.size();i++){
            String dtime="";
            Date time=null,timecur=null;
            try {
                time = new SimpleDateFormat("H:mm").parse(listClassesRemain.get(i).getC_start_time());
                dtime=new SimpleDateFormat("H:mm").format(new Date());
                timecur=new SimpleDateFormat("H:mm").parse(dtime);

            } catch (ParseException e) {
                e.printStackTrace();
            }

                if(timecur.before(time)) {
                    classesToday++;
                }
        }

        btnClasses.setText("Classes\n\n"+classesToday+" class remaining\nToday");

        listExamUpcoming=db.displayUpcomingExam();
        for (int i=0;i<listExamUpcoming.size();i++){
            String ddate="",str7day="";
            Date _after7Day=null;
            SimpleDateFormat sdformate=new SimpleDateFormat("E,MMM dd, yyyy");
            try{
                Date date= sdformate.parse(listExamUpcoming.get(i).getE_date());
                ddate=outputDate.format(date);

                Calendar calendar=Calendar.getInstance();
                Date _7day=calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR,7);
                str7day= outputDate.format(calendar.getTime());
            }catch (Exception e){}
            Date dateafter=null,datecurrent=null;
            try {
                dateafter= sdformate.parse(ddate);
                datecurrent=sdformate.parse(cdate);
                _after7Day=sdformate.parse(str7day);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((datecurrent.before(dateafter)) && (_after7Day.after(dateafter))) {
                examupcoming++;
            }
        }
        btnExam.setText("Exams\n\n"+examupcoming+" exam in the next\n7 days");

        Thread t=new Thread(){
            @Override
            public void run() {
                try{
                    while (!isInterrupted()){
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextTask();
                            }
                        });
                    }
                }catch (InterruptedException e){}
            }
        };
        t.start();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,TaskViewActivity.class);
                startActivity(intent);
            }
        });

        btnClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,ScheduleActivity.class);
                startActivity(intent);
            }
        });

        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,ExamViewActivity.class);
                startActivity(intent);
            }
        });


        listClasses.setAdapter(new AdapterClassDash(getApplicationContext(),db.displayClassesToday(dayString,cdate,acadID)));
        setListViewHeigthBased2(listClasses);
        listTasks.setAdapter(new AdapterTaskView(getApplicationContext(),db.displayTaskToday(cdate)));
        setListViewHeigthBased(listTasks);


        //SharedPreferences sharedPreferences= getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //String name=sharedPreferences.getString("username","");



    }

    public void updateTextTask(){
        if(count==0){
            count++;
            int todayTask=db.countTaskToday(cdate);
            btnTask.setText("Tasks"+"\n\n"+todayTask+" tasks due Today\n"+countOverDue+" tasks OverDue");
            btnClasses.setText("Classes\n\n"+classesToday+" class remaining\nToday");
        }else {
            count=0;
            SimpleDateFormat outputDate=new SimpleDateFormat("E,MMM dd, yyyy");
            Calendar calendar=Calendar.getInstance();
            Date today=calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR,1);
            String tomorrow= outputDate.format(calendar.getTime());
            int taskTomorrow=db.countTaskToday(tomorrow);
            btnTask.setText("Tasks"+"\n\n"+taskTomorrow+" tasks due\nTomorrow");
            btnClasses.setText("Classes\n\n"+classesTomorrow+" classes\nTomorrow");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i=new Intent(DashboardActivity.this,SignInActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
        } else if (id == R.id.nav_tasks) {
            Intent task=new Intent(DashboardActivity.this,TaskViewActivity.class);
            startActivity(task);
        } else if (id == R.id.nav_exams) {
            Intent i=new Intent(DashboardActivity.this,ExamViewActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_schedule) {
            Intent i=new Intent(DashboardActivity.this,ScheduleActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_setting) {
            Intent i=new Intent(DashboardActivity.this,SettingActivity.class);
            startActivity(i);
        }else if(id == R.id.nav_holiday){
            Intent i=new Intent(DashboardActivity.this,ViewHolidayActivity.class);
            startActivity(i);
        }else if(id == R.id.nav_calendar){
            Intent i=new Intent(DashboardActivity.this,BaseActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public static void setListViewHeigthBased2(ListView listView){
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
