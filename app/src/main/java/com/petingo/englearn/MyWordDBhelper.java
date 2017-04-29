package com.petingo.englearn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

class MyWordDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="MyWord";

    private static final int VERSION=1;

    MyWordDBHelper(Context context){
        super(context, DATABASE_NAME, null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NameList ("
                +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "Name CHAR)");

        String TABLE_NAME = "WordList";
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                +_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "Eng CHAR,"
                + "KK CHAR,"
                + "Chi CHAR,"
                + "example CHAR"
                + "tableID INTEGER"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}

