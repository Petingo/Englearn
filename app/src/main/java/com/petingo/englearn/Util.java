package com.petingo.englearn;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Petingo on 2017/4/29.
 */

class Util {
    static void copyDB(Context context){
        String DATABASE_PATH = "/data/data/com.petingo.englearn/databases/";
        String DATABASE_FILENAME = "ecdict";

        String databaseFilename = DATABASE_PATH + DATABASE_FILENAME;
        File dir = new File(databaseFilename);

        if (!dir.exists()) {
            dir.mkdir();
        }

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(databaseFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStream is = context.getResources().openRawResource(R.raw.ecdict);
        byte[] buffer = new byte[8192];
        int count;
        try {
            while ((count = is.read(buffer)) > 0) {

                if (os != null) {
                    os.write(buffer, 0, count);
                    os.flush();
                } else {
                    Log.e("os","NULL");
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        try {
            is.close();
            if (os != null) {
                os.close();
            } else {
                Log.e("os","NULL");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
