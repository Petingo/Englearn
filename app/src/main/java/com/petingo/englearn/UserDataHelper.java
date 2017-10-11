package com.petingo.englearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;

class UserDataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyWord";
    private static final int VERSION = 1;

    UserDataHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NameList ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "Name CHAR)");

        db.execSQL("CREATE TABLE WordList ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "Word CHAR,"
                + "KK CHAR,"
                + "Translation CHAR,"
                + "listName CHAR"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    static void newWordListName(Context context, String name) {
        SQLiteDatabase db = getWritableDB(context);
        ContentValues cv = new ContentValues();
        cv.put("Name", name);
        db.insert("NameList", null, cv);
        cv.clear();
        Toast.makeText(context, context.getString(R.string.newSuccess), Toast.LENGTH_SHORT).show();
        Log.e("newWordListName", "Success");
    }

    static SQLiteDatabase getWritableDB(Context context) {
        UserDataHelper helper = new UserDataHelper(context);
        return helper.getWritableDatabase();
    }

    static SQLiteDatabase getReadableDB(Context context) {
        UserDataHelper helper = new UserDataHelper(context);
        return helper.getReadableDatabase();
    }
}

