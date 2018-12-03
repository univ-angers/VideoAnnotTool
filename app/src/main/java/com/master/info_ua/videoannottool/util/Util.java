package com.master.info_ua.videoannottool.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.master.info_ua.videoannottool.annotation.DirPath;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;


public class Util {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static VideoAnnotation parseJSONAssets(Context context, String fichier) {

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

    public static VideoAnnotation parseJSON(Context context,String dirName, String fileName) {

        try {
            File file = context.getExternalFilesDir(dirName+"/"+fileName);
            FileInputStream fileInputStream = new FileInputStream(file);

            Reader reader = new InputStreamReader(fileInputStream);
            Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
            VideoAnnotation videoAnnotation = gson.fromJson(reader, VideoAnnotation.class);
                Log.d("ERR_JSON", "Lecture JSON OK ");
            return videoAnnotation;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_JSON", "ERREUR Lecture JSON ");

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
    public static void saveVideoAnnotation(Context context, VideoAnnotation videoAnnotation, String dirPath, String videoName){
        if(isAppDirectory(dirPath)) {
            Writer writer;

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd-MM-yyyy HH:mm:ss");
            gsonBuilder.serializeNulls(); //Ne pas ignorer les attributs avec valeur null
            gsonBuilder.setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            try {
                File file = new File(context.getExternalFilesDir(dirPath + "/" + videoName),videoName + ".json");


                writer = new FileWriter(file );
                Log.d("VANNOT", "Annotation saved TEST"+ file);

                String jsonStr = gson.toJson(videoAnnotation);
                writer.write(jsonStr);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERR_VANNOT", "Unable to save annotation ");
            }
        }else
            Log.d("ERR_REP","chemin du repertoire erroné");
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
            File file = context.getExternalFilesDir(DirPath.CATEGORIE1_SUB1.toString());
            if(file != null){
                Log.e("SUCCESS", "Directory created: ["+file.getAbsolutePath()+"]");
            }else{
                Log.e("FAIL", "Unable to create dir");
            }
        }
    }



    //Vérifie si le repertoire est correcte et crée le fichier à l'endroit demandé et le renvoie sous forme de File
    public static File getFile(Context context,String repertoire,String nomFichier){
        boolean dans_enum=false;
        DirPath[] tab = DirPath.values();
        for(int i=0;i<tab.length;i++)
            if(tab[i].toString()==repertoire)
                dans_enum=true;
        if(dans_enum)
            return context.getExternalFilesDir(repertoire+"/"+nomFichier);
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



}
