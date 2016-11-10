package com.petingo.englearn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

public class ECdicDBhelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="ecdict";

    public static final int VERSION=1;

    public ECdicDBhelper(Context context){
        super(context, DATABASE_NAME, null,VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int i=0;i<26;i++){
            String TABLE_NAME = "list_"+(char)(65+i);
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    +_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "Eng CHAR,"
                    + "KK CHAR,"
                    + "Chi CHAR,"
                    + "example CHAR"
                    + ")");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
