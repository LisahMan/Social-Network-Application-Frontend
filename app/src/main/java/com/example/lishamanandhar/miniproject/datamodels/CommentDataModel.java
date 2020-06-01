package com.example.lishamanandhar.miniproject.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lisha Manandhar on 10/29/2017.
 */

public class CommentDataModel {


    public static final int COMMENT_TYPE = 2;
    public static final int COMMENT_LIST_TYPE = 3;

    private String user;
    private String username;
    private String data;
    private int type;

    public CommentDataModel(String user , String username , String data , int type){
        this.user = user;
        this.username = username;
        this.data = data;
        this.type = type;
    }





    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
