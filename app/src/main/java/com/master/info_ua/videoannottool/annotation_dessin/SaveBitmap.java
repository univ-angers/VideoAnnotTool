package com.master.info_ua.videoannottool.annotation_dessin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class SaveBitmap {

    static File externalFile;

    /*
    public static Bitmap readBitmapFromFile(Context context, String fileName) {
        //JSONArray jsonArray = null;
        Bitmap bitmap;
        InputStream stream;
        InputStreamReader streamReader;
        //Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
        Gson gson = new GsonBuilder().create();
        try {

            if (isExternalStorageWritable()){
                externalFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
            }

            FileInputStream fis = new FileInputStream(externalFile);
            DataInputStream in = new DataInputStream(fis);
            stream = context.getAssets().open(fileName);
            streamReader = new InputStreamReader(stream);



            bitmap = gson.fromJson(streamReader, Bitmap.class);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return bitmap;
    }

*/
    public static void writeJsonDataFile(Context context, Bitmap bitmap, String fileName) {

        FileOutputStream outputStream;

        try {

            if (isExternalStorageWritable()){
                externalFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

                String bitmapStr = getStringFromBitmap(bitmap);

                outputStream = new FileOutputStream(externalFile);
                outputStream.write(bitmapStr.getBytes());
                outputStream.close();

                Log.e("TAG", "Writed to "+externalFile.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmapImage(Context context, Bitmap imageToSave,String path, String fileName) {

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


    public static Bitmap getBitmapFromAppDir(Context context, String fileName) {

        Bitmap bitmap = null;
        try {
            externalFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            if (externalFile.exists()){
                FileInputStream fileInputStream = new FileInputStream(externalFile);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.d("TAG", "Bitmap read from "+externalFile.getAbsolutePath());

        return bitmap;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    /*
     * This functions converts Bitmap picture to a string which can be
     * JSONified.
     * */
    protected static String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
}
