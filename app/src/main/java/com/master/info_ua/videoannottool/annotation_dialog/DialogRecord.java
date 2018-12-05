package com.master.info_ua.videoannottool.annotation_dialog;


import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogRecord {

    private Button btnstart;
    private Button btnstop;
    private Button btnlisten;
    private Button btnValid;
    private Button btnCancel;

    private Dialog dialog;
    private MediaRecorder recorder;
    private Context mainActivity;

    private String directory;
    private String audioName;

    private DialogRecordListener recordListener;

    private Annotation audioAnnot;

    public DialogRecord(Context activity, String directory){

        this.mainActivity = activity;
        this.directory = directory;
        dialog = new Dialog(mainActivity);

        dialog.setContentView(R.layout.boite_dialog_record);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.TitleDialogRecord);

        btnstart =  dialog.findViewById(R.id.btnStartRecord);
        btnstop =  dialog.findViewById(R.id.btnStopRecord);
        btnlisten = dialog.findViewById(R.id.btnListenRecord);
        btnValid = dialog.findViewById(R.id.btnValiderRecord);
        btnCancel = dialog.findViewById(R.id.btnAnnulerRecord);

        if(activity instanceof DialogRecordListener ){
            recordListener = (DialogRecordListener) activity;
        }
    }

    /**
     * Affichage de la boîte de dialogue
     * @param videoName
     */
    public void showDialogRecord(Annotation audioAnnot, String videoName){

        final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");

        this.audioName=videoName+"_"+dateFormat.format(new Date())+".mp3";

        btnstart.setOnClickListener(btnClickListener);
        btnstop.setOnClickListener(btnClickListener);
        btnlisten.setOnClickListener(btnClickListener);
        btnValid.setOnClickListener(btnClickListener);
        btnCancel.setOnClickListener(btnClickListener);

        dialog.show();
        btnstart.setEnabled(true);
        btnstop.setEnabled(false);
        btnlisten.setEnabled(false);
        btnValid.setEnabled(false);

        this.audioAnnot = audioAnnot;
    }


    protected View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int btnId = v.getId();

            switch (btnId){
                case R.id.btnStartRecord:
                    btnstart.setEnabled(false);
                    btnstop.setEnabled(true);
                    btnlisten.setEnabled(false);
                    btnValid.setEnabled(false);
                    StartRecord(mainActivity);
                    break;

                case R.id.btnStopRecord:
                    btnstart.setEnabled(true);
                    btnstop.setEnabled(false);
                    btnlisten.setEnabled(true);
                    btnValid.setEnabled(true);
                    stopRecord(mainActivity);
                    break;

                case R.id.btnListenRecord:
                    btnstart.setEnabled(true);
                    btnstop.setEnabled(false);
                    btnlisten.setEnabled(false);
                    btnValid.setEnabled(true);
                    ListenRecord(mainActivity);
                    break;

                case R.id.btnValiderRecord:
                    recordListener.addAudioAnnot(audioAnnot);
                    dialog.cancel();
                    break;

                case R.id.btnAnnulerRecord:
                    File file = new File(mainActivity.getFilesDir(), audioName);
                    file.delete();
                    dialog.cancel();
                    break;
            }
        }
    };

    public void StartRecord (Context main){

        try {
            if (recorder == null)
                recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);


            recorder.setOutputFile(main.getExternalFilesDir(directory)+File.separator+audioName);

            this.audioAnnot.setAudioFileName(audioName);
            this.audioAnnot.setAnnotationStartTime(4000);
            this.audioAnnot.setAnnotationDuration(5000);
            this.audioAnnot.setAnnotationTitle("Test audio annot");

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

    public void stopRecord (Context main){
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

    public void ListenRecord (Context main) {
        MediaPlayer player;
        try{

            player= new MediaPlayer();
            player.setDataSource(main.getExternalFilesDir(directory)+File.separator+audioName);

            player.prepare();
            player.start();
            Toast messageplay;
            messageplay = Toast.makeText(main,"Lecture "+audioName+" en cours", Toast.LENGTH_LONG);
            messageplay.show();

            while(player.isPlaying());
            player.stop();

            Toast messagestop;
            messagestop = Toast.makeText(main,"Fin de transmission", Toast.LENGTH_LONG);
            messagestop.show();


        }
        catch (IOException e) {
            System.out.println("listenning fail");
        }
    }

    public interface DialogRecordListener{
        void addAudioAnnot(Annotation annotation);
    }

}