package com.example.lishamanandhar.miniproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lisha Manandhar on 11/22/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    final public static String DBNAME = "db_social_net";
    final public static int VERSION = 1;
    final public static String TABLE = "tbl_saved_post";
    final public static String ID = "id";
    final public static String USER = "user";
    final public static String USERNAME = "username";
    final public static String POSTID = "post_id";
    final public static String BODY = "body";


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE IF NOT EXISTS " + TABLE + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        USER + " TEXT , " +
                        USERNAME + " TEXT , " +
                        POSTID + " TEXT , " +
                        BODY + " TEXT ); ";

        sqLiteDatabase.execSQL(query);




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
