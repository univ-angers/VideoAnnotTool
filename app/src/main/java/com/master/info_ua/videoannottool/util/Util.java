package com.master.info_ua.videoannottool.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.master.info_ua.videoannottool.annotation.DirPath;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
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


    public static void creerFichier(DirPath repertoire,String nomFichier,Context context){
        boolean dans_enum=false;
        DirPath[] tab = DirPath.values();
        for(int i=0;i<tab.length;i++)
            if(tab[i].toString()==repertoire.toString())
                dans_enum=true;
        if(dans_enum)
            context.getExternalFilesDir(repertoire.toString()+nomFichier);

    }



}
