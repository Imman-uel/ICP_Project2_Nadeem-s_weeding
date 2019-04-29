package com.example.finalproject2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginFOrm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        getSupportActionBar().setTitle("Login Page");
    }

    public void signup(View view){
        Intent intent = new Intent(LoginFOrm.this,SignUp_Page.class);
        startActivity(intent);
    }
}
