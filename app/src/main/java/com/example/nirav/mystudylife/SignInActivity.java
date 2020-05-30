package com.example.nirav.mystudylife;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    Button btn_sign_up;
    EditText txt_uname,txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_in);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        txt_uname=(EditText)findViewById(R.id.signintxtemail);
        txt_password=(EditText)findViewById(R.id.signintxtpassword);


        btn_sign_up=(Button)findViewById(R.id.signupnav);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });


    }

    public void onLogin(View view){
        String u_email=txt_uname.getText().toString();
        String u_pass=txt_password.getText().toString();

        String type="login";
        BackgroundWorker backgroundWorker=new BackgroundWorker(this);
        backgroundWorker.execute(type,u_email,u_pass);

    }


}
