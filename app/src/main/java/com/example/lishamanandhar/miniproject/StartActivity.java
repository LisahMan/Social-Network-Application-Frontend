package com.example.lishamanandhar.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.lishamanandhar.miniproject.session.SessionManager;

/**
 * Created by Lisha Manandhar on 11/14/2017.
 */

public class StartActivity extends AppCompatActivity {
    Button btnEnterLogin , btnEnterRegister;
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.start_activity);
        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()){
            Intent intent = new Intent(StartActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        btnEnterLogin = (Button) findViewById(R.id.btnenterlogin);
        btnEnterRegister = (Button) findViewById(R.id.btnenterregister);
        btnEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this , LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnEnterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
