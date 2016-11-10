package com.petingo.englearn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

/**
 * Created by Petingo on 2016/10/2.
 */

public class MyWordDBhelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME="MyWord";

    public static final int VERSION=1;

    public MyWordDBhelper(Context context){
        super(context, DATABASE_NAME, null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int i=0;i<26;i++){
            String TABLE_NAME = "NameList";
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    +_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "name CHAR,"
                    + ")");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}

