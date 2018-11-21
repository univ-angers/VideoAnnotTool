package com.example.etudiant.videoannottool.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.etudiant.videoannottool.annotation.VideoAnnotation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class Util {



    public static VideoAnnotation parseJSON(String fichier, Context context) {

        try {
            InputStream inputStream = context.getAssets().open(fichier);

            Reader reader = new InputStreamReader(inputStream);
            Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
            VideoAnnotation videoAnnotation = gson.fromJson(reader, VideoAnnotation.class);
            return videoAnnotation;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public static File getPublicStorageDir(String dirName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), dirName);
        if (!file.mkdirs()) {
            Log.e("Erreur fichier", "Directory not created");
        }
        return file;
    }
    //getFreeSpace()     comparer les tailles

    public static void initFiles(File dir){
        File file1 = new File(dir,"cat1");
        File file2 = new File(dir,"cat2");

        file1.mkdir();
        file2.mkdir();

    }



}
