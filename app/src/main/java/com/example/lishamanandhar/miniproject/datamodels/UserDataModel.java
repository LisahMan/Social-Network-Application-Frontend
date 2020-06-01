package com.example.lishamanandhar.miniproject.datamodels;

/**
 * Created by Lisha Manandhar on 11/4/2017.
 */

public class UserDataModel {
    String id;
    String username;
    String followField;


    public UserDataModel(){
    }

    public UserDataModel(String id , String username){
        this.id = id;
        this.username = username;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollowField() {
        return followField;
    }

    public void setFollowField(String followField) {
        this.followField = followField;
    }
}
