package com.example.lishamanandhar.miniproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lishamanandhar.miniproject.application.MyApplication;
import com.example.lishamanandhar.miniproject.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lisha Manandhar on 10/24/2017.
 */

public class LoginActivity extends AppCompatActivity {

    EditText etlogusername , etlogpassword;
    Button btnlogin , btngotoregister;
    MyApplication myApp;
    SessionManager session;
    String url = "http://172.16.4.246:8000/user/login/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        etlogusername = (EditText) findViewById(R.id.etlogusername);
        etlogpassword = (EditText) findViewById(R.id.etlogpassword);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btngotoregister = (Button) findViewById(R.id.btngotoregister);
        myApp = MyApplication.getInstance();
        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etlogusername.getText().toString().trim();
                String password = etlogpassword.getText().toString().trim();

                if(username.isEmpty()){
                    etlogusername.setError("Enter Username");
                }
                else if(password.isEmpty()){
                    etlogpassword.setError("Enter password");
                }
                else{
                    checkLogin(username,password);
                }
            }
        });

        btngotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(LoginActivity.this , RegisterActivity.class);
               startActivity(i);
                finish();
            }
        });


    }

    private void checkLogin(final String username ,final String password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,onSuccess,onError){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params = new HashMap<>();
            params.put("username",username);
            params.put("password",password);
            return params;
            }
        };

        myApp.getRequestQueue().add(stringRequest);

    }

    Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i("response",response);

            if(!response.contains("error")){
              session.setLogin(true);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    session.setUsername(jsonObject.getString("username"));
                    session.setUser(jsonObject.getString("id"));

                    Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
         Toast.makeText(getApplicationContext(),"User doesn't exists",Toast.LENGTH_SHORT).show();
        }
    };


}
