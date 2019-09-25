package com.master.info_ua.videoannottool.custom;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Classe gérant la lecture audio
 */
public class Audio {
    //Chemin du fichier audio
    private String audioPath;
    private Context mainActivity;

    //Constructeur
    public Audio(Context context,String path){
       this.mainActivity=context;
       this.audioPath=path;
    }

    //Lis le fichier audio
    public void listen() {
        //Instanciation
        MediaPlayer AudioPlayer = new MediaPlayer();;
        try{
            AudioPlayer.setDataSource(audioPath);
            AudioPlayer.prepare();
            AudioPlayer.start();
            Log.i("AUDIO_ANNOT","Lecture de "+audioPath+ " en cours");
            Toast.makeText(mainActivity,"Lecture", Toast.LENGTH_LONG).show();
            //Tant que l'audio est en cours de lecture, on ne fait rien
            while(AudioPlayer.isPlaying());
            AudioPlayer.stop();
        }
        catch (IOException e) {
            Log.e("AUDIO_ANNOT","ECHEC DE LA LECTURE");
        }
    }
}
