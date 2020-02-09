package com.master.info_ua.videoannottool.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.master.info_ua.videoannottool.custom.Difficulte;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
            reader.close();
            fis.close();
            return videoAnnotation;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_JSON", "ERREUR Lecture JSON ");
        }
        return null;
    }


    //Récupère un fichier dans le répertoire indiqué de l'application et fait le parsing en objet Java
    public static List<Annotation> parseJSON_Annot(Context context) {
//        //Récupère le chemin absolu du fichier
//        String filePath = context.getExternalFilesDir("annotations").getAbsolutePath() + File.separator + "AnnotPredef_num_"+annotNum+".json";
        //Permet de sélectionner uniquement les fichiers JSON (chacun d'entre eux correspond à une annotation prédéfinie)
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".json");
            }
        };
        List<Annotation> annotList = new ArrayList<>();
        List<File> annotFileList = Arrays.asList(context.getExternalFilesDir("annotations").listFiles(filter));
        for (File file : annotFileList) {
            try {
                //Lecture du fichier
                FileInputStream fis = new FileInputStream(new File(file.getPath()));
                Reader reader = new InputStreamReader(fis);
                //Parsing du fichier
                Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
                Annotation annotPredef = gson.fromJson(reader, Annotation.class);
                fis.close();
                annotList.add(annotPredef);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERR_JSON", "ERREUR Lecture JSON ");
            }
        }
        return annotList;
    }

    public static Difficulte parseJSON_Difficulte(Context context) {
        String filePath = context.getExternalFilesDir("difficulte").getAbsolutePath() + File.separator + "videodifficulte.json";
        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
//            Reader reader = new FileReader(file);
            //Le fichier à le même nombre de caractère que sa taille
            String content = "";
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }

//                char[] a = new char[(int)file.length()];
//            reader.read(a);
            //On sauvegarde les données caractère par caractère
//            for (char c : a) {
//                content += c;
//            }
            System.out.println("Contenu du fichier JSON " + content) ;
            Difficulte difficulte = new Difficulte(content);
            reader.close();
            return difficulte;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_JSON", "ERREUR Lecture JSON ");
        }
        return null;
    }

    //Sauvegarde l'objet Annotation dans un fichier Json dans le dossier des annotations prédéfinis
    public static void saveAnnotationPredef(Context context, Annotation annotPredef) {
        Writer writer;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd-MM-yyyy HH:mm:ss");
        gsonBuilder.serializeNulls(); //Ne pas ignorer les attributs avec valeur null
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        annotPredef.setDrawFileName(annotPredef.getAnnotationTitle()+".png");
        try {
            File file = new File(context.getExternalFilesDir("annotations"),annotPredef.getAnnotationTitle() + ".json");
            writer = new FileWriter(file);
            String jsonStr = gson.toJson(annotPredef);
            writer.write(jsonStr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_VANNOT", "Unable to save ListAnnotationPredef ");
        }
    }

    //Sauvegarde de l'objet d'association video difficulte en JSON
    public static void saveNewVideoDifficulte(Context context, String videoName, String difficulte) {
        Writer writer;
        try {
            File file = new File(context.getExternalFilesDir("difficulte"), "videodifficulte.json");
            writer = new FileWriter(file, true);
            Difficulte d = parseJSON_Difficulte(context);

            for (Difficulte.Association a : d.getAssociations()) {
                System.out.println("name = " + a.getVideoName() + " difficulte = " + a.getDifficulte());
            }

            d.add(videoName, difficulte);
            String jsonStr = d.toJson();
            writer.write(jsonStr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_SAVEDIFF", "Unable to save Difficulte");
        }
    }


    //Sauvegarde de la modification du nom d'une vidéo
    public static void saveEditVideoName(Context context, final String videoName, String newVideoName) {
        Writer writer;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson =gsonBuilder.create();
        try {
            File file = new File(context.getExternalFilesDir("difficulte"), "videodifficulte.json");
            writer = new FileWriter(file);
            Difficulte d = parseJSON_Difficulte(context);
            d.editVideoName(videoName,newVideoName);
            String jsonStr = gson.toJson(d);
            writer.write(jsonStr);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_EDITNAME", "Unable to save edit video name");
        }
    }


    //Sauvegarde de la modification du niveau de difficulte d'une vidéo
    public static void saveEditDifficulte(Context context, final String videoName, int difficulte) {
        Writer writer;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson =gsonBuilder.create();
        try {
            File file = new File(context.getExternalFilesDir("difficulte"), "videodifficulte.json");
            writer = new FileWriter(file);
            Difficulte d = parseJSON_Difficulte(context);
            String difficulteString = "" + difficulte;
            d.editDifficulte(videoName,difficulteString);
            String jsonStr = gson.toJson(d);
            writer.write(jsonStr);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_EDITNAME", "Unable to save edit video difficulte");
        }
    }


    //Sauvegarde de la suppression d'une vidéo
    public static void saveRemoveVideo(Context context, final String videoName) {
        Writer writer;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson =gsonBuilder.create();
        try {
            File file = new File(context.getExternalFilesDir("difficulte"), "videodifficulte.json");
            writer = new FileWriter(file);
            Difficulte d = parseJSON_Difficulte(context);
            d.remove(videoName);
            String jsonStr = gson.toJson(d);
            writer.write(jsonStr);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERR_REMOVE", "Unable to save remove video difficulte");
        }
    }

    public static int getDifficulteFromJSON(Context context, String videoName) {
        Difficulte d = parseJSON_Difficulte(context);
        return d.getDifficulte(videoName);
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

    public static void deleteRecursiveDirectory(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursiveDirectory(child);
            }
        }
        System.out.println("Suppression "+fileOrDirectory.getAbsolutePath() +"    "+ fileOrDirectory.delete());
    }


    //Initialise la liste d'item du spinner categorie
    public static List<Categorie> setCatSpinnerList(Context context) {
        List<Categorie> categorieList = new ArrayList<>();
        //Si le dossier existe
        if (Util.appDirExist(context)) {
            //Pour chacune des catégories
            for (DirPath dirPath : DirPath.values()) {
                if (!dirPath.isSubDir()) {
                    //On ajoute la catégorie à la liste de catégories
                    categorieList.add(new Categorie(dirPath.getName(), null, dirPath.toString()));
                }
            }
            //Pour chacune des sous-catégories
            for(DirPath dirPath : DirPath.values()) {
                if(dirPath.isSubDir()) {
                    File subDir = new File(dirPath.getPath());
                    //Récupère le nom de la catégorie mère
                    String categorieMere = subDir.getParent();
                    for (Categorie categorie : categorieList) {
                        System.out.println(categorie.getName() + " = " + categorieMere + "?");
                        //Si dans la liste de catégorie, il existe une catégorie correspondant à la catégorie mère
                        if (categorie.getName().equals(categorieMere.toUpperCase())) {
                            System.out.println("OK");
                            //On récupère sa liste de sous-catégories
                            List<Categorie> listSubCat = categorie.getSubCategories();
                            //On ajoute la nouvelle
                            listSubCat.add(new Categorie(subDir.getName(), categorieMere.toUpperCase(), subDir.getPath()));
                            //On met à jour la liste
                            categorie.setSubCategories(listSubCat);
                            System.out.println("Ajout de " + subDir.getName() + " à " + categorieMere.toUpperCase() + " : taille " + categorie.getSubCategories().size());
                        }
                    }
                }
            }
        } else {
            //Si le dossier n'existe pas, on le crée
            Util.createDir(context);
            //Puis, on rappelle une seconde fois la méthode
            categorieList = setCatSpinnerList(context);
        }
        return categorieList;
    }    //Initialise la liste d'item du spinner sub-categorie


    public static List<Categorie> initCatList(Context context) {
        List<Categorie> categorieList = new ArrayList<>();
        Categorie newCat;
        //Si le dossier existe
        File Dir = context.getExternalFilesDir("");
        if (Util.appDirExist(context)) {
            //Pour chacune des catégories et sous-catégories
            for (File catDir : Dir.listFiles()) {
                if ( ( !catDir.getName().matches("annotations") ) && ( !catDir.getName().matches("difficulte") ) ) {
                    newCat = new Categorie(catDir.getName(), null, catDir.getName());
                    for (File subCatDir : catDir.listFiles()) {
                        newCat.getSubCategories().add(new Categorie(subCatDir.getName(), newCat.getName(), newCat.getName() + "/" + subCatDir.getName()));
                    }
                    categorieList.add(newCat);
                }
            }
        }
        return categorieList;
    }

/*  Réinitialisation après modification des catégories */
    public static void reInitCatList(Context context, List<Categorie> liste) {
        File Dir = context.getExternalFilesDir("");
        if (Util.appDirExist(context)) {
            for (File catDir : Dir.listFiles()) {
                for(int i=0;i<liste.size();i++){
//          On vérifie si les catégories sont conservées sinon on supprime tout le répertoire
                    if(catDir.getName().matches(liste.get(i).getName())){
//          On vérifie également si des sous-catégories sont conservées ou supprimées
                        for(File subCatDir: catDir.listFiles()){
                            for(int j=0;j<liste.size();j++) {
                                if(subCatDir.getName().matches(liste.get(j).getName())){
                                    break;
                                }
                                if(j==liste.size()-1)
                                {
                                    deleteRecursiveDirectory(subCatDir);
                                }
                            }
                        }
                        break;
                    }
                    if(i==liste.size()-1)
                    {
                        deleteRecursiveDirectory(catDir);
                    }
                }
            }
        }
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

    //Permet de fermer le clavier après un input.
    public static void FermerClavier(View view){
        //Cache le clavier de la tablette
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);

        Log.e("test input mode", imm.)

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}
