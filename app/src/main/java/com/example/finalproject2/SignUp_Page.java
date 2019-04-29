package com.example.finalproject2;

import android.content.Intent;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SignUp_Page extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__page);
        getSupportActionBar().setTitle("Register");
    }

    private EditText username1,email1,password1;




    public void login(View view) throws SQLException {
        username1 = (EditText) findViewById(R.id.username);
        email1 = (EditText) findViewById(R.id.email);
        password1 = (EditText) findViewById(R.id.username);

        DatabaseConnection databaseConnection =new DatabaseConnection();
        databaseConnection.SignUp_Page(username1.getText().toString(),email1.getText().toString(),password1.getText().toString());
        Intent intent = new Intent(SignUp_Page.this,LoginFOrm.class);
        startActivity(intent);
    }

}
