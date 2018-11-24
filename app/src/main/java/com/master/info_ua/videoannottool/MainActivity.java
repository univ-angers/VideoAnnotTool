package com.master.info_ua.videoannottool;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
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
import com.master.info_ua.videoannottool.adapter.SpinnerAdapter;
import com.master.info_ua.videoannottool.adapter.VideosAdapter;
import com.master.info_ua.videoannottool.annotation.DirPath;
import com.master.info_ua.videoannottool.annotation.Video;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.master.info_ua.videoannottool.annotation_dessin.DrawView;
import com.master.info_ua.videoannottool.annotation_dialog.DialogRecord;
import com.master.info_ua.videoannottool.fragment.Fragment_annotation;
import com.master.info_ua.videoannottool.fragment.Fragment_draw;
import com.master.info_ua.videoannottool.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.master.info_ua.videoannottool.util.Util.getFile;
import static com.master.info_ua.videoannottool.util.Util.isExternalStorageWritable;
import static com.master.info_ua.videoannottool.util.Util.parseJSON;


public class MainActivity extends Activity implements Ecouteur {

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

    private boolean ExoPlayerRepeat = false;
    private FrameLayout RepeatButton;
    private ImageView RepeatIcon;
    private float ExoplayerSpeed = 1f;
    private FrameLayout SpeedButton;
    private ImageView SpeedIcon;

    private ListView listViewVideos;

    private Spinner spinnerCategorie;
    private Spinner spinnerSubCategorie;

    private VideosAdapter videosAdapter;

    private List<Video> videoList;
    private Video currentVideo;

    String videoName = "test"; // a modifié pour aller chercher le nom des video

    private Fragment_draw drawFragment;
    private Fragment_annotation annotFragment;
    private static final String FRAGMENT_DRAW_TAG = "drawFragment";
    private static final String FRAGMENT_ANNOT_TAG = "annotFragment";

    private FragmentManager fragmentManager;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
        //Autorisation enregistrement audio
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            }
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            ResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            ResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            ExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        //TEST FICHIERS
        if (isExternalStorageWritable()) {
            File file = getFile(DirPath.CATEGORIE1_SUB1, "test.txt", this);
        }


        listViewVideos = findViewById(R.id.lv_videos);

        spinnerCategorie = findViewById(R.id.spinner_cat);
        spinnerSubCategorie = findViewById(R.id.spinner_sub_cat);


        videoList = initVideoList();
        currentVideo = videoList.get(0);

        videosAdapter = new VideosAdapter(this, videoList);

        listViewVideos.setAdapter(videosAdapter);
        listViewVideos.setClickable(true);
        listViewVideos.setOnItemClickListener(videoItemClickListener);

        listViewVideos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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
        audioAnnotBtn = findViewById(R.id.audio_annot_btn);
        audioAnnotBtn.setOnClickListener(btnClickListener);

        textAnnotBtn = findViewById(R.id.text_annot_btn);
        textAnnotBtn.setOnClickListener(btnClickListener);

        graphAnnotBtn = findViewById(R.id.graphic_annot_btn);
        graphAnnotBtn.setOnClickListener(btnClickListener);

        zoomAnnotBtn = findViewById(R.id.zoom_mode_annot_btn);
        zoomAnnotBtn.setOnClickListener(btnClickListener);

        slowAnnotBtn = findViewById(R.id.slow_mode_annot_btn);
        slowAnnotBtn.setOnClickListener(btnClickListener);


        fragmentManager = getFragmentManager();
        annotFragment = (Fragment_annotation) fragmentManager.findFragmentByTag(FRAGMENT_ANNOT_TAG);

        if (annotFragment == null) {
            annotFragment = new Fragment_annotation();
            fragmentManager.beginTransaction().replace(R.id.annotation_menu, annotFragment, FRAGMENT_ANNOT_TAG).commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Affichage de la liste des annotation de la vidéo courante
        annotFragment.updateAnnotationList(currentVideo.getVideoAnnotation());
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

        initSlowButton();
        initExoPlayer();
        initRepeatButton();

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

            currentVideo = (Video) listViewVideos.getItemAtPosition(position);

            annotFragment.updateAnnotationList(currentVideo.getVideoAnnotation());

            videoName = currentVideo.getFileName();

            player.stop();

            initExoPlayer(); // recrée le lecteur
        }
    };


    public void initExoPlayer() {

        SimpleExoPlayerView exoPlayerView = findViewById(R.id.player_view);

        //1. creating an ExoPlayer with default parameters from Getting Started guide(https://google.github.io/ExoPlayer/guide.html)
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        DataSourceFactory = new DefaultExtractorsFactory();
        //2. prepare video source from url

        Uri uri = Uri.fromFile(new java.io.File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera" + File.separator + videoName + ".mp4"));
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
        setSpeed(1f);
        player.setPlayWhenReady(false);
        if (!ExoPlayerRepeat) {
            player.setRepeatMode(Player.REPEAT_MODE_OFF);
        } else {
            player.setRepeatMode(Player.REPEAT_MODE_ONE);
        }
        player.prepare(videoSource, false, false);
    }

    public void setSpeed(float speed) {
        PlaybackParameters speedParam = new PlaybackParameters(speed, speed);
        player.setPlaybackParameters(speedParam);
    }

    private void openFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        FullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        FullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        ExoPlayerFullscreen = true;
        FullScreenDialog.show();
    }

    private void initRepeatButton() {
        PlaybackControlView controlView = playerView.findViewById(R.id.exo_controller);
        RepeatIcon = controlView.findViewById(R.id.exo_repeat_icon);
        RepeatButton = controlView.findViewById(R.id.exo_repeat_button);
        RepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ExoPlayerRepeat) {
                    // active le mode repeat
                    player.setRepeatMode(Player.REPEAT_MODE_ONE);
                    RepeatIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.repeat_button_on));
                    ExoPlayerRepeat = true;
                } else {
                    // desactive le mode repeat
                    player.setRepeatMode(Player.REPEAT_MODE_OFF);
                    RepeatIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.repeat_button_off));
                    ExoPlayerRepeat = false;
                }
            }
        });
    }

    private void initSlowButton() {
        PlaybackControlView controlView = playerView.findViewById(R.id.exo_controller);
        SpeedIcon = controlView.findViewById(R.id.exo_speed_icon);
        SpeedButton = controlView.findViewById(R.id.exo_speed_button);
        SpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ExoplayerSpeed == 1f) {
                    // reduit la vitesse
                    setSpeed(0.5f);
                    SpeedIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.speed_down));
                    ExoplayerSpeed = 0.5f;
                } else {
                    // augmente la vitesse
                    setSpeed(1f);
                    SpeedIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.speed_up));
                    ExoplayerSpeed = 1f;
                }
            }
        });
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

    //Initialise la liste de vidéos pous la session ( A BUT DE TESTS )
    public List<Video> initVideoList() {

        List<Video> videoList = new ArrayList<>(); //Liste de vidéo

        VideoAnnotation videoAnnotations1 = parseJSON(this, "annot_video1.json");
        VideoAnnotation videoAnnotations2 = parseJSON(this,"annot_video2.json");
        VideoAnnotation videoAnnotations3 = parseJSON(this,"annot_video3.json");
        VideoAnnotation videoAnnotations4 = parseJSON(this, "annot_video4.json");

        //Création d'instances de vidéos
        Video video1 = new Video("video 1", null, videoAnnotations1);
        Video video2 = new Video("video 2", null, videoAnnotations2);
        Video video3 = new Video("video 3", null, videoAnnotations3);
        Video video4 = new Video("video 4", null, videoAnnotations4);

        //Ajout dans la liste
        videoList.add(video1);
        videoList.add(video2);
        videoList.add(video3);
        videoList.add(video4);

        return videoList;
    }

    // methode dans Main activity qui renvoie le moment de la position pour les anotation sous forme de long
    @Override
    public long getVideoTime(){
        return player.getCurrentPosition();
    }

    //methode pour fixer le curseur de lecture a une position donné sous forme de long
    public void setVideoTime(long positionCurseur){
        player.seekTo(positionCurseur);
    }

    @Override
    public SimpleExoPlayer getPlayer() {
        return player;
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int btnId = view.getId();
            switch (btnId) {
                case R.id.audio_annot_btn:
                    DialogRecord dialog = new DialogRecord();
                    dialog.showDialogRecord(MainActivity.this, videoName);
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
