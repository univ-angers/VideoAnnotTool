package com.example.etudiant.videoannottool;


import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class dialogRecord {
    MediaRecorder recorder;
    String audioName="";
    public void showDialogRecord(final MainActivity main){
        Dialog dialog = new Dialog(main);

        dialog.setContentView(R.layout.boite_dialog_record);

        Button btnstart =  dialog.findViewById(R.id.btnStartRecord);
        Button btnstop =  dialog.findViewById(R.id.btnStopRecord);

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartRecord(main);
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord(main);
            }
        });

        dialog.show();
    }
    public void StartRecord (MainActivity main){


        try {
            if (recorder == null)
                recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            this.audioName="audio.mp3";


            recorder.setOutputFile(main.getFilesDir()+File.separator + audioName);

            recorder.prepare();
            recorder.start();

            Toast messagedebut;
            messagedebut = Toast.makeText( main,"Début d'enregistrement", Toast.LENGTH_LONG);
            messagedebut.show();
            System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + audioName);
            //ExportAnnotationRecord(5,player2);

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

}
