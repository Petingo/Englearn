package com.petingo.englearn;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Petingo on 2017/4/29.
 */

public class utli {
    public void copyFile (String destinationPath, String fileName, int rawResource, Context context) {
        String destinationAndFileName = destinationPath + fileName;
        File dir = new File(destinationPath);

        if (!dir.exists())
            dir.mkdir();

        FileOutputStream os = null;
        try {

            os = new FileOutputStream(destinationAndFileName);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        InputStream is = context.getResources().openRawResource(rawResource);
        byte[] buffer = new byte[8192];
        int count;
        // 开始复制db文件
        try {
            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
                os.flush();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        try {
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
