package com.example.nirav.mystudylife;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText txtFname,txtLname,txtPassword,txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SignUp");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtFname=(EditText)findViewById(R.id.signuptxtfirstname);
        txtLname=(EditText)findViewById(R.id.signuptxtlastname);
        txtPassword=(EditText)findViewById(R.id.signuptxtpassword);
        txtEmail=(EditText)findViewById(R.id.signuptxtemail);

    }

    public void OnLogin(View view){

        String uname=txtFname.getText().toString();
        String lname=txtLname.getText().toString();
        String pass=txtPassword.getText().toString();
        String email=txtEmail.getText().toString();

        if((uname.equals(null))|| (uname.equals(""))){
            txtFname.setError("Enter First Name");
        }else
        if((lname.equals(null))|| (lname.equals(""))){
            txtLname.setError("Enter First Name");
        }
        else
        if(!isValidEmail(email)){
            txtEmail.setError("Invalid Email");
        }
        else
        if(!isValidPassword((pass))){
            txtPassword.setError("Invalid Password");
        }
        else {
            String type = "register";

            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, uname, lname, pass, email);
        }

    }

    public boolean isValidEmail(String email){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern  pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher  matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 5) {
            return true;
        }
        return false;
    }


}
