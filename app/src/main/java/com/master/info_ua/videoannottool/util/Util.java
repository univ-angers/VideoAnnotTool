package com.master.info_ua.videoannottool.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Util {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    /**
     * Récupère un fichier dans le repertoire assets du projet et fait le parsing en objet Java
     *
     * @param context
     * @param fichier
     * @return
     */
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

    /**
     * Récupère un fichier dans le repertoire indiqué de l'application et fait le parsing en objet Java
     *
     * @param context
     * @param dirName
     * @param fileName
     * @return
     */
    public static VideoAnnotation parseJSON(Context context, String dirName, String fileName) {

        String filePath = context.getExternalFilesDir(dirName).getAbsolutePath()+ File.separator + fileName;

        try {
            FileInputStream fis = new FileInputStream (new File(filePath));
            Reader reader = new InputStreamReader(fis);
            Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
            VideoAnnotation videoAnnotation = gson.fromJson(reader, VideoAnnotation.class);
            fis.close();
            return videoAnnotation;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_JSON", "ERREUR Lecture JSON ");
        }
        return null;
    }


    /**
     * Sauvegarde l'objet videoAnnotation dans un fichier Json
     *
     * @param context
     * @param videoAnnotation
     * @param dirPath
     * @param videoName
     */
    public static void saveVideoAnnotation(Context context, VideoAnnotation videoAnnotation, String dirPath, String videoName) {

        Writer writer;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd-MM-yyyy HH:mm:ss");
        gsonBuilder.serializeNulls(); //Ne pas ignorer les attributs avec valeur null
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        try {
            File file = new File(context.getExternalFilesDir(dirPath), videoName + ".json");

            writer = new FileWriter(file);
            Log.d("VANNOT", "Annotation saved TEST" + file);

            String jsonStr = gson.toJson(videoAnnotation);
            writer.write(jsonStr);
            writer.close();

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

    /**
     * Crée l'ensemble des repertoire spécifiés dans l'Enum "DirPath"
     * @param context
     */
    public static void createDir(Context context) {
        if (isExternalStorageWritable()) {
            for (DirPath dirPath : DirPath.values()) {
                File file = context.getExternalFilesDir(dirPath.toString());
                if (file != null) {
                    Log.e("SUCCESS", "Directory created: [" + file.getAbsolutePath() + "]");
                } else {
                    Log.e("FAIL", "Unable to create dir");
                }
            }
        }
    }


    //Vérifie si le repertoire est correcte et crée le fichier à l'endroit demandé et le renvoie sous forme de File
    public static File getFile(Context context, String repertoire, String nomFichier) {
        boolean dans_enum = false;
        DirPath[] tab = DirPath.values();
        for (int i = 0; i < tab.length; i++)
            if (tab[i].toString() == repertoire)
                dans_enum = true;
        if (dans_enum)
            return context.getExternalFilesDir(repertoire + "/" + nomFichier);
        else return null;
    }

    /**
     * Permet de vérifier la validité d'un nom de dossier: existe comme catégorie  pour l'application
     *
     * @param path
     * @return
     */
    public static boolean isAppDirectory(String path) {

        for (DirPath dirPath : DirPath.values()) {
            if (dirPath.toString().equals(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permet de vérifier la validité d'un nom de dossier: existe comme catégorie avec sous-catégorie pour l'application
     *
     * @param dirName:    nom du répertoire
     * @param subDirName: nom du sous-repertoire
     * @return
     */
    public static boolean isAppDirectory(String dirName, String subDirName) {

        String directory = dirName + File.separator + subDirName;
        for (DirPath dirPath : DirPath.values()) {
            if (dirPath.toString().equals(directory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * vérifie l'existence des sous-répertoires de l'application
     *
     * @param context
     * @return
     */
    public static boolean appDirExist(Context context) {
        File root = context.getExternalFilesDir(null);
        Log.e("ROOT", root.getAbsolutePath());
        if (root.listFiles().length > 0) {
            for (File file : root.listFiles()) {
                Log.e("CONT_FILE", file.getAbsolutePath());
            }
        } else {
            Log.e("ROOT_CONT", "No sub dir");
        }
        return root.listFiles().length > 0;
    }

    /**
     * Crée une nouvelle instance de VideoAnnotation
     * @return
     */
    public static VideoAnnotation createNewVideoAnnotation() {
        VideoAnnotation videoAnnotation = new VideoAnnotation(DATE_FORMAT.format(new Date()), DATE_FORMAT.format(new Date()), new ArrayList<Annotation>());

        return videoAnnotation;
    }

    /**
     * Récupère un objet image (Bitmap) depuis le dossier propre au contexte de l'application
     *
     * @param filename
     * @return
     */
    public static Bitmap getBitmapFromAppDir(Context context, String path, String filename) {

        Bitmap bitmap = null;
        try {
            String dirPath = context.getExternalFilesDir(path).getAbsolutePath();
            File filePath = new File(dirPath,filename);
            if (filePath.exists()){
                FileInputStream fileInputStream = new FileInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmap;
    }

    /**
     * Sauvegarde une image Bitmap dans le répertoire indiqué
     *
     * @param context
     * @param imageToSave
     * @param path chemin du répertoire de sauvegarde
     * @param fileName nom du fichier
     */
    public static void saveBitmapImage(Context context, Bitmap imageToSave,String path, String fileName) {
        final File externalFile;
        if (isExternalStorageWritable()) {
            externalFile = new File(context.getExternalFilesDir(path), fileName);
            if (externalFile.exists()) {
                externalFile.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(externalFile);
                imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Log.e("TAG", "Bitmap writed to "+externalFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
