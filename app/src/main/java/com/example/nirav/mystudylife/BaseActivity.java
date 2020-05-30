package com.example.nirav.mystudylife;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.Date;
import java.util.List;
import java.util.Locale;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;

public class BaseActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    my_study_life_dbo db;

    String sID,sDate,eDate;
    List<ClassExam> arrayExam=new ArrayList<ClassExam>();
    List<ClassSubjects> subList = new ArrayList<ClassSubjects>();
    List<ClassTasks> arrayTask=new ArrayList<ClassTasks>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);


        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {

            @Override
            public String interpretDate(java.util.Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());

            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
        //return null;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }


    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        db=new my_study_life_dbo(getApplicationContext());
        SharedPreferences sharedPreferences= getSharedPreferences("scheduleInfo", Context.MODE_PRIVATE);
        sID=sharedPreferences.getString("sID","");
        sDate=sharedPreferences.getString("sDate","");
        eDate=sharedPreferences.getString("seDate","");

        arrayExam=db.displayAllExam(sID);

        String fTime,fDate;

        Calendar startTime=null;
        Calendar endTime;
        WeekViewEvent event;

        SimpleDateFormat sdfTime=new SimpleDateFormat("hh:mm");
        SimpleDateFormat sdfDate=new SimpleDateFormat("E,MMM dd, yyyy");
        Date daTime=null,daDate=null,eaTime=null;

        int count=arrayExam.size();
      //  Log.d("Co : ",String.valueOf(count));
        for (int i=0;i<count;i++){
            startTime = Calendar.getInstance();
            fTime=arrayExam.get(i).getE_start_time();
            fDate=arrayExam.get(i).getE_date();
            try {
                daTime=sdfTime.parse(fTime);
                daDate=sdfDate.parse(fDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(daTime);
                cal.add(Calendar.MINUTE,Integer.parseInt(arrayExam.get(i).getE_duration()));
                eaTime=sdfTime.parse(sdfTime.format(cal.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String sYear=(String) android.text.format.DateFormat.format("yyyy",daDate);
            int sYear1=Integer.parseInt(sYear);

            // startTime.add(Calendar.DAY_OF_MONTH, 13);
            startTime.set(Calendar.DAY_OF_MONTH, daDate.getDate());
            startTime.set(Calendar.HOUR_OF_DAY, daTime.getHours());
            startTime.set(Calendar.MINUTE, daTime.getMinutes());
            startTime.set(Calendar.MONTH, daDate.getMonth());
            startTime.set(Calendar.YEAR, newYear-1);
            // Log.d("Date 1",String.valueOf(newYear));
            //Log.d("Date 2",String.valueOf(arrayExam.size()));

            endTime = (Calendar) startTime.clone();
            // endTime.set(Calendar.DAY_OF_MONTH, daDate.getDate());
            endTime.set(Calendar.HOUR_OF_DAY, eaTime.getHours());
            endTime.set(Calendar.MINUTE, eaTime.getMinutes());
            endTime.set(Calendar.MONTH, daDate.getMonth());
           // endTime.set(Calendar.YEAR,newYear-1);

            subList = db.displaySubjectBySubID(String.valueOf(arrayExam.get(i).getE_subject_id()));
            // event = new WeekViewEvent(1, getEventTitleExam(startTime,subList.get(0).getSub_name()), startTime, endTime);
            event = new WeekViewEvent(i, getEventTitleExam(startTime,String.valueOf(subList.get(0).getSub_name())), startTime, endTime);
            int r, g, b;
            r = subList.get(0).getSub_color_red();
            g = subList.get(0).getSub_color_green();
            b = subList.get(0).getSub_color_blue();
            event.setColor(Color.rgb(r, g, b));
            events.add(event);

//            Log.d("Log : ",String.valueOf(i));

        }


        arrayTask=db.displayTask(sID);

        for(int i=0;i<arrayTask.size();i++){

            startTime = Calendar.getInstance();

            fDate=arrayTask.get(i).getT_due_date();
            try {
                daDate=sdfDate.parse(fDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String sYear=(String) android.text.format.DateFormat.format("yyyy",daDate);

            Log.d("Log : ",""+daDate.getDate());
            //endTime.add(Calendar.HOUR_OF_DAY, 2);
            startTime.set(Calendar.DAY_OF_MONTH, daDate.getDate());
            startTime.set(Calendar.HOUR_OF_DAY, 1);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, daDate.getMonth() );
            startTime.set(Calendar.YEAR, newYear-1);

            endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, 2);
         //   endTime.set(Calendar.MONTH, daDate.getMonth() );
            event = new WeekViewEvent(i, getEventTitle(startTime), startTime, endTime);
            subList = db.displaySubjectBySubID(String.valueOf(arrayTask.get(i).getT_subject_id()));
            event = new WeekViewEvent(i, getEventTitleTask(startTime,String.valueOf(subList.get(0).getSub_name())), startTime, endTime);
            int r, g, b;
            r = subList.get(0).getSub_color_red();
            g = subList.get(0).getSub_color_green();
            b = subList.get(0).getSub_color_blue();
            event.setColor(Color.rgb(r, g, b));
            events.add(event);
        }
        return events;

    }

    protected String getEventTitleExam(Calendar time,String subName) {
        return String.format("Exam of %s On %02d:%02d %s/%d",subName, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    protected String getEventTitleTask(Calendar time,String subName) {
        return String.format("Task of %s Due On %s/%d",subName, time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }
}
