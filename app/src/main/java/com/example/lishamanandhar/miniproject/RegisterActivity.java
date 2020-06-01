package com.example.lishamanandhar.miniproject;

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

public class RegisterActivity extends AppCompatActivity {

    EditText etRegUsername , etRegMobileNumber , etRegPassword , etRegRePassword;
    Button btnRegister , btngotologin;
    SessionManager session;
    MyApplication myApp;
    String url = "http://172.16.4.246:8000/user/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        etRegUsername = (EditText) findViewById(R.id.etregusername);
        etRegMobileNumber = (EditText) findViewById(R.id.etregmobile);
        etRegPassword = (EditText) findViewById(R.id.etregpassword);
        etRegRePassword = (EditText) findViewById(R.id.etregrepassword);
        btnRegister = (Button) findViewById(R.id.btnregister);
        btngotologin = (Button) findViewById(R.id.btngotologin);
        myApp = MyApplication.getInstance();

        session = new SessionManager(getApplicationContext());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etRegUsername.getText().toString().trim();
                String password = etRegPassword.getText().toString().trim();
                String mobileNumber = etRegMobileNumber.getText().toString().trim();
                String rePassword = etRegRePassword.getText().toString().trim();

                if(username.isEmpty()){
                    etRegUsername.setError("Enter Username");
                }
                else if(mobileNumber.isEmpty()){
                    etRegMobileNumber.setError("Enter mobile number");
                }
                else if(mobileNumber.length() >10){
                    etRegMobileNumber.setError("Mobile number is greater than 10");
                }
                else if(mobileNumber.length() <10){
                    etRegMobileNumber.setError("Mobile number is less than 10");
                }
                else if(password.isEmpty()){
                    etRegPassword.setError("Enter Password");
                }
                else if(password.length() < 8){
                    etRegPassword.setError("Password Should atleast be 8 characters");
                }
                else if(rePassword.isEmpty()){
                    etRegRePassword.setError("Enter Confirm Password");
                }
                else if(!rePassword.equals(password)){
                    etRegRePassword.setError("Password Doesn't match");
                }
                else{
                    register(username,mobileNumber,password);
                }
            }
        });

        btngotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this , LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void register(final String username , final String mobile_numner , final String password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,onSuccess,onError){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                params.put("phone_number",mobile_numner);
                return params;
            }
        };
        myApp.getRequestQueue().add(stringRequest);

    }

    Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i("response2",response);

            session.setLogin(true);

            try {
                JSONObject jsonObject = new JSONObject(response);
                session.setUsername(jsonObject.getString("username"));
                session.setUser(jsonObject.getString("id"));

                Intent i = new Intent(RegisterActivity.this , MainActivity.class);
                startActivity(i);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(RegisterActivity.this , "Username already taken or mobile number taken ",Toast.LENGTH_SHORT).show();
        }
    };
}
