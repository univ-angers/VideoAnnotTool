package com.master.info_ua.videoannottool.annotation;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Classe gerant la lecture audio
 */
public class Audio {
    private String audioPath; //Chemin vers le fichier audio
    private Context mainActivity;

    /**
     *  Constructeur de la classe
     * @param context
     * @param path Chemin vers le fichier audio à lire
     */
    public Audio(Context context,String path){
       this.mainActivity=context;
       this.audioPath=path;
    }
    /**
     * Lis le fichier audio
     */
    public void listen() {
        MediaPlayer AudioPlayer;
        try{

            AudioPlayer= new MediaPlayer();
            AudioPlayer.setDataSource(audioPath);

            AudioPlayer.prepare();
            AudioPlayer.start();
            Log.i("AUDIO_ANNOT","Lecture de "+audioPath+ " en cours");
            Toast toastStartListen;
            toastStartListen = Toast.makeText(mainActivity,"Lecture", Toast.LENGTH_LONG);
            toastStartListen.show();

            while(AudioPlayer.isPlaying());
            AudioPlayer.stop();



        }
        catch (IOException e) {
            Log.e("AUDIO_ANNOT","ECHEC DE LA LECTURE");
        }
    }
}
