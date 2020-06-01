package com.example.lishamanandhar.miniproject.json_classes;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 10/29/2017.
 */

public class Post {

    private String user;
    private String username;
    private String body;
    private String date;
    private String saved;
    private ArrayList<Comment> comList;

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Comment> getComList() {
        return comList;
    }

    public void setComList(ArrayList<Comment> comList) {
        this.comList = comList;
    }
}
