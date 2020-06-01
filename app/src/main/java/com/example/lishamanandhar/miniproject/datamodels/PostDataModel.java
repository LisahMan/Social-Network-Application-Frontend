package com.example.lishamanandhar.miniproject.datamodels;

/**
 * Created by Lisha Manandhar on 10/29/2017.
 */

public class PostDataModel {

   public static final int POST_TYPE = 0;
   public static final int POST_LIST_TYPE = 1;

    private String user;
    private String username;
    private String id;
    private String data;
    private String saved;
    private int type;

    public PostDataModel(String user , String username ,String id, String data ,String saved, int type){
        this.user = user;
        this.username = username;
        this.id = id;
        this.data = data;
        this.saved = saved;
        this.type = type;
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
