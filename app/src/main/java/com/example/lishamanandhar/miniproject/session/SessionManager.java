package com.example.lishamanandhar.miniproject.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lisha Manandhar on 10/24/2017.
 */

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "logIn";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String USER = "user";
    private static final String USERNAME = "username";

    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN,isLoggedIn);
        editor.commit();
    }

    public void setUser(String user){
        editor.putString(USER,user);
        editor.commit();
    }

    public void setUsername(String name){
        editor.putString(USERNAME,name);
        editor.commit();
    }

    public String getUsername(){
        return pref.getString(USERNAME,null);
    }

    public String getUser(){
        return pref.getString(USER,null);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN,false);

    }

    public void logOut(){
        editor.clear().commit();
    }

}
