package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by buxiaohui on 6/15/17.
 */

public class FileUtils {
    public static void writeStr2File(String content,String fileName){

        FileOutputStream fop = null;
        File file = null;
        if(content == null){
            return;
        }
        try {

            file = new File(fileName);
            fop = new FileOutputStream(file);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
