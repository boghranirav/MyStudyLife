package com.example.nirav.mystudylife;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    ListView list_setting;
    String[] set_array={"General"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.layout_setting_list,R.id.txt_view_setting,set_array);
        list_setting=(ListView)findViewById(R.id.setting_list);
        list_setting.setAdapter(adapter);

        list_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemP=i;
                String itemV=(String)list_setting.getItemAtPosition(itemP);

                switch (itemP){
                    case 0:
                        Intent in=new Intent(SettingActivity.this,GeneralSettingActivity.class);
                        startActivity(in);
                        break;
                }
            }
        });

    }

}
