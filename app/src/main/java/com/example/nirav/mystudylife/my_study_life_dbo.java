package com.example.nirav.mystudylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Nirav on 27/10/16.
 */

public class my_study_life_dbo extends SQLiteOpenHelper {


    public my_study_life_dbo(Context context) {
        super(context, "db_mystudylife", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE table_academic_year(academic_id INTEGER PRIMARY KEY AUTOINCREMENT, academic_start_date TEXT, academic_end_date TEXT)");
        db.execSQL("CREATE TABLE table_term_academic(term_id INTEGER PRIMARY KEY AUTOINCREMENT, academic_id INTEGER, term_start_date TEXT, term_end_date TEXT,term_name TEXT, FOREIGN KEY(academic_id) REFERENCES table_academic_year(academic_id))");
        db.execSQL("CREATE TABLE table_subjects(subject_id INTEGER PRIMARY KEY AUTOINCREMENT, term_id INTEGER, subject_name TEXT, s_color_red INTEGER, s_color_green INTEGER, s_color_blue INTEGER, FOREIGN KEY(term_id) REFERENCES table_term_academic(term_id))");
        db.execSQL("CREATE TABLE table_classes(class_id INTEGER PRIMARY KEY AUTOINCREMENT, subject_id INTEGER, class_module TEXT, class_room TEXT, class_building TEXT, class_teacher TEXT, FOREIGN KEY(subject_id) REFERENCES table_subjects(subject_id))");
        db.execSQL("CREATE TABLE table_classes_time_main(time_id INTEGER PRIMARY KEY AUTOINCREMENT, class_id INTEGER, class_type TEXT, FOREIGN KEY(class_id) REFERENCES table_classes(class_id))");
        db.execSQL("CREATE TABLE table_classes_time_child(day_id INTEGER PRIMARY KEY AUTOINCREMENT, time_id INTEGER, day_name TEXT, class_start_time TEXT, class_end_time TEXT, class_date TEXT, FOREIGN KEY(time_id) REFERENCES table_classes_time_main(time_id))");
        db.execSQL("CREATE TABLE table_holiday(holiday_id INTEGER PRIMARY KEY AUTOINCREMENT, holiday_name TEXT, holiday_start_date TEXT, holiday_end_date TEXT, holiday_added_on TEXT)");
        db.execSQL("CREATE TABLE table_exams(exam_id INTEGER PRIMARY KEY AUTOINCREMENT, subject_id INTEGER, exam_module TEXT, exam_date TEXT, exam_start_time TEXT, exam_duration TEXT, exam_seat TEXT, exam_room TEXT, exam_added_on TEXT, FOREIGN KEY(subject_id) REFERENCES table_subjects(subject_id))");
        db.execSQL("CREATE TABLE table_tasks(task_id INTEGER PRIMARY KEY AUTOINCREMENT, subject_id INTEGER, task_type TEXT, task_due_date TEXT, task_title TEXT, task_detail TEXT, task_completed TEXT, task_added_on TEXT, task_completed_on TEXT, exam_id INTEGER, FOREIGN KEY(subject_id) REFERENCES table_subjects(subject_id))");
        db.execSQL("CREATE TABLE table_general(g_id INTEGER PRIMARY KEY AUTOINCREMENT, g_first_day TEXT, default_start_time TEXT, default_duration TEXT)");
        db.execSQL("CREATE TABLE table_reminder(r_id INTEGER PRIMARY KEY AUTOINCREMENT, class_remind_before TEXT, exam_remind_before TEXT, task_remind_before TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists table_academic_year");
        db.execSQL("drop table if exists table_term_academic");
        db.execSQL("drop table if exists table_subjects");
        db.execSQL("drop table if exists table_classes");
        db.execSQL("drop table if exists table_classes_time_main");
        db.execSQL("drop table if exists table_classes_time_child");
        db.execSQL("drop table if exists table_holiday");
        db.execSQL("drop table if exists table_exams");
        db.execSQL("drop table if exists table_tasks");
        db.execSQL("drop table if exists table_general");
        db.execSQL("drop table if exists table_reminder");

    }

    public List<ClassGeneral> displayGeneral(){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_general",null);
        List<ClassGeneral> genList=new ArrayList<ClassGeneral>();

        if(genList.isEmpty()){
            SQLiteDatabase dbin=this.getWritableDatabase();
            ContentValues cv=new ContentValues();
            cv.put("g_first_day","Monday");
            cv.put("default_start_time","9:0");
            cv.put("default_duration","90");
            dbin.insert("table_general",null,cv);

        }
            if(cursor.moveToFirst()){
                do{
                    ClassGeneral cg=new ClassGeneral();
                    cg.setG_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("g_id"))));
                    cg.setG_default_start_time(cursor.getString(cursor.getColumnIndex("default_start_time")).toString());
                    cg.setG_first_date(cursor.getString(cursor.getColumnIndex("g_first_day")).toString());
                    cg.setG_default_duration(cursor.getString(cursor.getColumnIndex("default_duration")).toString());
                    genList.add(cg);
                }while (cursor.moveToNext());
            }
        return genList;
    }

    public void updateGeneral(ClassGeneral cg){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("g_first_day",cg.getG_first_date());
        cv.put("default_start_time", cg.getG_default_start_time());
        cv.put("default_duration",cg.getG_default_duration());
        db.update("table_general",cv,"g_id = ?", new String[]{Integer.toString(cg.getG_id())});
        db.close();
    }

    public void addNewAcademicYear(ClassAcademicYear cay){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("academic_start_date",cay.getA_start_date());
        cv.put("academic_end_date",cay.getS_end_date());
        db.insert("table_academic_year",null,cv);
        db.close();
    }

    public  List<ClassAcademicYear> displayAcademicYear(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_academic_year",null);
        List<ClassAcademicYear> acList=new ArrayList<ClassAcademicYear>();
        if(cursor.moveToFirst()){
            do{
                ClassAcademicYear cy=new ClassAcademicYear();
                cy.setAcad_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("academic_id"))));
                cy.setA_start_date(cursor.getString(cursor.getColumnIndex("academic_start_date")));
                cy.setS_end_date(cursor.getString(cursor.getColumnIndex("academic_end_date")));
                acList.add(cy);

            }while (cursor.moveToNext());
        }
        return acList;
    }

    public  List<ClassAcademicYear> displayAcademicYearByID(Integer aID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_academic_year where academic_id= ? ",new String[]{String.valueOf(aID)});
        List<ClassAcademicYear> acList=new ArrayList<ClassAcademicYear>();
        if(cursor.moveToFirst()){
            do{
                ClassAcademicYear cy=new ClassAcademicYear();
                cy.setAcad_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("academic_id"))));
                cy.setA_start_date(cursor.getString(cursor.getColumnIndex("academic_start_date")));
                cy.setS_end_date(cursor.getString(cursor.getColumnIndex("academic_end_date")));
                acList.add(cy);

            }while (cursor.moveToNext());
        }
        return acList;
    }

    public void addNewAcademicTerm(ClassTermAcademic cta){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("academic_id",cta.getT_academic_id());
        cv.put("term_start_date",cta.getT_start_date());
        cv.put("term_end_date",cta.getT_end_date());
        cv.put("term_name",cta.getT_name());
        db.insert("table_term_academic",null,cv);
        db.close();
    }

    public List<ClassTermAcademic> displayTermByYear(String acID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_term_academic where academic_id=?",new String[]{acID});
        List<ClassTermAcademic> termList=new ArrayList<ClassTermAcademic>();
        if ((cursor.moveToFirst())){
            do{
                ClassTermAcademic ct=new ClassTermAcademic();
                ct.setT_term_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("term_id"))));
                ct.setT_academic_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("academic_id"))));
                ct.setT_name(cursor.getString(cursor.getColumnIndex("term_name")));
                ct.setT_start_date(cursor.getString(cursor.getColumnIndex("term_start_date")));
                ct.setT_end_date(cursor.getString(cursor.getColumnIndex("term_end_date")));
                termList.add(ct);
            }while (cursor.moveToNext());
        }
        return termList;
    }

    public List<ClassTermAcademic> displayTermByID(String acID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_term_academic where term_id=?",new String[]{acID});
        List<ClassTermAcademic> termList=new ArrayList<ClassTermAcademic>();
        if ((cursor.moveToFirst())){
            do{
                ClassTermAcademic ct=new ClassTermAcademic();
                ct.setT_term_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("term_id"))));
                ct.setT_academic_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("academic_id"))));
                ct.setT_name(cursor.getString(cursor.getColumnIndex("term_name")));
                ct.setT_start_date(cursor.getString(cursor.getColumnIndex("term_start_date")));
                ct.setT_end_date(cursor.getString(cursor.getColumnIndex("term_end_date")));
                termList.add(ct);
            }while (cursor.moveToNext());
        }
        return termList;
    }


    public void deleteTerm(Integer tID){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("table_term_academic","term_id=?",new String[]{String.valueOf(tID)});
    }

    public void updateTerm(ClassTermAcademic cta){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("term_start_date",cta.getT_start_date());
        cv.put("term_end_date",cta.getT_end_date());
        cv.put("term_name",cta.getT_name());
        db.update("table_term_academic",cv,"term_id=?",new String[]{String.valueOf(cta.getT_term_id())});
        db.close();
    }

    public void  deleteAcademicYear(Integer aID){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("table_academic_year","academic_id=?",new String[]{String.valueOf(aID)});
    }

    public void updateAcademicYear(ClassAcademicYear cay){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("academic_start_date",cay.getA_start_date());
        cv.put("academic_end_date",cay.getS_end_date());
        db.update("table_academic_year",cv,"academic_id=?",new String[]{String.valueOf(cay.getAcad_id())});
        db.close();
    }

    public List<ClassSubjects> displaySubjectByYear(String aID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_subjects ts,table_term_academic tta,table_academic_year tay where ts.term_id=tta.term_id and tta.academic_id=tay.academic_id and tta.academic_id=?",new String[]{aID});
        List<ClassSubjects> yearSubject=new ArrayList<ClassSubjects>();

        if(cursor.moveToFirst()){
            do {
                ClassSubjects cs=new ClassSubjects();

                cs.setSub_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                cs.setSub_term_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("term_id"))));
                cs.setSub_name(cursor.getString(cursor.getColumnIndex("subject_name")));
                cs.setSub_color_red(Integer.parseInt(cursor.getString(cursor.getColumnIndex("s_color_red"))));
                cs.setSub_color_green(Integer.parseInt(cursor.getString(cursor.getColumnIndex("s_color_green"))));
                cs.setSub_color_blue(Integer.parseInt(cursor.getString(cursor.getColumnIndex("s_color_blue"))));
                yearSubject.add(cs);

            }while (cursor.moveToNext());
        }
        return yearSubject;
    }

    public List<ClassSubjects> displaySubjectBySubID(String aID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_subjects where subject_id=?",new String[]{aID});
        List<ClassSubjects> yearSubject=new ArrayList<ClassSubjects>();

        if(cursor.moveToFirst()){
            do {
                ClassSubjects cs=new ClassSubjects();

                cs.setSub_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                cs.setSub_term_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("term_id"))));
                cs.setSub_name(cursor.getString(cursor.getColumnIndex("subject_name")));
                cs.setSub_color_red(Integer.parseInt(cursor.getString(cursor.getColumnIndex("s_color_red"))));
                cs.setSub_color_green(Integer.parseInt(cursor.getString(cursor.getColumnIndex("s_color_green"))));
                cs.setSub_color_blue(Integer.parseInt(cursor.getString(cursor.getColumnIndex("s_color_blue"))));
                yearSubject.add(cs);

            }while (cursor.moveToNext());
        }
        return yearSubject;
    }

    public void addNewSubject(ClassSubjects cs){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("term_id",cs.getSub_term_id());
        cv.put("subject_name",cs.getSub_name());
        cv.put("s_color_red",cs.getSub_color_red());
        cv.put("s_color_green",cs.getSub_color_green());
        cv.put("s_color_blue",cs.getSub_color_blue());
        db.insert("table_subjects",null,cv);
        db.close();
    }

    public void deleteSubject(Integer sID){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("table_subjects","subject_id=?",new String[]{String.valueOf(sID)});
    }

    public void updateSubject(ClassSubjects cs){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("term_id",cs.getSub_term_id());
        cv.put("subject_name",cs.getSub_name());
        cv.put("s_color_red",cs.getSub_color_red());
        cv.put("s_color_green",cs.getSub_color_green());
        cv.put("s_color_blue",cs.getSub_color_blue());
        db.update("table_subjects",cv,"subject_id=?",new String[]{String.valueOf(cs.getSub_id())});
        db.close();
    }

    public void addHoliday(ClassHoliday ch){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("holiday_name",ch.getH_name());
        cv.put("holiday_start_date",ch.getH_start_date());
        cv.put("holiday_end_date",ch.getH_end_date());
        cv.put("holiday_added_on",ch.getH_added_on());
        db.insert("table_holiday",null,cv);
        db.close();
    }

    public List<ClassHoliday> displayHoliday(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_holiday",null);
        List<ClassHoliday> listH=new ArrayList<ClassHoliday>();

        if(cursor.moveToFirst()){
            do{
                ClassHoliday ch=new ClassHoliday();
                ch.setHoliday_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("holiday_id"))));
                ch.setH_name(cursor.getString(cursor.getColumnIndex("holiday_name")));
                ch.setH_start_date(cursor.getString(cursor.getColumnIndex("holiday_start_date")));
                ch.setH_end_date(cursor.getString(cursor.getColumnIndex("holiday_end_date")));
                listH.add(ch);
            }while (cursor.moveToNext());
        }
        return listH;
    }

    public void deleteHoliday(Integer hID){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("table_holiday","holiday_id=?",new String[]{String.valueOf(hID)});
    }

    public void updateHoliday(ClassHoliday ch){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("holiday_name",ch.getH_name());
        cv.put("holiday_start_date",ch.getH_start_date());
        cv.put("holiday_end_date",ch.getH_end_date());
        db.update("table_holiday",cv,"holiday_id=?",new String[]{String.valueOf(ch.getHoliday_id())});
        db.close();
    }

    public void addClass(ClassClasses ch,String classesType,ClassClassesTimeChild ctc,ArrayList<String> arrayDays){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv1=new ContentValues();

        cv1.put("subject_id",ch.getClass_subject_id());
        cv1.put("class_module",ch.getClass_module());
        cv1.put("class_room",ch.getClass_room());
        cv1.put("class_building",ch.getClass_building());
        cv1.put("class_teacher",ch.getClass_teacher());
        db.insert("table_classes",null,cv1);

        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor cursor2=db2.rawQuery("select max(class_id) from table_classes",null);
        cursor2.moveToFirst();
        String classID=String.valueOf(cursor2.getInt(0));

        SQLiteDatabase db3=this.getWritableDatabase();
        ContentValues cv3=new ContentValues();
        cv3.put("class_id",classID);
        cv3.put("class_type",classesType);
        db3.insert("table_classes_time_main",null,cv3);

        SQLiteDatabase db4=this.getReadableDatabase();
        Cursor cursor4=db4.rawQuery("select max(time_id) from table_classes_time_main",null);
        cursor4.moveToFirst();
        String timeID=String.valueOf(cursor4.getInt(0));

        SQLiteDatabase db5=this.getWritableDatabase();
        ContentValues cv5=new ContentValues();
        //(day_id ,   ,
        if(classesType.equals("0")){
            for(int i=0;i<arrayDays.size();i++){
                cv5.put("time_id",timeID);
                cv5.put("day_name",arrayDays.get(i));
                cv5.put("class_start_time",ctc.getC_start_time());
                cv5.put("class_end_time",ctc.getC_end_time());
                db5.insert("table_classes_time_child",null,cv5);
            }

        }else if(classesType.equals("1")){
            cv5.put("time_id",timeID);
            cv5.put("class_date",ctc.getC_date());
            cv5.put("class_start_time",ctc.getC_start_time());
            cv5.put("class_end_time",ctc.getC_end_time());
            db5.insert("table_classes_time_child",null,cv5);
        }
    }

    public List<ClassClasses> displayClasses(String aID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_classes tc, table_subjects ts, table_term_academic tta, table_academic_year tay where tc.subject_id=ts.subject_id and tta.term_id=ts.term_id and tay.academic_id=tta.academic_id and tay.academic_id=?",new String[]{aID});
        List<ClassClasses> listC=new ArrayList<ClassClasses>();


        if(cursor.moveToFirst()){
            do {
                ClassClasses c=new ClassClasses();
                c.setClass_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("class_id"))));
                c.setClass_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                c.setClass_module(cursor.getString(cursor.getColumnIndex("class_module")));
                c.setClass_room(cursor.getString(cursor.getColumnIndex("class_room")));
                c.setClass_building(cursor.getString(cursor.getColumnIndex("class_building")));
                c.setClass_teacher(cursor.getString(cursor.getColumnIndex("class_teacher")));
                listC.add(c);
            }while (cursor.moveToNext());
        }
        return listC;
    }

    public List<ClassClasses> displayClassesByClassID(Integer tID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_classes where class_id=?",new String[]{String.valueOf(tID)});
        List<ClassClasses> listC=new ArrayList<ClassClasses>();

        if(cursor.moveToFirst()){
            do {
                ClassClasses c=new ClassClasses();
                c.setClass_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("class_id"))));
                c.setClass_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                c.setClass_module(cursor.getString(cursor.getColumnIndex("class_module")));
                c.setClass_room(cursor.getString(cursor.getColumnIndex("class_room")));
                c.setClass_building(cursor.getString(cursor.getColumnIndex("class_building")));
                c.setClass_teacher(cursor.getString(cursor.getColumnIndex("class_teacher")));
                listC.add(c);
            }while (cursor.moveToNext());
        }
        return listC;
    }

    public List<ClassClassesTimeChild> displayTimingByCID(String classID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_classes_time_child tc, table_classes_time_main tm where tc.time_id=tm.time_id and tm.class_id=?",new String[]{classID});
        List<ClassClassesTimeChild> listT=new ArrayList<ClassClassesTimeChild>();

        if(cursor.moveToFirst()){
            do {
                ClassClassesTimeChild cc=new ClassClassesTimeChild();
                cc.setDay_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("day_id"))));
                cc.setTime_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("time_id"))));
                cc.setDay_name(cursor.getString(cursor.getColumnIndex("day_name")));
                cc.setC_date(cursor.getString(cursor.getColumnIndex("class_date")));
                cc.setC_start_time(cursor.getString(cursor.getColumnIndex("class_start_time")));
                cc.setC_end_time(cursor.getString(cursor.getColumnIndex("class_end_time")));
                listT.add(cc);
            }while (cursor.moveToNext());
        }
        return listT;
    }

    public List<ClassClassesTimeMain> displayMainTimeByCID(String classID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_classes_time_main where class_id=?",new String[]{classID});
        List<ClassClassesTimeMain> listTime=new ArrayList<ClassClassesTimeMain>();

        if(cursor.moveToFirst()){
            ClassClassesTimeMain cm=new ClassClassesTimeMain();
            cm.setTime_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("time_id"))));
            cm.setT_class_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("class_id"))));
            cm.setClass_type(cursor.getString(cursor.getColumnIndex("class_type")));
            listTime.add(cm);
        }
        return  listTime;
    }

    public void updateClassAndTime(ClassClasses cc,ClassClassesTimeMain cm,ClassClassesTimeChild cct,ArrayList<String> dayNames){

        SQLiteDatabase db1=this.getWritableDatabase();
        ContentValues cv1=new ContentValues();
        cv1.put("subject_id",cc.getClass_subject_id());
        cv1.put("class_module",cc.getClass_module());
        cv1.put("class_room",cc.getClass_room());
        cv1.put("class_building",cc.getClass_building());
        cv1.put("class_teacher",cc.getClass_teacher());
        db1.update("table_classes",cv1,"class_id=?",new String[]{String.valueOf(cc.getClass_id())});

        SQLiteDatabase db2=this.getWritableDatabase();
        ContentValues cv2=new ContentValues();
        cv2.put("class_type",cm.getClass_type());
        db2.update("table_classes_time_main",cv2,"class_id=?",new String[]{String.valueOf(cm.getT_class_id())});

        SQLiteDatabase db3=this.getWritableDatabase();
        ContentValues cv3=new ContentValues();
        db3.delete("table_classes_time_child","time_id=?",new String[]{String.valueOf(cct.getTime_id())});

        if(cm.getClass_type().equals("0")) {
            for (int i = 0; i < dayNames.size(); i++) {
                cv3.put("time_id", cct.getTime_id());
                cv3.put("day_name", dayNames.get(i));
                cv3.put("class_start_time", cct.getC_start_time());
                cv3.put("class_end_time", cct.getC_end_time());
                db3.insert("table_classes_time_child", null, cv3);
            }
        }else {
                cv3.put("time_id",cct.getTime_id());
                cv3.put("class_date",cct.getC_date());
                cv3.put("class_start_time",cct.getC_start_time());
                cv3.put("class_end_time",cct.getC_end_time());
                db3.insert("table_classes_time_child",null,cv3);
        }

    }

    public void deleteClassTime(String cID){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("table_classes","class_id=?",new String[]{cID});
    }

    public void addNewExam(ClassExam ce){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("subject_id",ce.getE_subject_id());
        cv.put("exam_module",ce.getE_module());
        cv.put("exam_date",ce.getE_date());
        cv.put("exam_start_time",ce.getE_start_time());
        cv.put("exam_duration",ce.getE_duration());
        cv.put("exam_seat",ce.getE_seat());
        cv.put("exam_room",ce.getE_room());
        cv.put("exam_added_on",ce.getE_added_on());
        db.insert("table_exams",null,cv);
    }

    public List<ClassExam> displayAllExam(String aID){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery("select * from table_exams te, table_subjects ts, table_term_academic tta where te.subject_id=ts.subject_id and tta.term_id=ts.term_id and tta.academic_id=? order by te.exam_date",new String[]{aID});
        //Cursor cursor=db.rawQuery("select * from table_exams where exam_date<?",new String[]{getDate});
        List<ClassExam> listExam=new ArrayList<ClassExam>();

        if(cursor.moveToFirst()){
            do {
                ClassExam ce=new ClassExam();
                ce.setExma_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("exam_id"))));
                ce.setE_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                ce.setE_module(cursor.getString(cursor.getColumnIndex("exam_module")));
                ce.setE_date(cursor.getString(cursor.getColumnIndex("exam_date")));
                ce.setE_start_time(cursor.getString(cursor.getColumnIndex("exam_start_time")));
                ce.setE_seat(cursor.getString(cursor.getColumnIndex("exam_seat")));
                ce.setE_room(cursor.getString(cursor.getColumnIndex("exam_room")));
                ce.setE_duration(cursor.getString(cursor.getColumnIndex("exam_duration")));
                listExam.add(ce);
            }while (cursor.moveToNext());
        }
        return  listExam;
    }

    public List<ClassExam> displayExamByEID(String eID){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery("select * from table_exams where exam_id=?",new String[]{eID});
        List<ClassExam> listExam=new ArrayList<ClassExam>();

        if(cursor.moveToFirst()){
            do {
                ClassExam ce=new ClassExam();
                ce.setExma_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("exam_id"))));
                ce.setE_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                ce.setE_module(cursor.getString(cursor.getColumnIndex("exam_module")));
                ce.setE_date(cursor.getString(cursor.getColumnIndex("exam_date")));
                ce.setE_start_time(cursor.getString(cursor.getColumnIndex("exam_start_time")));
                ce.setE_seat(cursor.getString(cursor.getColumnIndex("exam_seat")));
                ce.setE_room(cursor.getString(cursor.getColumnIndex("exam_room")));
                ce.setE_duration(cursor.getString(cursor.getColumnIndex("exam_duration")));
                listExam.add(ce);
            }while (cursor.moveToNext());
        }
        return  listExam;
    }

    public void deleteExamByEID(String eID){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("table_exams","exam_id=?",new String[]{eID});
    }

    public void updateExamByEID(ClassExam ce){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("subject_id",ce.getE_subject_id());
        cv.put("exam_module",ce.getE_module());
        cv.put("exam_date",ce.getE_date());
        cv.put("exam_start_time",ce.getE_start_time());
        cv.put("exam_duration",ce.getE_duration());
        cv.put("exam_seat",ce.getE_seat());
        cv.put("exam_room",ce.getE_room());
        db.update("table_exams",cv,"exam_id=?",new String[]{String.valueOf(ce.getExma_id())});
    }

    public void addNewTask(ClassTasks ct){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("subject_id",ct.getT_subject_id());
        cv.put("task_type",ct.getT_type());
        cv.put("task_due_date",ct.getT_due_date());
        cv.put("task_title",ct.getT_title());
        cv.put("task_detail",ct.getT_detail());
        cv.put("task_completed","0");
        cv.put("task_added_on",ct.getT_added_on());
        //cv.put("task_completed_on",ct.getT_completed_on());
        db.insert("table_tasks",null,cv);
    }

    public List<ClassTasks> displayTask(String aID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_tasks tt, table_subjects ts, table_term_academic tta where tt.subject_id=ts.subject_id and tta.term_id=ts.term_id and tta.academic_id=?",new String[]{aID});
        List<ClassTasks> listTask=new ArrayList<ClassTasks>();
        if (cursor.moveToFirst()){
            do {
                ClassTasks ct=new ClassTasks();
                ct.setTask_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("task_id"))));
                ct.setT_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                ct.setT_type(cursor.getString(cursor.getColumnIndex("task_type")));
                ct.setT_due_date(cursor.getString(cursor.getColumnIndex("task_due_date")));
                ct.setT_title(cursor.getString(cursor.getColumnIndex("task_title")));
                ct.setT_detail(cursor.getString(cursor.getColumnIndex("task_detail")));
                ct.setT_completed(cursor.getString(cursor.getColumnIndex("task_completed")));
                ct.setT_added_on(cursor.getString(cursor.getColumnIndex("task_added_on")));
                ct.setT_completed_on(cursor.getString(cursor.getColumnIndex("task_completed_on")));
                listTask.add(ct);
            }while (cursor.moveToNext());
        }
        return listTask;
    }

    public List<ClassTasks> displayTaskByTID(String tID){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_tasks where task_id=?",new String[]{tID});
        List<ClassTasks> listTask=new ArrayList<ClassTasks>();
        if (cursor.moveToFirst()){
            do {
                ClassTasks ct=new ClassTasks();
                ct.setTask_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("task_id"))));
                ct.setT_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                ct.setT_type(cursor.getString(cursor.getColumnIndex("task_type")));
                ct.setT_due_date(cursor.getString(cursor.getColumnIndex("task_due_date")));
                ct.setT_title(cursor.getString(cursor.getColumnIndex("task_title")));
                ct.setT_detail(cursor.getString(cursor.getColumnIndex("task_detail")));
                ct.setT_completed(cursor.getString(cursor.getColumnIndex("task_completed")));
                ct.setT_added_on(cursor.getString(cursor.getColumnIndex("task_added_on")));
                ct.setT_completed_on(cursor.getString(cursor.getColumnIndex("task_completed_on")));
                listTask.add(ct);
            }while (cursor.moveToNext());
        }
        return listTask;
    }

    public void updateTaskComplitedByTID(String tID,String comp){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("task_completed",comp);
        if (Integer.parseInt(comp)==100) {
            String cdate = DateFormat.getDateTimeInstance().format(new Date());
            cv.put("task_completed_on", cdate);
        }
        db.update("table_tasks",cv,"task_id=?",new String[]{tID});
    }

    public  void deleteTaskByTid(String tID){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("table_tasks","task_id=?",new String[]{tID});
    }

    public void updateTaskByTID(ClassTasks ct){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("subject_id",ct.getT_subject_id());
        cv.put("task_type",ct.getT_type());
        cv.put("task_due_date",ct.getT_due_date());
        cv.put("task_title",ct.getT_title());
        cv.put("task_detail",ct.getT_detail());
        db.update("table_tasks",cv,"task_id=?",new String[]{String.valueOf(ct.getTask_id())});
    }

    public int countTaskToday(String cdate){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from table_tasks where task_due_date=?",new String[]{cdate});
        int count=0;

        if (cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                count=cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    public int countClassesToday(String cdate){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from table_classes_time_child where class_date=?",new String[]{cdate});
        int count=0;

        if (cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                count=cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    public int countExamToday(String cdate){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from table_exams where exam_date=?",new String[]{cdate});
        int count=0;

        if (cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                count=cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    public List<ClassClassesTimeChild> displayTimingByAID(String aID,String dayS,String cdate){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_classes_time_child tc, table_classes_time_main tm,table_classes tcl,table_subjects ts,table_term_academic tta where tc.time_id=tm.time_id and tcl.class_id=tm.class_id and ts.subject_id=tcl.subject_id and tta.term_id=ts.term_id and tta.academic_id=? and (tc.class_date=? or tc.day_name=?)",new String[]{aID,cdate,dayS});
        List<ClassClassesTimeChild> listT=new ArrayList<ClassClassesTimeChild>();

        if(cursor.moveToFirst()){
            do {
                ClassClassesTimeChild cc=new ClassClassesTimeChild();
                cc.setDay_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("day_id"))));
                cc.setTime_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("time_id"))));
                cc.setDay_name(cursor.getString(cursor.getColumnIndex("day_name")));
                cc.setC_date(cursor.getString(cursor.getColumnIndex("class_date")));
                cc.setC_start_time(cursor.getString(cursor.getColumnIndex("class_start_time")));
                cc.setC_end_time(cursor.getString(cursor.getColumnIndex("class_end_time")));
                listT.add(cc);
            }while (cursor.moveToNext());
        }
        return listT;
    }

    public List<ClassExam> displayUpcomingExam(){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery("select * from table_exams",new String[]{});
        //Cursor cursor=db.rawQuery("select * from table_exams where exam_date<?",new String[]{getDate});
        List<ClassExam> listExam=new ArrayList<ClassExam>();

        if(cursor.moveToFirst()){
            do {
                ClassExam ce=new ClassExam();
                ce.setExma_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("exam_id"))));
                ce.setE_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                ce.setE_module(cursor.getString(cursor.getColumnIndex("exam_module")));
                ce.setE_date(cursor.getString(cursor.getColumnIndex("exam_date")));
                ce.setE_start_time(cursor.getString(cursor.getColumnIndex("exam_start_time")));
                ce.setE_seat(cursor.getString(cursor.getColumnIndex("exam_seat")));
                ce.setE_room(cursor.getString(cursor.getColumnIndex("exam_room")));
                ce.setE_duration(cursor.getString(cursor.getColumnIndex("exam_duration")));
                listExam.add(ce);
            }while (cursor.moveToNext());
        }
        return  listExam;
    }

    public List<ClassClasses> displayClassesToday(String dayS,String cdate,String acid){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_classes_time_child tc, table_classes_time_main tm,table_classes tcl,table_subjects ts,table_term_academic tta where tc.time_id=tm.time_id and tcl.class_id=tm.class_id and ts.subject_id=tcl.subject_id and tta.term_id=ts.term_id and (tc.class_date=? or tc.day_name=?) and tta.academic_id=?",new String[]{cdate,dayS,acid});
        List<ClassClasses> listC=new ArrayList<ClassClasses>();


        if(cursor.moveToFirst()){
            do {
                ClassClasses c=new ClassClasses();
                c.setClass_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("class_id"))));
                c.setClass_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                c.setClass_module(cursor.getString(cursor.getColumnIndex("class_module")));
                c.setClass_room(cursor.getString(cursor.getColumnIndex("class_room")));
                c.setClass_building(cursor.getString(cursor.getColumnIndex("class_building")));
                c.setClass_teacher(cursor.getString(cursor.getColumnIndex("class_teacher")));
                listC.add(c);
            }while (cursor.moveToNext());
        }
        return listC;
    }

    public List<ClassTasks> displayTaskToday(String cDate){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from table_tasks where task_due_date=?",new String[]{cDate});
        List<ClassTasks> listTask=new ArrayList<ClassTasks>();
        if (cursor.moveToFirst()){
            do {
                ClassTasks ct=new ClassTasks();
                ct.setTask_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("task_id"))));
                ct.setT_subject_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("subject_id"))));
                ct.setT_type(cursor.getString(cursor.getColumnIndex("task_type")));
                ct.setT_due_date(cursor.getString(cursor.getColumnIndex("task_due_date")));
                ct.setT_title(cursor.getString(cursor.getColumnIndex("task_title")));
                ct.setT_detail(cursor.getString(cursor.getColumnIndex("task_detail")));
                ct.setT_completed(cursor.getString(cursor.getColumnIndex("task_completed")));
                ct.setT_added_on(cursor.getString(cursor.getColumnIndex("task_added_on")));
                ct.setT_completed_on(cursor.getString(cursor.getColumnIndex("task_completed_on")));
                listTask.add(ct);
            }while (cursor.moveToNext());
        }
        return listTask;
    }
}
