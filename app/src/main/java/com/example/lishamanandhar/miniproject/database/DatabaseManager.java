package com.example.lishamanandhar.miniproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lishamanandhar.miniproject.datamodels.PostDataModel;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 11/22/2017.
 */

public class DatabaseManager {

    SQLiteDatabase db;
    private static DatabaseManager ourInstance;

    public static DatabaseManager getInstance(Context context) {

        if(ourInstance==null){
           ourInstance = new DatabaseManager(context);
        }
        return ourInstance;

    }

    private DatabaseManager(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    public void savePost(PostDataModel postDataModel){
        Log.i("saved username",postDataModel.getUsername());
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.USER,postDataModel.getUser());
        cv.put(DatabaseHelper.USERNAME,postDataModel.getUsername());
        cv.put(DatabaseHelper.POSTID,postDataModel.getId());
        cv.put(DatabaseHelper.BODY,postDataModel.getData());
        if(!tableExists(postDataModel.getUsername(),postDataModel.getId())) {
            long l = db.insert(DatabaseHelper.TABLE, null, cv);
        }
    }

    public boolean tableExists(String username , String postId){

        String query = "SELECT " + DatabaseHelper.USERNAME + " FROM " + DatabaseHelper.TABLE + " WHERE " +
                DatabaseHelper.USERNAME + " = '" + username + "'" + " AND " + DatabaseHelper.POSTID + " = '" + postId + "'";

        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0) {
            return false;
        }
        return true;

    }

    public ArrayList<PostDataModel> fetchSavedPost(){
       ArrayList<PostDataModel> postList = new ArrayList<>();
       String query = "SELECT * FROM " + DatabaseHelper.TABLE + ";";

       Cursor c = db.rawQuery(query,null);

        while(c.moveToNext()){
          String user = c.getString(c.getColumnIndex(DatabaseHelper.USER));
          String username = c.getString(c.getColumnIndex(DatabaseHelper.USERNAME));
          String postId = c.getString(c.getColumnIndex(DatabaseHelper.POSTID));
          String body = c.getString(c.getColumnIndex(DatabaseHelper.BODY));
          PostDataModel postDataModel = new PostDataModel(user,username,postId,body,"",PostDataModel.POST_LIST_TYPE);
          postList.add(postDataModel);
        }

        return postList;

    }

    public void unSavePost(String postId){
       String query = "DELETE FROM " + DatabaseHelper.TABLE + " WHERE " +
                      DatabaseHelper.POSTID + " = \"" + postId + "\"" + ";" ;

        db.execSQL(query);

    }




}
