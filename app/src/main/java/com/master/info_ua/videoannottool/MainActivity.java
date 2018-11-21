package com.master.info_ua.videoannottool;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.support.v4.content.ContextCompat;

import com.master.info_ua.videoannottool.adapter.AnnotationsAdapter;
import com.master.info_ua.videoannottool.adapter.SpinnerAdapter;
import com.master.info_ua.videoannottool.adapter.VideosAdapter;
import com.master.info_ua.videoannottool.annotation_dessin.DrawView;
import com.master.info_ua.videoannottool.annotation_dialog.DialogRecord;
import com.master.info_ua.videoannottool.annotation.AnnotationType;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.Video;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ImageButton audioAnnotBtn;
    private ImageButton textAnnotBtn;
    private ImageButton graphAnnotBtn;
    private ImageButton zoomAnnotBtn;
    private ImageButton slowAnnotBtn;

    private SimpleExoPlayer player;
    private SimpleExoPlayerView playerView;
    private MediaSource videoSource;
    private DefaultExtractorsFactory DataSourceFactory;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private int ResumeWindow;
    private long ResumePosition;

    private boolean ExoPlayerFullscreen = false;
    private FrameLayout FullScreenButton;
    private ImageView FullScreenIcon;
    private Dialog FullScreenDialog;

    private ListView listViewVideos;
    private ListView listViewAnnotations;

    private Spinner spinnerCategorie;
    private Spinner spinnerSubCategorie;

    private VideosAdapter videosAdapter;
    private AnnotationsAdapter annotationsAdapter;

    private List<Video> videoList;

    private MediaRecorder recorder;
    private String audioName="";
    String videoName = "test"; // a modifié pour aller chercher le nom des video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
        //Autorisation enregistrement audio
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},1);
            }
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            ResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            ResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            ExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        listViewVideos = (ListView) findViewById(R.id.lv_videos);
        listViewAnnotations = (ListView) findViewById(R.id.lv_annotations);

        spinnerCategorie = (Spinner) findViewById(R.id.spinner_cat);
        spinnerSubCategorie = (Spinner) findViewById(R.id.spinner_sub_cat);

        videoList = initVideoList();
        videosAdapter = new VideosAdapter(this, videoList);
        annotationsAdapter = new AnnotationsAdapter(this, new ArrayList<Annotation>()); //Initilisatisation de la liste d'annotations (vide)

        listViewVideos.setAdapter(videosAdapter);
        listViewVideos.setClickable(true);
        listViewVideos.setOnItemClickListener(videoItemClickListener);

        listViewVideos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
       // listViewVideos.setSelection(selectedListItem);

        listViewAnnotations.setAdapter(annotationsAdapter);
        listViewAnnotations.setClickable(true);
        listViewAnnotations.setOnItemClickListener(annotationItemClickListener);


        //Spinner catégorie
        ArrayList<String> categorieList = new ArrayList<>();
        categorieList.add("Categorie");
        categorieList.add("item1");
        categorieList.add("item2");

        ArrayAdapter<String> spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, categorieList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(spinnerAdapter);

        //Spinner sous-catégorie
        ArrayList<String> spinnerList2 = new ArrayList<>();
        spinnerList2.add("Sous-categorie");
        spinnerList2.add("item1");
        spinnerList2.add("item2");

        ArrayAdapter<String> spinnerAdapter2 = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, spinnerList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategorie.setAdapter(spinnerAdapter2);



        //Listener btn audio_annot_btn
        audioAnnotBtn= findViewById(R.id.audio_annot_btn);
        audioAnnotBtn.setOnClickListener(btnClickListener);

        textAnnotBtn= findViewById(R.id.text_annot_btn);
        textAnnotBtn.setOnClickListener(btnClickListener);

        graphAnnotBtn= findViewById(R.id.graphic_annot_btn);
        graphAnnotBtn.setOnClickListener(btnClickListener);

        zoomAnnotBtn= findViewById(R.id.zoom_mode_annot_btn);
        zoomAnnotBtn.setOnClickListener(btnClickListener);

        slowAnnotBtn= findViewById(R.id.slow_mode_annot_btn);
        slowAnnotBtn.setOnClickListener(btnClickListener);




    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, ResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, ResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, ExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }


    private void initFullscreenDialog() {

        FullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (ExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (playerView == null) {

            playerView = findViewById(R.id.player_view);
            initFullscreenDialog();
            initFullscreenButton();

        }

        initExoPlayer();

        if (ExoPlayerFullscreen) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
            FullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            FullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
            FullScreenDialog.show();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();

        if (playerView != null && playerView.getPlayer() != null) {
            ResumeWindow = playerView.getPlayer().getCurrentWindowIndex();
            ResumePosition = Math.max(0, playerView.getPlayer().getContentPosition());

            playerView.getPlayer().release();
        }

        if (FullScreenDialog != null)
            FullScreenDialog.dismiss();
    }

    //Listener pour le clic sur la liste de vidéos
    protected AdapterView.OnItemClickListener videoItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            videosAdapter.setSelectedListItem(position);
            videosAdapter.notifyDataSetChanged();

            Video video = (Video) listViewVideos.getItemAtPosition(position);

            //Mise à jour de la liste
            annotationsAdapter.clear();
            annotationsAdapter.addAll(video.getVideoAnnotation().getAnnotationList());
            annotationsAdapter.notifyDataSetChanged();

            videoName = video.getFileName();

            player.stop();

            initExoPlayer(); // recrée le lecteur
        }
    };

    //Listener pour le clic sur la liste d'annotations

    protected AdapterView.OnItemClickListener annotationItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Annotation annotation = (Annotation) listViewAnnotations.getItemAtPosition(position);


            //Exemple affichage d'informations de l'annotation
            Toast.makeText(getApplicationContext(),"Annotation: "+ annotation.getAnnotationTitle() + " Type: " + annotation.getAnnotationType(),Toast.LENGTH_SHORT).show();



        }
    };

    public void initExoPlayer() {
        String path = "android.resource://" + getPackageName() + "/" + R.raw.test;

        SimpleExoPlayerView exoPlayerView = findViewById(R.id.player_view);

        //1. creating an ExoPlayer with default parameters from Getting Started guide(https://google.github.io/ExoPlayer/guide.html)
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        DataSourceFactory = new DefaultExtractorsFactory();
        //2. prepare video source from url

        Uri uri = Uri.fromFile(new java.io.File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera" + File.separator + videoName + ".mp4"));
        //Uri uri = Uri.fromFile(new java.io.File("/sdcard/DCIM/Camera/" + videoName + ".mp4"));
        DataSpec dataSpec = new DataSpec(uri);
        FileDataSource fileDataSource = new FileDataSource();
        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }
        videoSource = new ExtractorMediaSource(
                uri,
                new DefaultDataSourceFactory(this, "ua"),
                new DefaultExtractorsFactory(), null, null);

        //2. create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl());
        exoPlayerView.setControllerShowTimeoutMs(0);
        exoPlayerView.setPlayer(player);
        player.setPlayWhenReady(false);
        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        player.prepare(videoSource, false, false);
    }


    private void openFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        FullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        FullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        ExoPlayerFullscreen = true;
        FullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(playerView);
        ExoPlayerFullscreen = false;
        FullScreenDialog.dismiss();
        FullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));
    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = playerView.findViewById(R.id.exo_controller);
        FullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        FullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        FullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }

    //Initialise la liste de vidéos pous la session ( A BUT DE TESTES )
    public List<Video> initVideoList() {

        List<Video> videoList = new ArrayList<>(); //Liste de vidéo

        final VideoAnnotation videoAnnotations1;
        final VideoAnnotation videoAnnotations2;
        final VideoAnnotation videoAnnotations3;

        //Création des annotations
        Annotation annotation1 = new Annotation("annotation1", null, null, null, AnnotationType.TEXT);
        Annotation annotation2 = new Annotation("annotation2", null, null, null, AnnotationType.TEXT);
        Annotation annotation3 = new Annotation("annotation3", null, null, null, AnnotationType.TEXT);

        //Constructionn de listes d'annotation (3 dans ce cas)
        List<Annotation> arrayOfAnnotations1 = new ArrayList<>();
        arrayOfAnnotations1.add(annotation1);
        arrayOfAnnotations1.add(annotation2);
        arrayOfAnnotations1.add(annotation3);

        List<Annotation> arrayOfAnnotations2 = new ArrayList<>();
        arrayOfAnnotations2.add(annotation2);
        arrayOfAnnotations2.add(annotation1);
        arrayOfAnnotations2.add(annotation3);

        List<Annotation> arrayOfAnnotations3 = new ArrayList<>();
        arrayOfAnnotations3.add(annotation3);
        arrayOfAnnotations3.add(annotation2);
        arrayOfAnnotations3.add(annotation1);

        //Instanciation des objets d'annotations
        videoAnnotations1 = new VideoAnnotation(null, null, arrayOfAnnotations1);
        videoAnnotations2 = new VideoAnnotation(null, null, arrayOfAnnotations2);
        videoAnnotations3 = new VideoAnnotation(null, null, arrayOfAnnotations3);

        //Création d'instances de vidéos
        Video video1 = new Video("test", null, videoAnnotations1);
        Video video2 = new Video("test2", null, videoAnnotations2);
        Video video3 = new Video("nom3", null, videoAnnotations3);

        //Ajout dans la liste
        videoList.add(video1);
        videoList.add(video2);
        videoList.add(video3);

        return videoList;
    }

    View.OnClickListener btnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int btnId=view.getId();
            switch (btnId){
                case R.id.audio_annot_btn:
                    DialogRecord dialog= new DialogRecord();
                    dialog.showDialogRecord(MainActivity.this,videoName);
                    break;
                case R.id.graphic_annot_btn:
                    DrawView drawView = (DrawView)findViewById(R.id.draw_view);
                    drawView.setOnTouchEnable(true);

                    // faire apparaitre le fragment

                    break;
                case R.id.slow_mode_annot_btn:
                    break;
                case R.id.text_annot_btn:
                    break;
                case R.id.zoom_mode_annot_btn:
                    break;

            }


        }
    };
}
