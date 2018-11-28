package com.master.info_ua.videoannottool.annotation_audio;


import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogRecord {
    MediaRecorder recorder;
    String audioName;

    private String videoName;
    public void showDialogRecord(final MainActivity main, String videoName){
        final Dialog dialog = new Dialog(main);

        this.videoName=videoName;

        dialog.setContentView(R.layout.boite_dialog_record);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.TitleDialogRecord);
        final Button btnstart =  dialog.findViewById(R.id.btnStartRecord);
        final Button btnstop =  dialog.findViewById(R.id.btnStopRecord);
        final Button btnlisten = dialog.findViewById(R.id.btnListenRecord);
        final Button btnValid = dialog.findViewById(R.id.btnValiderRecord);
        final Button btnCancel = dialog.findViewById(R.id.btnAnnulerRecord);
        dialog.show();
        btnstart.setEnabled(true);
        btnstop.setEnabled(false);
        btnlisten.setEnabled(false);
        btnValid.setEnabled(false);



        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnstart.setEnabled(false);
                btnstop.setEnabled(true);
                btnlisten.setEnabled(false);
                btnValid.setEnabled(false);

                StartRecord(main);
            }
        });
        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnstart.setEnabled(true);
                btnstop.setEnabled(false);
                btnlisten.setEnabled(true);
                btnValid.setEnabled(true);

                stopRecord(main);
            }
        });
        btnlisten.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                btnstart.setEnabled(true);
                btnstop.setEnabled(false);
                btnlisten.setEnabled(false);
                btnValid.setEnabled(true);

                ListenRecord(main);
            }
        });
        btnValid.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialog.cancel();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                File file = new File(main.getFilesDir(), audioName);
                file.delete();

                dialog.cancel();
            }
        });



    }
    public DialogRecord(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");

        this.audioName=videoName+"_"+dateFormat.format(new Date())+".mp3";
    }
    public void StartRecord (MainActivity main){


        try {
            if (recorder == null)
                recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);


            recorder.setOutputFile(main.getExternalFilesDir(Environment.DIRECTORY_MUSIC)+File.separator+audioName);

            recorder.prepare();
            recorder.start();

            Toast messagedebut;
            messagedebut = Toast.makeText( main,"Début d'enregistrement", Toast.LENGTH_LONG);
            messagedebut.show();


        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("record fail");
            recorder=null;
        }

    }
    public void stopRecord (MainActivity main){
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast messagefin;
            messagefin = Toast.makeText(main, "Fichier " + this.audioName + " enregistré", Toast.LENGTH_LONG);
            messagefin.show();

        } catch (Exception e) {
            System.out.println("stop fail");
        }
    }
    public void ListenRecord (MainActivity main) {
        MediaPlayer player;
        try{

            player= new MediaPlayer();
            player.setDataSource(main.getExternalFilesDir(Environment.DIRECTORY_MUSIC)+File.separator+audioName);

            player.prepare();
            player.start();
            Toast messageplay;
            messageplay = Toast.makeText(main,"Lecture "+audioName+" en cours", Toast.LENGTH_LONG);
            messageplay.show();

            while(player.isPlaying());
            player.stop();
            player=null;

            Toast messagestop;
            messagestop = Toast.makeText(main,"Fin de transmission", Toast.LENGTH_LONG);
            messagestop.show();


        }
        catch (IOException e) {
            System.out.println("listenning fail");
            player=null;
        }
    }

}
