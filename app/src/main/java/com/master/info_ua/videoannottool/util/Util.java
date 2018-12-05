package com.master.info_ua.videoannottool.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.master.info_ua.videoannottool.annotation.DirPath;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;


public class Util {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static VideoAnnotation parseJSON(Context context, String fichier) {

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

    /**
     * Sauvegarde l'objet videoAnnotation dans un fichier Json
     * @param context
     * @param videoAnnotation
     * @param dirPath
     * @param videoName
     */
    public static void saveVideoAnnotaion(Context context, VideoAnnotation videoAnnotation, String dirPath, String videoName){

        Writer writer;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd-MM-yyyy HH:mm:ss");
        gsonBuilder.serializeNulls(); //Ne pas ignoré les attributs avec valeur null
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        try {
            writer = new FileWriter(context.getExternalFilesDir(dirPath).getAbsolutePath()+"/"+videoName+".json");
            String jsonStr = gson.toJson(videoAnnotation);
            writer.write(jsonStr);
            writer.close();
            Log.e("VANNOT", "Annotation saved ");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_VANNOT", "Unable to save annotation ");
        }
    }


    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static void createDir(Context context){
        if (isExternalStorageWritable()){
            for (DirPath dirPath : DirPath.values()){
                File file = context.getExternalFilesDir(dirPath.toString());
                if(file != null){
                    Log.e("SUCCESS", "Directory created: ["+file.getAbsolutePath()+"]");
                }else{
                    Log.e("FAIL", "Unable to create dir");
                }
            }
        }
    }


    public static File getFile(DirPath repertoire,String nomFichier,Context context){
        boolean dans_enum=false;
        DirPath[] tab = DirPath.values();
        for(int i=0;i<tab.length;i++)
            if(tab[i].toString()==repertoire.toString())
                dans_enum=true;
        if(dans_enum)
            return context.getExternalFilesDir(repertoire.toString()+"/"+nomFichier);
        else return null;
    }

    /**
     * Permet de vérifier la validité d'un nom de dossier: existe comme catégorie  pour l'application
     *
     * @param path
     * @return
     */
    public static boolean isAppDirectory(String path){

        for (DirPath dirPath : DirPath.values()){
            if (dirPath.toString().equals(path)){
                return true;
            }
        }
        return false;
    }

    /**
     * Permet de vérifier la validité d'un nom de dossier: existe comme catégorie avec sous-catégorie pour l'application
     * @param dirName: nom du répertoire
     * @param subDirName: nom du sous-repertoire
     * @return
     */
    public static boolean isAppDirectory(String dirName, String subDirName){

        String directory = dirName+File.separator+subDirName;
        for (DirPath dirPath : DirPath.values()){
            if (dirPath.toString().equals(directory)){
                return true;
            }
        }
        return false;
    }

    /**
     * vérifie l'existence des sous-répertoires de l'application
     * @param context
     * @return
     */
    public static boolean appDirExist(Context context){
        File root = context.getExternalFilesDir(null);
        Log.e("ROOT", root.getAbsolutePath());
        if(root.listFiles().length > 0){
            for (File file: root.listFiles()){
                Log.e("CONT_FILE", file.getAbsolutePath());
            }
        }else {
            Log.e("ROOT_CONT", "No sub dir");
        }
        return root.listFiles().length > 0;
    }

}
