package com.master.info_ua.videoannottool.dialog;


import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.custom.Audio;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe permettant l'affichage de la boite de dialogue dédiée à l'enregistrement audio
 */
public class DialogAudio {


    //Définition des différents Boutons de la boite de Dialogue
    private Button btnStart;
    private Button btnStop;
    private Button btnListen;
    private Button btnValid;
    private Button btnCancel;


    private Dialog dialogBox;

    private MediaRecorder recorder;

    private Context mainActivity;
    private String directory;
    private String audioName;
    private Annotation audioAnnot;
    private long startTime;

    private DialogCallback recordCallback;

    /**
     * Constructeur de la classe
     *
     * @param context
     * @param directory repertoire de la vidéo
     * @param startTime temps de début de l'annotation
     */
    public DialogAudio(Context context, String directory, long startTime) {

        //Initialisation
        this.startTime = startTime;
        this.mainActivity = context;
        this.directory = directory;
        dialogBox = new Dialog(mainActivity);

        dialogBox.setContentView(R.layout.boite_dialog_record);
        dialogBox.setCancelable(false);
        dialogBox.setTitle(R.string.TitleDialogRecord);

        btnStart = dialogBox.findViewById(R.id.btnStartRecord);
        btnStop = dialogBox.findViewById(R.id.btnStopRecord);
        btnListen = dialogBox.findViewById(R.id.btnListenRecord);
        btnValid = dialogBox.findViewById(R.id.btnValiderRecord);
        btnCancel = dialogBox.findViewById(R.id.btnAnnulerRecord);


        if (context instanceof DialogCallback) {
            recordCallback = (DialogCallback) context;
        }
    }

    /**
     * Affiche la boite de dialogue permettant l'enregistrement audio
     *
     * @param audioAnnot annotation traitée
     * @param videoName  nom de la vidéo
     */
    public void showDialogRecord(Annotation audioAnnot, String videoName) {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");

        this.audioName = videoName + "_" + dateFormat.format(new Date()) + ".mp3";

        btnStart.setOnClickListener(btnClickListener);
        btnStop.setOnClickListener(btnClickListener);
        btnListen.setOnClickListener(btnClickListener);
        btnValid.setOnClickListener(btnClickListener);
        btnCancel.setOnClickListener(btnClickListener);

        dialogBox.show();
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnListen.setEnabled(false);
        btnValid.setEnabled(false);

        this.audioAnnot = audioAnnot;
    }

    protected View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int btnId = v.getId();

            switch (btnId) {
                case R.id.btnStartRecord:
                    btnStart.setEnabled(false);
                    btnStop.setEnabled(true);
                    btnListen.setEnabled(false);
                    btnValid.setEnabled(false);
                    startRecording();
                    break;

                case R.id.btnStopRecord:
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                    btnListen.setEnabled(true);
                    btnValid.setEnabled(true);
                    stopRecording();
                    break;

                case R.id.btnListenRecord:
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                    btnListen.setEnabled(false);
                    btnValid.setEnabled(true);
                    Audio audio = new Audio(mainActivity, mainActivity.getExternalFilesDir(directory) + File.separator + audioName);
                    audio.listen();
                    break;

                case R.id.btnValiderRecord:
                    recordCallback.addAudioAnnot(audioAnnot);
                    Toast toastConfirmAnnot;
                    toastConfirmAnnot = Toast.makeText(mainActivity, "Annotation Enregistrée", Toast.LENGTH_LONG);
                    toastConfirmAnnot.show();
                    Log.i("AUDIO_DIALOG-BOX", "Validation " + audioName);
                    dialogBox.cancel();
                    break;

                case R.id.btnAnnulerRecord:
                    File file = new File(mainActivity.getFilesDir(), audioName);
                    file.delete();
                    Log.i("AUDIO_DIALOG-BOX", "Annulation");
                    dialogBox.cancel();
                    break;
            }
        }
    };

    /**
     * Débute l'enregistrement audio
     */
    public void startRecording() {

        try {
            if (recorder == null)
                recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);


            recorder.setOutputFile(mainActivity.getExternalFilesDir(directory) + File.separator + audioName);

            this.audioAnnot.setAudioFileName(audioName);
            this.audioAnnot.setAnnotationStartTime(startTime);

            this.audioAnnot.setAnnotationTitle("Test audio annot");

            recorder.prepare();
            recorder.start();
            Log.i("AUDIO_DIALOG-BOX", "Enregistrement commencé");
            Toast.makeText(mainActivity, "Début d'enregistrement", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AUDIO_DIALOG-BOX", "ECHEC DE L'ENREGISTREMENT");
            recorder = null;
        }

    }

    /**
     * Arrête l'enregistrement audio
     */
    public void stopRecording() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;

            MediaPlayer tmpPlayer = new MediaPlayer();
            tmpPlayer.setDataSource(mainActivity.getExternalFilesDir(directory) + File.separator + audioName);

            audioAnnot.setAnnotationDuration(tmpPlayer.getDuration());
            Log.e("AUDIO_DIALOG-BOX", "Enregistrement terminé ==> [durée de l'audio: " + tmpPlayer.getDuration());
            Toast.makeText(mainActivity, "Fichier " + this.audioName + " enregistré", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("AUDIO_DIALOG-BOX", "ECHEC DE L'ARRET");
        }
    }

}