package com.master.info_ua.videoannottool.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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
import java.util.List;


public class Util {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    //Récupère un fichier dans le répertoire assets du projet et fait le parsing en objet Java
    public static VideoAnnotation parseJSONAssets(Context context, String fichier) {
        try {
            //Lecture du fichier
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

    //Récupère un fichier dans le répertoire indiqué de l'application et fait le parsing en objet Java
    public static VideoAnnotation parseJSON(Context context, String dirName, String fileName) {
        //Récupère le chemin absolu du fichier
        String filePath = context.getExternalFilesDir(dirName).getAbsolutePath() + File.separator + fileName;
        try {
            //Lecture du fichier
            FileInputStream fis = new FileInputStream(new File(filePath));
            Reader reader = new InputStreamReader(fis);
            //Parsing du fichier
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

    //Sauvegarde l'objet videoAnnotation dans un fichier Json
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
            String jsonStr = gson.toJson(videoAnnotation);
            writer.write(jsonStr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_VANNOT", "Unable to save annotation ");
        }
    }

    //Vérifie que l'on possède une autorisation d'écriture, sinon renvoie faux
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // Crée l'ensemble des repertoire spécifiés dans l'Enum "DirPath"
    public static void createDir(Context context) {
        if (isExternalStorageWritable()) {
            for (DirPath dirPath : DirPath.values()) {
                    File file = context.getExternalFilesDir(dirPath.toString());
                //Pourquoi est-ce vide?
                if (file != null) {
                } else {
                    Log.e("FAIL", "Unable to create dir");
                }
            }
        }
    }


    //Initialise la liste d'item du spinner categorie
    public static List<Categorie> setCatSpinnerList(Context context) {
        List<Categorie> categorieList = new ArrayList<>();
        //Si le dossier existe
        if (Util.appDirExist(context)) {
            //Pour chacune des catégories et sous-catégories
            for (DirPath dirPath : DirPath.values()) {
                //Si la catégorie ne possède pas de catégorie mère
                if (!dirPath.isSubDir()) {
                    //On ajoute la catégorie à la liste de catégories
                    categorieList.add(new Categorie(dirPath.getName(), null, dirPath.toString()));
                }
            }
        } else {
            //Si le dossier n'existe pas, on le crée
            Util.createDir(context);
            //Puis, on rappelle une seconde fois la méthode
            categorieList = setCatSpinnerList(context);
        }
        return categorieList;
    }


    //Initialise la liste d'item du spinner sub-categorie
    public static List<Categorie> setSubCatSpinnerList(String parentDir) {
        List<Categorie> categorieList = new ArrayList<>();
        categorieList.add(new Categorie("Sous-categorie", null, "../"));
        //Si le nom parentDir est un nom de dossier valide
        if (Util.isAppDirectory(parentDir)) {
            //Pour chacune des catégories
            for (DirPath dirPath : DirPath.values()) {
                //Si la catégorie est une sous-catégorie et que le dossier parent correspond bien au dossier passé en paramètre
                if (dirPath.isSubDir() && dirPath.getPath().substring(0, dirPath.getPath().indexOf("/")).equals(parentDir)) {
                    //Log.e("SUB_CAT", dirPath.toString());
                    //On ajoute la sous-catégorie à la liste
                    categorieList.add(new Categorie(dirPath.getName(), parentDir, dirPath.toString()));
                }
            }
        }
        return categorieList;
    }

    //Permet de vérifier la validité d'un nom de dossier: existe comme catégorie  pour l'application
    public static boolean isAppDirectory(String path) {
        //Pour chaque catégorie
        for (DirPath dirPath : DirPath.values()) {
            //Si le chemin passé en paramètre est égal au chemin de la catégorie, on renvoie True
            if (dirPath.toString().equals(path)) {
                return true;
            }
        }
        return false;
    }

    //Permet de vérifier la validité d'un nom de dossier: existe comme catégorie avec sous-catégorie pour l'application
    public static boolean isAppDirectory(String dirName, String subDirName) {
        String directory = dirName + File.separator + subDirName;
        //Pour chaque catégorie
        for (DirPath dirPath : DirPath.values()) {
            //Si le chemin de la sous-catégorie est égal à la catégorie, on renvoie True
            if (dirPath.toString().equals(directory)) {
                return true;
            }
        }
        return false;
    }

    //Vérifie l'existence des sous-répertoires de l'application
    public static boolean appDirExist(Context context) {
        //Chemin absolu de la racine de l'appareil
        File root = context.getExternalFilesDir(null);
        Log.e("ROOT", root.getAbsolutePath());
        //Si la racine possède au moins un fichier
        if (root.listFiles().length > 0) {
            //Pour chacun de ces fichiers
            for (File file : root.listFiles()) {
                //Log?
                Log.e("CONT_FILE", file.getAbsolutePath());
            }
        }
        return root.listFiles().length > 0;
    }

    //Crée une nouvelle instance de VideoAnnotation
    public static VideoAnnotation createNewVideoAnnotation() {
        //Initialise une VideoAnnotation avec le bon format de Date
        VideoAnnotation videoAnnotation = new VideoAnnotation(DATE_FORMAT.format(new Date()), DATE_FORMAT.format(new Date()), new ArrayList<Annotation>());
        return videoAnnotation;
    }

    //Récupère un objet image (Bitmap) depuis le dossier propre au contexte de l'application
    public static Bitmap getBitmapFromAppDir(Context context, String path, String filename) {
        Bitmap bitmap = null;
        try {
            String dirPath = context.getExternalFilesDir(path).getAbsolutePath();
            File filePath = new File(dirPath, filename);
            if (filePath.exists()) {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    //Sauvegarde une image Bitmap dans le répertoire indiqué
    public static void saveBitmapImage(Context context, Bitmap imageToSave, String path, String fileName) {
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
                Log.e("TAG", "Bitmap writed to " + externalFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //Renvoie le chemin du fichier 'uploader'
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String filePath = "";
        if (contentUri.getHost().contains("com.android.providers.media")) {
            String wholeID = DocumentsContract.getDocumentId(contentUri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Video.Media.DATA};
            // where id is equal to
            String sel = MediaStore.Video.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else {
            return  getRealPathFromURI_BelowAPI11(context,contentUri);
        }
    }

    //Permet la sauvegarde d'un fichier vidéo importé dans le repertoire désigné par la catégorie et sous catégorie indiquées
    public static void saveImportVideoFile(Context context, Categorie subCatDir, File videoFile) {
        try {
            // Dossier source
            File subDirContent = context.getExternalFilesDir(subCatDir.getPath());
            // Dossier de destination
            File videoFileDirectory = new File(subDirContent,FilenameUtils.removeExtension(videoFile.getName()));
            //Création du dossier de destination
            videoFileDirectory.mkdir();
            //Copie de la vidéo du dossier source au dossier de destination
            FileUtils.copyFileToDirectory(videoFile, videoFileDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
