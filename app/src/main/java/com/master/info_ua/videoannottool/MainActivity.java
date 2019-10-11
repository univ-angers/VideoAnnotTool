package com.master.info_ua.videoannottool;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.master.info_ua.videoannottool.adapter.SpinnerAdapter;
import com.master.info_ua.videoannottool.adapter.VideosAdapter;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.ControllerAnnotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.master.info_ua.videoannottool.custom.Audio;
import com.master.info_ua.videoannottool.custom.DrawView;
import com.master.info_ua.videoannottool.custom.Video;
import com.master.info_ua.videoannottool.dialog.DialogAudio;
import com.master.info_ua.videoannottool.dialog.DialogCallback;
import com.master.info_ua.videoannottool.dialog.DialogEditVideo;
import com.master.info_ua.videoannottool.dialog.DialogImport;
import com.master.info_ua.videoannottool.dialog.DialogProfil;
import com.master.info_ua.videoannottool.dialog.DialogText;
import com.master.info_ua.videoannottool.fragment.Fragment_annotation;
import com.master.info_ua.videoannottool.fragment.Fragment_draw;
import com.master.info_ua.videoannottool.player_view.ZoomableExoPlayerView;
import com.master.info_ua.videoannottool.util.AnnotationComparator;
import com.master.info_ua.videoannottool.util.Categorie;
import com.master.info_ua.videoannottool.util.DirPath;
import com.master.info_ua.videoannottool.util.Ecouteur;
import com.master.info_ua.videoannottool.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.master.info_ua.videoannottool.annotation.AnnotationType.AUDIO;
import static com.master.info_ua.videoannottool.annotation.AnnotationType.TEXT;

public class MainActivity extends Activity implements Ecouteur, DialogCallback, Fragment_draw.DrawFragmentCallback, Fragment_annotation.AnnotFragmentListener, DialogEditVideo.EditVideoDialogListener {

    private static final int READ_REQUEST_CODE = 42;

    // attribut servant pour l'option de pleine écran du lecteur a stocker des iinformation
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private ImageButton audioAnnotBtn;
    private ImageButton textAnnotBtn;
    private ImageButton graphAnnotBtn;
    private RelativeLayout btnLayout;

    // Attribut en lien avec exoplayer
    // le player et son mediaSource
    private SimpleExoPlayer player;
    private ZoomableExoPlayerView exoPlayerView;
    //private SimpleExoPlayerView playerView;
    private MediaSource videoSource;

    private int ResumeWindow;
    private long ResumePosition;

    // attribut pour les bouton associé au pleine écran
    private boolean ExoPlayerFullscreen = false;
    private FrameLayout FullScreenButton;
    private ImageView FullScreenIcon;
    private Dialog FullScreenDialog;

    // attribut pour les bouton associé a la répétition
    private boolean exoPlayerRepeat = false;
    private FrameLayout RepeatButton;
    private ImageView RepeatIcon;

    // attribut pour les bouton gérant le ralentit de la vidéo
    private float exoplayerSpeed = 1f;
    private FrameLayout SpeedButton;
    private ImageView speedIcon;

    // attribut pour gérer le bouton play
    private boolean exoplayerPlay = false;
    private FrameLayout playButton;
    private ImageView playIcon;

    private ListView listViewVideos;

    private Spinner spinnerCategorie;
    private Spinner spinnerSubCategorie;

    private VideosAdapter videosAdapter;

    private Video currentVideo;

    String videoName; // a modifié pour aller chercher le nom des video

    private Fragment_draw drawFragment;
    private Fragment_annotation annotFragment;
    private static final String FRAGMENT_DRAW_TAG = "drawFragment";
    private static final String FRAGMENT_ANNOT_TAG = "annotFragment";

    private FragmentManager fragmentManager;

    private DrawView drawView;
    private ImageView drawBimapIv;
    private TextView annotCommentTv;

    // controler pour excécuter les annotation au moment voulu
    // handler servant a récuperer les messages des threads secondaire et as les effectuer dans le main thread
    private ControllerAnnotation controlerAnnotation;
    private Handler mainHandler;


    private VideoAnnotation currentVAnnot;
    private Categorie currentCategorie;
    private Categorie currentSubCategorie;

    private ArrayAdapter<Categorie> spinnerAdapter;
    private ArrayAdapter<Categorie> spinnerAdapter2;

    public static final boolean ELEVE = false;
    public static final boolean COACH = true;
    private boolean statut_profil = ELEVE;  //flag pour savoir si utilisateur = eleve ou coach. L'app se lance en eleve

    // attribut pour le nom de la vidéo
    private TextView videoImportName;
    private File fileVideoImport;


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



        // récupération des donnée pour les transmettre via une instance lors du passage en mode pleine écran
        if (savedInstanceState != null) {
            ResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            ResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            ExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        listViewVideos = findViewById(R.id.lv_videos);
        registerForContextMenu(listViewVideos);


        spinnerCategorie = findViewById(R.id.spinner_cat);
        spinnerSubCategorie = findViewById(R.id.spinner_sub_cat);


        videosAdapter = new VideosAdapter(this, new ArrayList<Video>());

        listViewVideos.setAdapter(videosAdapter);
        listViewVideos.setClickable(true);
        listViewVideos.setOnItemClickListener(videoItemClickListener);

        listViewVideos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //Spinner catégorie
        List<Categorie> categorieList = new ArrayList<>();
        categorieList.add(new Categorie("Categorie", null, "/"));
        categorieList.addAll(Util.setCatSpinnerList(this));

        spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, categorieList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(spinnerAdapter);

        //Spinner sous-catégorie

        List<Categorie> spinnerList2 = new ArrayList<>();
        spinnerList2.add(new Categorie("Sous-categorie", null, "/"));


        spinnerAdapter2 = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, spinnerList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategorie.setAdapter(spinnerAdapter2);


        //Listener btn audio_annot_btn
        audioAnnotBtn = findViewById(R.id.audio_annot_btn);
        audioAnnotBtn.setEnabled(false);        //bouton desactivés de base
        audioAnnotBtn.setOnClickListener(btnClickListener);

        textAnnotBtn = findViewById(R.id.text_annot_btn);
        textAnnotBtn.setEnabled(false);        //bouton desactivés de base
        textAnnotBtn.setOnClickListener(btnClickListener);

        graphAnnotBtn = findViewById(R.id.graphic_annot_btn);
        graphAnnotBtn.setEnabled(false);        //bouton desactivés de base
        graphAnnotBtn.setOnClickListener(btnClickListener);

        btnLayout = findViewById(R.id.btn_layout_id);
        drawBimapIv = findViewById(R.id.draw_bitmap_iv);
        annotCommentTv = findViewById(R.id.annot_comment_tv);

        fragmentManager = getFragmentManager();
        annotFragment = (Fragment_annotation) fragmentManager.findFragmentByTag(FRAGMENT_ANNOT_TAG);

        if (annotFragment == null) {
            annotFragment = new Fragment_annotation();
            fragmentManager.beginTransaction().replace(R.id.annotation_menu, annotFragment, FRAGMENT_ANNOT_TAG).commit();
        }

        drawView = findViewById(R.id.draw_view);

        // il faut mettre la visibilité a GONE pour pouvoir cliquer sur la vidéo, la visibilitè de la vue est rétablie en lancant la saisie d'une annotation
        drawView.setVisibility(View.GONE);

        if (!Util.appDirExist(this)) {
            Util.createDir(this);
        }

        fileVideoImport = new File("");
    }

    @Override
    protected void onStart() {
        super.onStart();

        spinnerCategorie.setOnItemSelectedListener(catItemSelectedListener);
        spinnerCategorie.setSelection(1);
        spinnerSubCategorie.setOnItemSelectedListener(subCatItemSelectedListener);

        List<Video> videoList = setVideoList(DirPath.CATEGORIE1_SUB1.toString());
        if (videoList.size() > 0) {
            currentVideo = videoList.get(0);
            setCurrentVAnnot();
            videoName = currentVideo.getFileName();

            //Affichage de la liste des annotation de la vidéo courante
            annotFragment.updateAnnotationList(currentVAnnot);
        }

        mainHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                Bundle bundle = msg.getData();
                Annotation annotation = (Annotation) bundle.getSerializable("annotation");
                //Lancement de l'annotation
                onAnnotationLauched(annotation);

                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import:
                DialogImport dialogImport = new DialogImport();
                dialogImport.showDialogImport(MainActivity.this);
                return true;
            case R.id.action_profile:
                if (statut_profil == ELEVE) {
                    DialogProfil dialogProfil = new DialogProfil();
                    dialogProfil.showDialogProfil(MainActivity.this,item);


                } else if (statut_profil == COACH) {
                    btnLayout.setVisibility(View.GONE);
                    item.setTitle("Mode consultation");
                    statut_profil = ELEVE;
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, ResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, ResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, ExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_videos) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Video video = videosAdapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.edit_item:
                DialogEditVideo dialog = new DialogEditVideo(this, video);
                dialog.showDialogEdit();
                return true;
            case R.id.delete_item:
                videosAdapter.remove(video);
                videosAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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

        if (exoPlayerView == null){
            exoPlayerView = findViewById(R.id.exo_player_view);
            initFullscreenDialog();
            initFullscreenButton();
        }

        initSlowButton();
        initExoPlayer();
        initRepeatButton();
        initPlayButton();

        if (ExoPlayerFullscreen) {
            ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
            FullScreenDialog.addContentView(exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            FullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
            FullScreenDialog.show();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();

        if (exoPlayerView != null && exoPlayerView.getPlayer() != null) {
            ResumeWindow = exoPlayerView.getPlayer().getCurrentWindowIndex();
            ResumePosition = Math.max(0, exoPlayerView.getPlayer().getContentPosition());

            exoPlayerView.getPlayer().release();
        }

        if (FullScreenDialog != null)
            FullScreenDialog.dismiss();
    }

    /**
     * Listener pour le clic sur la liste de vidéos
     */
    protected AdapterView.OnItemClickListener videoItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            videosAdapter.setSelectedListItem(position);
            videosAdapter.notifyDataSetChanged();
            if(controlerAnnotation != null){
                controlerAnnotation.cancel();
            }


            currentVideo = (Video) listViewVideos.getItemAtPosition(position);
            setCurrentVAnnot();
            if (currentVAnnot == null) {
                currentVAnnot = Util.createNewVideoAnnotation();
            }


            annotFragment.updateAnnotationList(currentVAnnot);

            videoName = currentVideo.getFileName();

            if (currentVideo != null) {
                controlerAnnotation = new ControllerAnnotation( MainActivity.this, currentVideo.getVideoAnnotation(), mainHandler);
            } else {
                controlerAnnotation = new ControllerAnnotation(MainActivity.this, null, mainHandler);
            }

            if(player != null){
                player.stop();
            }

            playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_play));
            exoplayerPlay = false;
            initPlayButton();

            initExoPlayer(); // recrée le lecteur
        }
    };


    public void initExoPlayer() {

        //SimpleExoPlayerView exoPlayerView = findViewById(R.id.player_view);
        ZoomableExoPlayerView playerView = findViewById(R.id.exo_player_view);

        // 1 creating an ExoPlayer
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                com.google.android.exoplayer2.util.Util.getUserAgent(this, getResources().getString(R.string.app_name)),
                null /* listener */,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true /* allowCrossProtocolRedirects */
        );
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(this, null,
                httpDataSourceFactory);

        //2. prepare video source from url
        String filePath;

        if (currentSubCategorie != null && !currentSubCategorie.getPath().isEmpty()) {
            filePath = this.getExternalFilesDir(currentSubCategorie.getPath() + File.separator + videoName).getAbsolutePath();
        } else {
            filePath = this.getExternalFilesDir(DirPath.CATEGORIE1_SUB1.toString() + File.separator + videoName).getAbsolutePath();
        }
        //Uri uri = Uri.fromFile(new java.io.File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera" + File.separator + videoName + ".mp4"));
        Uri uri = Uri.fromFile(new java.io.File(filePath + File.separator + videoName + ".mp4"));

        DataSpec dataSpec = new DataSpec(uri);
        FileDataSource fileDataSource = new FileDataSource();
        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }
        //2. prepare video source from url
        videoSource = new ExtractorMediaSource(uri, mediaDataSourceFactory,
                new DefaultExtractorsFactory(), new Handler(), null);

        //2. create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl());
        playerView.setControllerShowTimeoutMs(0);
        playerView.setPlayer(player);
        setSpeed(1f);
        player.setPlayWhenReady(false);
        if (!exoPlayerRepeat) {
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

        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        FullScreenDialog.addContentView(exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        FullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        ExoPlayerFullscreen = true;
        FullScreenDialog.show();
    }

    private void initRepeatButton() {
        PlaybackControlView controlView = exoPlayerView.findViewById(R.id.exo_controller);
        RepeatIcon = controlView.findViewById(R.id.exo_repeat_icon);
        RepeatButton = controlView.findViewById(R.id.exo_repeat_button);
        RepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exoPlayerRepeat) {
                    // active le mode repeat
                    player.setRepeatMode(Player.REPEAT_MODE_ONE);
                    RepeatIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.repeat_button_on));
                    exoPlayerRepeat = true;
                } else {
                    // desactive le mode repeat
                    player.setRepeatMode(Player.REPEAT_MODE_OFF);
                    RepeatIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.repeat_button_off));
                    exoPlayerRepeat = false;
                }
            }
        });
    }

    private void initSlowButton() {
        PlaybackControlView controlView = exoPlayerView.findViewById(R.id.exo_controller);
        speedIcon = controlView.findViewById(R.id.exo_speed_icon);
        SpeedButton = controlView.findViewById(R.id.exo_speed_button);
        SpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exoplayerSpeed == 1f) {
                    // reduit la vitesse
                    setSpeed(0.5f);
                    speedIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.speed_down));
                    exoplayerSpeed = 0.5f;
                } else {
                    // augmente la vitesse
                    setSpeed(1f);
                    speedIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.speed_up));
                    exoplayerSpeed = 1f;
                }
            }
        });
    }

    private void initPlayButton() {
            PlaybackControlView controlView = exoPlayerView.findViewById(R.id.exo_controller);
            playIcon = controlView.findViewById(R.id.exo_play_icon);
            playButton = controlView.findViewById(R.id.exo_play_button);
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentVideo != null) {
                        if (exoplayerPlay == false) {
                            // lance la video
                            playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_pause));
                            exoplayerPlay = true;
                            //exoPlayerView.setUseController(false);

                            controlerAnnotation.setLast_pos(0);

                            player.setPlayWhenReady(true);
                            new Thread(controlerAnnotation).start();
                        } else {
                            // augmente la vitesse
                            playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_play));
                            exoplayerPlay = false;
                            //exoPlayerView.setUseController(false);
                            player.setPlayWhenReady(false);
                            controlerAnnotation.cancel();
                        }
                    }
                }
            });
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(exoPlayerView);
        ExoPlayerFullscreen = false;
        FullScreenDialog.dismiss();
        if ((player.getDuration() - player.getCurrentPosition())<=15){
            initExoPlayer();
        }
        FullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));
    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = exoPlayerView.findViewById(R.id.exo_controller);
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

    /**
     * Construit la liste de vidéo (+annotations associées) contenues dans la sous-catégorie spécifiée en paramètre
     *
     * @param subCatDir
     * @return
     */
    protected List<Video> setVideoList(String subCatDir) {

        List<Video> videoList = new ArrayList<>(); //Liste de vidéo

        /*
        if (isAppDirectory(subCatDir)) {
        } //check if valid directory for app
        */

        File subDirContent = this.getExternalFilesDir(subCatDir);

        if (subDirContent.listFiles().length > 0) {
            for (File videoFileDir : subDirContent.listFiles()) {
                Log.e("SUB_CONT_FILE", videoFileDir.getAbsolutePath());
                if (videoFileDir.isDirectory() && videoFileDir.listFiles().length > 0) {
                    Video video = new Video();
                    for (File videoFile : videoFileDir.listFiles()) {
                        if (videoFile.getName().substring(videoFile.getName().lastIndexOf(".") + 1).equals("mp4")) {
                            Log.e("VIDEO", "Video found [" + videoFile.getName() + "]");
                            video.setFileName(videoFileDir.getName()); //le fichir video porte le même nom que le répertoire qui le contient
                        }

                        if (videoFile.getName().substring(videoFile.getName().lastIndexOf(".") + 1).equals("json")) {
                            VideoAnnotation videoAnnotation = Util.parseJSON(this, subCatDir + File.separator + videoFileDir.getName(), videoFile.getName());
                            video.setVideoAnnotation(videoAnnotation);
                        }
                    }

                    if (video.getFileName() != null && !video.getFileName().isEmpty()) {
                        video.setPath(currentSubCategorie + File.separator + videoFileDir.getName());
                        videoList.add(video);
                    }
                }
            }
        } else {
            Log.e("SUB_CAT", "No content in " + subCatDir);
        }

        return videoList;
    }

    /**
     * listener de clic sur les button d'annotation
     */
    protected View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int btnId = view.getId();
            //On désactive les boutons d'annotation
            setAnnotButtonStatus(false);
            switch (btnId) {
                case R.id.audio_annot_btn:
                    player.setPlayWhenReady(false);
                    String directoryPath = currentSubCategorie.getPath() + File.separator + videoName;
                    DialogAudio dialog = new DialogAudio(MainActivity.this, directoryPath, player.getContentPosition());
                    Annotation auDdioAnnotation = new Annotation(AUDIO);
                    dialog.showDialogRecord(auDdioAnnotation, videoName);
                    break;
                case R.id.graphic_annot_btn:
                    player.setPlayWhenReady(false);
                    drawView.setVisibility(View.VISIBLE);
                    drawView.setOnTouchEnable(true);
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    drawFragment = (Fragment_draw) fragmentManager.findFragmentByTag(FRAGMENT_DRAW_TAG);
                    if (drawFragment == null) {
                        drawFragment = new Fragment_draw();
                        ft.add(R.id.annotation_menu, drawFragment, FRAGMENT_DRAW_TAG);
                        ft.hide(annotFragment);
                        ft.show(drawFragment);
                        ft.commit();
                    } else {
                        ft.hide(annotFragment);
                        ft.show(drawFragment);
                        ft.commit();
                    }

                    break;
                case R.id.text_annot_btn:
                    player.setPlayWhenReady(false);
                    Annotation textAnnotation = new Annotation("Text Annot ", TEXT);
                    DialogText dialogtext = new DialogText(MainActivity.this, 1);
                    dialogtext.showDialogBox(textAnnotation, MainActivity.this);

                    break;
            }
        }
    };


    /**
     * listener d'écoute pour la sélection d'un item catégorie
     */
    protected AdapterView.OnItemSelectedListener catItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            // Here you get the current item that is selected by its position
            currentCategorie = (Categorie) adapterView.getItemAtPosition(position);

            spinnerAdapter2.clear();
            spinnerAdapter2.addAll(Util.setSubCatSpinnerList(currentCategorie.getPath()));
            spinnerAdapter2.notifyDataSetChanged();
            spinnerSubCategorie.setSelection(1);
            Log.e("SELECT_CAT", currentCategorie.getPath());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapter) {
        }
    };

    /**
     * listener d'écoute pour la sélection d'un item sous-catégorie
     */
    protected AdapterView.OnItemSelectedListener subCatItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position > 0) {
                // Here you get the current item that is selected by its position
                currentSubCategorie = (Categorie) adapterView.getItemAtPosition(position);
                Log.e("SELECT_SUB_CAT", currentSubCategorie.getPath());

                videosAdapter.clear();
                videosAdapter.addAll(setVideoList(currentSubCategorie.getPath()));
                videosAdapter.notifyDataSetChanged();

                if(videosAdapter.getCount()> 0 ){
                    //currentVideo = videosAdapter.getItem(0);
                    currentVideo = (Video) listViewVideos.getItemAtPosition(0);
                    setCurrentVAnnot();
                    if (currentVAnnot == null) {
                        currentVAnnot = Util.createNewVideoAnnotation();
                    }

                    annotFragment.updateAnnotationList(currentVAnnot);

                    videoName = currentVideo.getFileName();

                    if (player != null){
                        player.stop();
                    }


                    initExoPlayer();

                    if (currentVideo != null) {
                        controlerAnnotation = new ControllerAnnotation( MainActivity.this, currentVideo.getVideoAnnotation(), mainHandler);
                    } else {
                        controlerAnnotation = new ControllerAnnotation(MainActivity.this, null, mainHandler);
                    }
                }else {
                    playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_play));
                    exoplayerPlay = false;
                    initPlayButton();
                    initExoPlayer();
                }
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapter) {
        }
    };


    // methode dans Main activity qui renvoie le moment de la position pour les anotation sous forme de long
    @Override
    public long getVideoCurrentPosition() {
        return player.getCurrentPosition();
    }

    //methode pour fixer le curseur de lecture a une position donné sous forme de long
    public void setVideoTime(long positionCurseur) {
        player.seekTo(positionCurseur);
    }

    @Override
    public SimpleExoPlayer getPlayer() {
        return player;
    }

    @Override
    public void resetCanvas() {
        drawView.resetCanvas();
    }

    @Override
    public void setOnTouchEnable(boolean bool) {
        drawView.setOnTouchEnable(bool);
    }

    @Override
    public String saveDrawImage() {

        String drawfileName = drawView.enregistrer_image(currentSubCategorie.getPath() + File.separator + videoName, this.videoName);

        return drawfileName;
    }


    /**
     * Sauvegarde d'une annotation graphique
     *
     * @param annotation
     */
    @Override
    public void onSaveDrawAnnotation(Annotation annotation) {

        onSaveAnnotation(annotation);
        closeDrawFragment();
    }


    /**
     * Ajoute l'annotation passée en paramètre dans la liste des annotations de la vidéo courrante
     *
     * @param annotation
     */
    @Override
    public void onSaveAnnotation(Annotation annotation) {
        // création de l'annotation
        annotation.setAnnotationStartTime(player.getCurrentPosition());

        currentVAnnot.getAnnotationList().add(annotation);
        currentVAnnot.setLastModified(Util.DATE_FORMAT.format(new Date()));

        if (currentVAnnot != null && (currentVAnnot.getAnnotationList().size() > 0) && currentSubCategorie.getPath() != null) {
            Collections.sort(currentVAnnot.getAnnotationList(), new AnnotationComparator());
            String directory = currentSubCategorie.getPath() + File.separator + videoName;
            Util.saveVideoAnnotation(MainActivity.this, currentVAnnot, directory, videoName);
            annotFragment.updateAnnotationList(currentVAnnot);

            reloadAfterAnnotUpdate();

        } else {
            Log.e("ANNOT_SAVE", "One of initialization object is null");
        }

        setAnnotButtonStatus(true);
    }


    @Override
    public void setColor(int color) {
        drawView.setColor(color);
    }

    @Override
    public void closeAnnotationFrame() {

        drawView.resetCanvas();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        annotFragment = (Fragment_annotation) fragmentManager.findFragmentByTag(FRAGMENT_ANNOT_TAG);
        if (annotFragment == null) {
            annotFragment = new Fragment_annotation();
            ft.add(R.id.annotation_menu, annotFragment, FRAGMENT_ANNOT_TAG);
            ft.hide(drawFragment);
            ft.show(annotFragment);
            ft.commit();
        } else {
            ft.hide(drawFragment);
            ft.show(annotFragment);
            ft.commit();
        }
        drawView.setVisibility(View.GONE);
    }


    public void setStatutProfil(boolean nouveauStatut) {
        statut_profil = nouveauStatut;
    }

    protected void setCurrentVAnnot() {
        currentVAnnot = Util.createNewVideoAnnotation();
        if (currentVideo.getVideoAnnotation() != null) {
            currentVAnnot = currentVideo.getVideoAnnotation();
        }
    }

    /**
     * Ferme la fragment de gestion de l'annotation graphique et affiche celui de la liste de annotations
     */
    protected void closeDrawFragment() {
        drawView.resetCanvas();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        annotFragment = (Fragment_annotation) fragmentManager.findFragmentByTag(FRAGMENT_ANNOT_TAG);
        if (annotFragment == null) {
            annotFragment = new Fragment_annotation();
            ft.add(R.id.annotation_menu, annotFragment, FRAGMENT_ANNOT_TAG);
            ft.hide(drawFragment);
            ft.show(annotFragment);
            ft.commit();
        } else {
            ft.hide(drawFragment);
            ft.show(annotFragment);
            ft.commit();
        }
        drawView.setVisibility(View.GONE);
    }

    /**
     * Implémentation du Listener de la class "FramentAnnotation"
     *
     * Gestion de la lecture/affichage de annotations au clic
     * @param annotation
     */
    @Override
    public void onAnnotItemClick(final Annotation annotation) {

        onAnnotationLauched(annotation);

    }

    /**
     *Méthode prenant en charge le lancement des annotations
     * @param annotation
     */
    protected void onAnnotationLauched(Annotation annotation) {

        if(!ExoPlayerFullscreen) {
            setAnnotButtonStatus(false);
            player.seekTo(annotation.getAnnotationStartTime());
            player.setPlayWhenReady(false);

            playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_pause));

            final String annotFileDirectory = currentSubCategorie.getPath() + "/" + currentVideo.getFileName();

            switch (annotation.getAnnotationType()) {
                case AUDIO:
                    Uri uri = Uri.parse(getApplicationContext().getExternalFilesDir(annotFileDirectory) + File.separator + annotation.getAudioFileName());
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(getApplicationContext(), uri);
                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int duration = Integer.parseInt(durationStr);

                    final Audio audio = new Audio(getApplicationContext(), getApplicationContext().getExternalFilesDir(annotFileDirectory) + File.separator + annotation.getAudioFileName());
                    audio.listen();

                    mainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            player.setPlayWhenReady(true);
                            setAnnotButtonStatus(true);
                        }
                    }, duration);  // ==> annotation.getAnnotationDuration()

                    break;

                case DRAW:
                    Bitmap bitmap = Util.getBitmapFromAppDir(getApplicationContext(), annotFileDirectory, annotation.getDrawFileName());
                    drawBimapIv.setVisibility(View.VISIBLE);
                    drawBimapIv.setImageBitmap(bitmap);

                    mainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawBimapIv.setVisibility(View.GONE);
                            drawBimapIv.setImageBitmap(null);
                            player.setPlayWhenReady(true);
                            setAnnotButtonStatus(true);
                        }
                    }, annotation.getAnnotationDuration());

                    break;

                case TEXT:
                    annotCommentTv.setVisibility(View.VISIBLE);
                    annotCommentTv.setText(annotation.getTextComment());

                    mainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            annotCommentTv.setVisibility(View.GONE);
                            annotCommentTv.setText("");
                            player.setPlayWhenReady(true);
                            setAnnotButtonStatus(true);
                        }
                    }, annotation.getAnnotationDuration());

                    break;

                default:
                    setAnnotButtonStatus(true);
                    if (drawBimapIv.getVisibility() == View.VISIBLE) {
                        drawBimapIv.setImageBitmap(null);
                        drawBimapIv.setVisibility(View.GONE);
                    }
            }
        }

    }

    @Override
    public void onDeleteAnnotation(Annotation annotation) {
        boolean isRemove = currentVAnnot.getAnnotationList().remove(annotation);
        if (isRemove){
            annotFragment.updateAnnotationList(currentVAnnot);
            String directory = currentSubCategorie.getPath() + File.separator + videoName;
            Util.saveVideoAnnotation(MainActivity.this, currentVAnnot, directory, videoName);
            annotFragment.updateAnnotationList(currentVAnnot);
            Toast.makeText(this, "Suppression de l'annotation effectué", Toast.LENGTH_SHORT).show();
            Log.e("ANNOT_SAVE", " **** Annot file saved successfully ****");

            reloadAfterAnnotUpdate();

        }else {
            Toast.makeText(this, "Échec de suppression de l'annotation", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Crée un Intent pour déclencher le selecteur de fichers
     */
    public void performFileSearch() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Sélectionner une vidéo "),READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null && data.getData() != null) {
                Uri filePath = data.getData();
                String s = Util.getRealPathFromURI(this,filePath);
                fileVideoImport = new File(Util.getRealPathFromURI(this,filePath));
                videoImportName.setText(fileVideoImport.getName());
            }
        }
    }

    @Override
    public void onClickVideoFileImport() {
        performFileSearch();
    }

    @Override
    public void updateImportVideoTextView(TextView videoImportTextView) {
        videoImportName = videoImportTextView;
    }

    @Override
    public void saveImportVideo(Categorie categorie) {
        Util.saveImportVideoFile(this,categorie,fileVideoImport);
        videosAdapter.clear();
        videosAdapter.addAll(setVideoList(currentSubCategorie.getPath()));
        videosAdapter.notifyDataSetChanged();
    }

    protected void reloadAfterAnnotUpdate(){

        controlerAnnotation.cancel();
        controlerAnnotation.setVideoAnnotation(currentVAnnot);
        controlerAnnotation = null;
        videosAdapter.clear();
        videosAdapter.addAll(setVideoList(currentSubCategorie.getPath()));
        videosAdapter.notifyDataSetChanged();

        if(videosAdapter.getCount()> 0 ){
            //currentVideo = videosAdapter.getItem(0);
            currentVideo = (Video) listViewVideos.getItemAtPosition(0);
            setCurrentVAnnot();
            if (currentVAnnot == null) {
                currentVAnnot = Util.createNewVideoAnnotation();
            }

            annotFragment.updateAnnotationList(currentVAnnot);

            videoName = currentVideo.getFileName();

            if (player != null){
                player.stop();
            }

            playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_play));
            exoplayerPlay = false;
            initPlayButton();
            initExoPlayer();

            controlerAnnotation  = new ControllerAnnotation(MainActivity.this, currentVideo.getVideoAnnotation(), mainHandler);
        }else {
            playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_play));
            exoplayerPlay = false;
            initPlayButton();
            initExoPlayer();
        }
    }

    /**
     * Active/Désactive les bouton de lancement de dialogue pour l'édition des annotations
     */
    protected void setAnnotButtonStatus(boolean status){
        audioAnnotBtn.setEnabled(status);
        graphAnnotBtn.setEnabled(status);
        textAnnotBtn.setEnabled(status);
    }

    //Edition des infos de la video via context menu
    @Override
    public void onSaveEditVideo(Video video, String title) {
        video.setFileName(title);
        videosAdapter.notifyDataSetInvalidated();
    }
}