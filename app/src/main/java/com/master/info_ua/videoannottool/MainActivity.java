package com.master.info_ua.videoannottool;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.master.info_ua.videoannottool.dialog.DialogAnnotModification;
import com.master.info_ua.videoannottool.dialog.DialogAnnotPredefModification;
import com.master.info_ua.videoannottool.dialog.DialogAudio;
import com.master.info_ua.videoannottool.dialog.DialogCallback;
import com.master.info_ua.videoannottool.dialog.DialogEditAnnot;
import com.master.info_ua.videoannottool.dialog.DialogEditDifficulte;
import com.master.info_ua.videoannottool.dialog.DialogEditVideo;
import com.master.info_ua.videoannottool.dialog.DialogImport;
import com.master.info_ua.videoannottool.dialog.DialogProfil;
import com.master.info_ua.videoannottool.dialog.DialogRenameAnnotPredef;
import com.master.info_ua.videoannottool.dialog.DialogText;
import com.master.info_ua.videoannottool.dialog.DialogValidationEnregistrementVideo;
import com.master.info_ua.videoannottool.dialog.DialogVignette;
import com.master.info_ua.videoannottool.fragment.Fragment_AnnotPredef;
import com.master.info_ua.videoannottool.fragment.Fragment_annotation;
import com.master.info_ua.videoannottool.fragment.Fragment_draw;
import com.master.info_ua.videoannottool.player_view.ZoomableExoPlayerView;
import com.master.info_ua.videoannottool.util.AnnotationComparator;
import com.master.info_ua.videoannottool.util.Categorie;
import com.master.info_ua.videoannottool.util.DirPath;
import com.master.info_ua.videoannottool.util.Ecouteur;
import com.master.info_ua.videoannottool.util.Util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.master.info_ua.videoannottool.annotation.AnnotationType.AUDIO;
import static com.master.info_ua.videoannottool.annotation.AnnotationType.DRAW;
import static com.master.info_ua.videoannottool.annotation.AnnotationType.TEXT;
import static java.lang.Math.round;


public class MainActivity extends Activity implements Ecouteur, DialogCallback, Fragment_draw.DrawFragmentCallback, Fragment_annotation.AnnotFragmentListener, Fragment_AnnotPredef.AnnotFragmentListener, DialogEditVideo.EditVideoDialogListener , DialogEditAnnot.EditAnnotDialogListener, DialogVignette.EditVignetteDialogListener, DialogEditDifficulte.EditDifficulteDialogListener{

    private static final int READ_REQUEST_CODE = 42;
    static final int READ_CATEGORY_CODE = 1;


    // attribut servant pour l'option de pleine écran du lecteur a stocker des iinformation
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    //Attributs en liens avec les boutons
    private ImageButton audioAnnotBtn;
    private ImageButton textAnnotBtn;
    private ImageButton graphAnnotBtn;
    private Button vignetteBtn;
    private Button exportVideoBtn;
    private Button annotPredefBtn;
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
    private Spinner spinnerDifficulte;

    private List<Video> listvideo;
    private VideosAdapter videosAdapter;

    private Video currentVideo;

    String videoName; // a modifié pour aller chercher le nom des video

    private Fragment_draw drawFragment;
    private Fragment_annotation annotFragment;
    private Fragment_AnnotPredef annotPredefFragment;
    private static final String FRAGMENT_DRAW_TAG = "drawFragment";
    private static final String FRAGMENT_ANNOT_TAG = "annotFragment";
    private static final String FRAGMENT_ANNOT_PREDEF_TAG = "annotPredefFragment";

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

    private List<Categorie> categorieList;

    private ArrayAdapter<Categorie> spinnerAdapter;
    private ArrayAdapter<Categorie> spinnerAdapter2;

    public static final boolean ELEVE = false;
    public static final boolean COACH = true;
    private boolean statut_profil = ELEVE;  //flag pour savoir si utilisateur = eleve ou coach. L'app se lance en eleve

    // attribut pour le nom de la vidéo
    private TextView videoImportName;
    private File fileVideoImport;

    //Attributs pour la recherche de vidéos
    private EditText searchVideo;
    private String searchText;

    // Listes de toutes les annotations prédéfinies
    private ArrayList<Annotation> listAnnotationsPredef = new ArrayList<>();

    //Dossier contenant les fichiers nécéssaires aux annotations prédéfinies (.png, .mp4, ...)
    private File annotPredefDirectory;


    //variables utilisees pour l'enregistrement de l'ecran
    private static final int CAST_PERMISSION_CODE = 22;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mProjectionManager;


    private static final int REQUEST_CODE_CAPTURE_PERM = 1234;
    private static final String VIDEO_MIME_TYPE = "video/avc";
    private DisplayMetrics metrics;
    private int screenDensity;
    private int screenWidth;
    private int screenHeight;

    private boolean mMuxerStarted = false;
    private Surface mInputSurface;
    private MediaCodec mVideoEncoder;
    private MediaCodec.BufferInfo mVideoBufferInfo;
    private int mTrackIndex = -1;

    public String nomVideoAEnregistrer = "";
    private boolean recording = false;
    private MediaMuxer mMuxer;

    //Fichier JSON pour la relation entre difficulté et vidéo
    private File fileVideoDifficulteDirectory;
    public File fileVideoDifficulte;
    private int currentDifficulte = 1;
    //END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        categorieList = new ArrayList<>();
        categorieList.add(new Categorie("Catégorie", null, "/"));

        categorieList.addAll(Util.initCatList(this));

        spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, categorieList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(spinnerAdapter);

        //Spinner sous-catégorie

        List<Categorie> spinnerList2 = new ArrayList<>();
        spinnerList2.add(new Categorie("Sous-categorie", null, "/"));

        vignetteBtn = findViewById(R.id.vignette_btn);
        vignetteBtn.setEnabled(false);
        vignetteBtn.setOnClickListener(btnClickListener);

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

        annotPredefBtn = findViewById(R.id.annot_predef_btn);
        annotPredefBtn.setOnClickListener(btnClickListener);

        exportVideoBtn = findViewById(R.id.export_video_btn);
        exportVideoBtn.setOnClickListener(btnClickListener);

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


        searchText = new String();
        searchVideo = (EditText)findViewById(R.id.editText_search_video);

        searchVideo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchText = charSequence.toString();
                videosAdapter.clear();
                if ((i == 0) && (i2 == 0)) { // Si le texte de recherche est vide, on ne charge dans la liste de vidéo que les vidéos de la catégorie courante
                    videosAdapter.addAll(setVideoList(currentSubCategorie.getPath()));
                } else { // Sinon, on parcourt la liste des catégories et on charge les vidéos de chaque catégorie, et dont le nom correspond au texte de recherche
                    for (Categorie cat : categorieList) {
                        for (Categorie subCat : cat.getSubCategories()) {
                            videosAdapter.addAll(setVideoList(subCat.getPath()));
                        }
                    }
                }
                videosAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fileVideoImport = new File("");

        searchText = new String();
        searchVideo = (EditText)findViewById(R.id.editText_search_video);

        // Créer le dossier "annotations" qui servira à stocker les annotations prédéfinies
        annotPredefDirectory = new File(MainActivity.this.getExternalFilesDir(""),"annotations");
        annotPredefDirectory.mkdirs();

        // Récupère les annotations prédéfines stocker en JSON pour les inserer dans la Liste
        listAnnotationsPredef.addAll(Util.parseJSON_Annot(MainActivity.this));

//        spinnerDifficulte=findViewById(R.id.spinner_difficulte);
//        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.difficultes, android.R.layout.simple_spinner_item);
//        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinnerDifficulte.setAdapter(adapterSpinner);
//        spinnerDifficulte.setOnItemSelectedListener(difficulteSelectedListener);

        //fileVideoDifficulte = new File(MainActivity.this.getExternalFilesDir("difficulte"), "videodifficulte.json");
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
        //Si on est en profil élève, on propose de passer en mode annotation et on interdit la gestion des catégories
        if(statut_profil==ELEVE) {
            menu.findItem(R.id.action_category).setVisible(false);
            menu.findItem(R.id.action_profile).setTitle("Mode annotation");
        }
        //Si on est en profil coach, on propose de passer en mode consultation et on autorise la gestion des catégories
        else if(statut_profil==COACH) {
            menu.findItem(R.id.action_category).setVisible(true);
            menu.findItem(R.id.action_profile).setTitle("Mode consultation");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import:
                DialogImport dialogImport = new DialogImport(categorieList);
                dialogImport.showDialogImport(MainActivity.this);
                return true;
            case R.id.action_profile:
                if (statut_profil == ELEVE) {
                    DialogProfil dialogProfil = new DialogProfil();
                    dialogProfil.showDialogProfil(MainActivity.this,item,annotFragment);
                } else if (statut_profil == COACH) {
                    btnLayout.setVisibility(View.GONE);
                    statut_profil = ELEVE;
                    annotFragment.setStatut_profil(ELEVE);
                    invalidateOptionsMenu();
                }
                return true;

            case R.id.action_category:
                Intent childintent = new Intent(MainActivity.this,CategoryActivity.class);
                for( Categorie cat: categorieList)
                {
                    System.out.println(cat + "     " + cat.getSubCategories().size());
                }

                childintent.putParcelableArrayListExtra("categorieList", (ArrayList<? extends Parcelable>) categorieList);
                startActivityForResult(childintent,READ_CATEGORY_CODE);
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
        if (v.getId() == R.id.lv_videos && statut_profil==COACH) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            menu.findItem(R.id.edit_item_video).setVisible(true);
            menu.findItem(R.id.delete_item_video).setVisible(true);
            menu.findItem(R.id.edit_item_annot).setVisible(false);
            menu.findItem(R.id.delete_item_annot).setVisible(false);
            menu.findItem(R.id.edit_item_infos_annot).setVisible(false);
            menu.findItem(R.id.renommer_annot_predef).setVisible(false);
            menu.findItem(R.id.modifier_annot_predef).setVisible(false);
            menu.findItem(R.id.supprimer_annot_predef).setVisible(false);
            //menu.findItem(R.id.edit_difficulte).setVisible(true);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Video video;
        Annotation annotation;

        switch (item.getItemId()) {
            case R.id.edit_item_video:
                video = videosAdapter.getItem(info.position);
                DialogEditVideo dialog = new DialogEditVideo(this, video);
                dialog.showDialogEdit();
                return true;
            case R.id.delete_item_video:
                video = videosAdapter.getItem(info.position);
                String subCatDir= video.getPath();
                File subDirContent = this.getExternalFilesDir(subCatDir);
                System.out.println(subDirContent.isDirectory()+"     "+ subCatDir + "     "+ subDirContent.getName() +"     "+ video.getPath());
                if (subDirContent.isDirectory())
                {
                    Util.deleteRecursiveDirectory(subDirContent);
                }
                Util.saveRemoveVideo(this, video.getName());
                videosAdapter.remove(video);
                videosAdapter.notifyDataSetChanged();
                initExoPlayer();
                Util.deleteRecursiveDirectory(subDirContent);
                return true;
            case R.id.edit_item_infos_annot:
                annotation = annotFragment.getAnnotationsAdapter().getItem(info.position);
                DialogEditAnnot dialogEditAnnot = new DialogEditAnnot(annotFragment, annotation);
                dialogEditAnnot.showDialogEdit();
                return true;
            case R.id.edit_item_annot:
                System.out.println("Annotation modifiée!!!");
                annotation = annotFragment.getAnnotationsAdapter().getItem(info.position);
                annotFragment.getFragmentListener().onEditAnnotation(annotation, info.position); //Inutile de repasser par le fragment pour revenir dans l'activité??
                return true;
            case R.id.delete_item_annot:
                annotation = annotFragment.getAnnotationsAdapter().getItem(info.position);
                annotFragment.getFragmentListener().onDeleteAnnotation(annotation);
                annotFragment.getAnnotationsAdapter().notifyDataSetChanged();
                return true;
            case R.id.renommer_annot_predef:
                Log.i("Menu", "Renommer");
                annotation = annotPredefFragment.getAnnotationsAdapter().getItem(info.position);
                annotPredefFragment.getFragmentListener().onRenommerAnnotationPredef(annotation, info.position);
                Log.i("Menu", ""+info.position);
                return true;
            case R.id.modifier_annot_predef:
                Log.i("Menu", "modifier");
                annotation = annotPredefFragment.getAnnotationsAdapter().getItem(info.position);
                annotPredefFragment.getFragmentListener().onUpdateAnnotationPredef(annotation, info.position);
                Log.i("Menu", ""+info.position);
                return true;
            case R.id.supprimer_annot_predef:
                Log.i("Menu", "supprimer");
                annotation = annotPredefFragment.getAnnotationsAdapter().getItem(info.position);
                annotPredefFragment.getFragmentListener().onDeleteAnnotationPredef(annotation, info.position);
                return true;
//            case R.id.edit_difficulte:
//                video = videosAdapter.getItem(info.position);
//                DialogEditDifficulte dialogDiff = new DialogEditDifficulte(this, video);
//                dialogDiff.showDialogEditDiff();
//                return true;
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

            System.out.println(currentVideo.getFileName() +"              "+ currentVideo.getName());
//            videoName = currentVideo.getFileName();
            videoName = currentVideo.getName();

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

    /**
     * Listener pour le filtrage par niveau de difficulté
     */
    public AdapterView.OnItemSelectedListener difficulteSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch(adapterView.getItemAtPosition(i).toString()) {
                case "Niv.1" :
                    currentDifficulte = 1;
                    Log.i("LISTENERDIFFICULTE", "currentDifficulte = " + currentDifficulte);
                    setVideoList(currentSubCategorie.getPath());
                    break;
                case "Niv.2" :
                    currentDifficulte = 2;
                    Log.i("LISTENERDIFFICULTE", "currentDifficulte = " + currentDifficulte);
                    setVideoList(currentSubCategorie.getPath());
                    break;
                case "Niv.3" :
                    currentDifficulte = 3;
                    Log.i("LISTENERDIFFICULTE", "currentDifficulte = " + currentDifficulte);
                    setVideoList(currentSubCategorie.getPath());
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void initExoPlayer() {

        //SimpleExoPlayerView exoPlayerView = findViewById(R.id.player_view);


        ZoomableExoPlayerView playerView = findViewById(R.id.exo_player_view);

        // Lors de l'exportation de la video on récupère les vues pour les inserer dans le playerView
        if (recording){
            ((ViewGroup) annotCommentTv.getParent()).removeView(annotCommentTv);
            ((ViewGroup) drawBimapIv.getParent()).removeView(drawBimapIv);
            exoPlayerView.addView(drawBimapIv);
            exoPlayerView.addView(annotCommentTv);
            exoPlayerView.setPlayer(player);
            playerView = exoPlayerView;
        }

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
        Uri uri = Uri.fromFile(new File(filePath + File.separator + videoName + ".mp4"));

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
        //replacer les elements d'interface dans le fullScreenDialog
        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        FullScreenDialog.addContentView(exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) drawBimapIv.getParent()).removeView(drawBimapIv);
        FullScreenDialog.addContentView(drawBimapIv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) annotCommentTv.getParent()).removeView(annotCommentTv);
        FrameLayout.LayoutParams VG_LParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        VG_LParam.gravity = Gravity.CENTER;
        FullScreenDialog.addContentView(annotCommentTv, VG_LParam);
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

                        //Cache le player de la video lors de l'export
                        if (recording) {
                            exoPlayerView.setUseController(false);
                        }
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

        //on replace les elements dans l'interface de l'application
        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(exoPlayerView);

        ((ViewGroup) drawBimapIv.getParent()).removeView(drawBimapIv);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(drawBimapIv);

        ((ViewGroup) annotCommentTv.getParent()).removeView(annotCommentTv);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(annotCommentTv);

        ((ViewGroup) drawView.getParent()).removeView(drawView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(drawView);

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
     * @return videoList
     */
    protected List<Video> setVideoList(String subCatDir) {

        List<Video> videoList = new ArrayList<>(); //Liste de vidéo

        /*
        if (isAppDirectory(subCatDir)) {
        } //check if valid directory for app
        */

        File subDirContent = this.getExternalFilesDir(subCatDir);

        if(subDirContent.listFiles() != null){        
            if (subDirContent.listFiles().length > 0) {
                for (File videoFileDir : subDirContent.listFiles()) {
                    Log.e("SUB_CONT_FILE", videoFileDir.getAbsolutePath());
                    if (videoFileDir.isDirectory() && videoFileDir.listFiles().length > 0) {
                        Video video = new Video();
                        for (File videoFile : videoFileDir.listFiles()) {
                            if (videoFile.getName().substring(videoFile.getName().lastIndexOf(".") + 1).equals("mp4")) {
                                Log.e("VIDEO", "Video found [" + videoFile.getName() + "]");
                                System.out.println("Nom de la vidéo: " + videoFileDir.getName());
                                video.setFileName(videoFileDir.getName()); //le fichier video porte le même nom que le répertoire qui le contient
                                video.setName(videoFileDir.getName());
                            }

                            if (videoFile.getName().substring(videoFile.getName().lastIndexOf(".") + 1).equals("json")) {
                                VideoAnnotation videoAnnotation = Util.parseJSON(this, subCatDir + File.separator + videoFileDir.getName(), videoFile.getName());
                                video.setVideoAnnotation(videoAnnotation);
                            }
                        }

                        if (video.getFileName() != null && !video.getFileName().isEmpty()) {
                            System.out.println("2) Nom de la vidéo: "+videoFileDir.getName());
                            video.setPath(currentCategorie + File.separator+ currentSubCategorie + File.separator + videoFileDir.getName());
                            videoList.add(video);
                        }
                    }
                }
            }
        }
        else {
            Log.e("SUB_CAT", "No content in " + subCatDir);
        }
        /**
         * On filtre la liste de vidéos, avec le texte de la recherche et la difficulte courante
         */

        return filter(videoList, searchText);
    }


    private List<Video> filter(List<Video> vl, String toSearch) {
        List<Video> ret = new ArrayList<>();
        for (Video inlist : vl) {
            //if (Util.getDifficulteFromJSON(this,inlist.getName()) == currentDifficulte ) {
                /**
                 * La recherche est insensible à la casse
                 */
                String videoNameIgnoreCase = inlist.getFileName().toLowerCase();
                String toSearchIgnoreCase = toSearch.toLowerCase();
                if (videoNameIgnoreCase.contains(toSearchIgnoreCase)) {
                    ret.add(inlist);
                }
            //}
        }
        return ret;
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
                        drawFragment.setPredef(false);
                        drawFragment.setEditing(false);
                        drawFragment.setDrawAnnotation(null);
                        ft.show(drawFragment);
                        ft.hide(annotFragment);
                        ft.commit();
                    }

                    break;
                case R.id.text_annot_btn:
                    player.setPlayWhenReady(false);
                    Annotation textAnnotation = new Annotation("Text Annot ", TEXT);
                    DialogText dialogtext = new DialogText(MainActivity.this, 1);
                    dialogtext.showDialogBox(textAnnotation, MainActivity.this);
                    break;

                    // Lancement du fragment des annotations prédéfinies
                case R.id.annot_predef_btn:
                    player.setPlayWhenReady(false);
                    FragmentTransaction ft2 = fragmentManager.beginTransaction();
                    annotPredefFragment = (Fragment_AnnotPredef)fragmentManager.findFragmentByTag(FRAGMENT_ANNOT_PREDEF_TAG);
                    if (annotPredefFragment == null) {
                        //Si le fragment est null on le créer
                        annotPredefFragment = new Fragment_AnnotPredef(listAnnotationsPredef,MainActivity.this);
                        ft2.add(R.id.annotation_menu, annotPredefFragment, FRAGMENT_ANNOT_PREDEF_TAG);
                        //on cache le fragment des annotations et on affiche celui des annotations prédéfines
                        ft2.hide(annotFragment);
                        ft2.show(annotPredefFragment);
                        ft2.commit();
                    } else {
                        //on cache le fragment des annotations et on affiche celui des annotations prédéfines
                        ft2.hide(annotFragment);
                        ft2.show(annotPredefFragment);
                        ft2.commit();
                    }

                    break;

                case R.id.vignette_btn:
                    player.setPlayWhenReady(false);
                    DialogVignette dialogVignette = new DialogVignette(MainActivity.this);
                    dialogVignette.showDialogVignette();
                    break;

                case R.id.export_video_btn:
                    //A l'appuie du bouton, on lance le plein écran et l'enregistrement de la vidéo commence
                    openFullscreenDialog();

                    exporterVideo();

                    setAnnotButtonStatus(true);
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
            Categorie cat = (Categorie) adapterView.getItemAtPosition(position);
            if (!cat.getSubCategories().isEmpty()){
                currentCategorie = (Categorie) adapterView.getItemAtPosition(position);
                currentSubCategorie = currentCategorie.getSubCategories().get(0);
                searchVideo.setText("");
                spinnerAdapter2.clear();
                spinnerAdapter2.add(new Categorie("Sous-catégorie", null, "/"));
                spinnerAdapter2.addAll(categorieList.get(position).getSubCategories());
                spinnerAdapter2.notifyDataSetChanged();
                spinnerSubCategorie.setSelection(1);
                Log.e("SELECT_CAT", currentCategorie.getPath());
                annotFragment.updateAnnotationList(null);
                if (videosAdapter.getCount() > 0) {
                    currentVideo = (Video) listViewVideos.getItemAtPosition(0);
                    setCurrentVAnnot();
                    if (currentVAnnot == null) {
                        currentVAnnot = Util.createNewVideoAnnotation();
                    }
                    annotFragment.updateAnnotationList(currentVAnnot);
                    videoName = currentVideo.getFileName();

                    if (player != null) {
                        player.stop();
                    }


                    initExoPlayer();

                    if (currentVideo != null) {
                        controlerAnnotation = new ControllerAnnotation(MainActivity.this, currentVideo.getVideoAnnotation(), mainHandler);
                    } else {
                        controlerAnnotation = new ControllerAnnotation(MainActivity.this, null, mainHandler);
                    }
                } else {
                    playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_play));
                    exoplayerPlay = false;
                    initPlayButton();
                    initExoPlayer();
                }
            }
            else {
                videosAdapter.clear();
                spinnerAdapter2.clear();
                spinnerAdapter2.add(new Categorie("Pas de sous-catégories",null));
                initExoPlayer();
                Toast.makeText(view.getContext(), "La catégorie ne possède pas de sous-catégories", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapter) {
        }
    };

    /**
     * listener d'écoute pour la sélection d'un item sous-catégorie
     */
    protected AdapterView.OnItemSelectedListener subCatItemSelectedListener = new AdapterView.OnItemSelectedListener() {

//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//            if (position > 0) {
//                // Here you get the current item that is selected by its position
//                currentSubCategorie = (Categorie) adapterView.getItemAtPosition(position);
//                Log.e("SELECT_SUB_CAT", currentSubCategorie.getPath());
//
//                videosAdapter.clear();
//                videosAdapter.addAll(setVideoList(currentSubCategorie.getPath()));
//                videosAdapter.notifyDataSetChanged();
//
//                if(videosAdapter.getCount()> 0 ){
//                    //currentVideo = videosAdapter.getItem(0);
//                    currentVideo = (Video) listViewVideos.getItemAtPosition(0);
//                    setCurrentVAnnot();
//                    if (currentVAnnot == null) {
//                        currentVAnnot = Util.createNewVideoAnnotation();
//                    }
//
//                    annotFragment.updateAnnotationList(currentVAnnot);
//
//                    videoName = currentVideo.getFileName();
//
//                    if (player != null){
//                        player.stop();
//                    }
//
//
//                    initExoPlayer();
//
//                    if (currentVideo != null) {
//                        controlerAnnotation = new ControllerAnnotation( MainActivity.this, currentVideo.getVideoAnnotation(), mainHandler);
//                    } else {
//                        controlerAnnotation = new ControllerAnnotation(MainActivity.this, null, mainHandler);
//                    }
//                }else {
//                    playIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.exo_controls_play));
//                    exoplayerPlay = false;
//                    initPlayButton();
//                    initExoPlayer();
//                }
//            }
//
//        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position > 0) {
                // Here you get the current item that is selected by its position
                currentSubCategorie = (Categorie) adapterView.getItemAtPosition(position);
                Log.e("SELECT_SUB_CAT", currentSubCategorie.getPath());
                searchVideo.setText("");
                videosAdapter.clear();
                videosAdapter.addAll(setVideoList(currentSubCategorie.getPath()));
                videosAdapter.notifyDataSetChanged();

                annotFragment.updateAnnotationList(null);

                if(videosAdapter.getCount()> 0 ){
                    //currentVideo = videosAdapter.getItem(0);

                    currentVideo = (Video) listViewVideos.getItemAtPosition(0);
                    setCurrentVAnnot();
                    if (currentVAnnot == null) {
                        currentVAnnot = Util.createNewVideoAnnotation();
                    }

                    }
                    annotFragment.updateAnnotationList(currentVAnnot);

                    if (currentVideo != null) {
                        videoName = currentVideo.getFileName();
                    }
                    else {
                        videoName = "";
                    }

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
    public void setErase() {
        drawView.setErase();
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
    public String saveDrawImage(Annotation annotation, boolean isEditing, boolean isPredef) {
        if(!isPredef){
            final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
            //Définition du chemin du fichier
            String drawFileName = videoName + "_" + dateFormat.format(new Date()) + ".png";
            drawView.enregistrer_image(currentSubCategorie.getPath() + File.separator + videoName, drawFileName);
            if(isEditing){
                //supprime l'ancienne image
                String directory = currentSubCategorie.getPath() + File.separator + videoName;
                File pngToDelete = new File(getExternalFilesDir(directory), annotation.getDrawFileName());
                pngToDelete.delete();
            }
            return drawFileName;
        }
        else
            drawView.enregistrer_image(annotPredefDirectory.getName(), annotation.getAnnotationTitle()+".png");
        return annotation.getAnnotationTitle()+".png";
    }

    @Override
    public void onSaveDrawAnnotation(Annotation annotation, boolean check) {
        onSaveAnnotation(annotation,check, drawFragment.isEditing());
        closeDrawFrame();
    }


    //Ne devrait plus exister???
    @Override
    public void onSaveDrawAnnotation(Annotation annotation, int position) {
        //If editing only
        if(drawFragment.isEditing())
            this.currentVAnnot.getAnnotationList().remove(position);
        onSaveDrawAnnotation(annotation, false); //True ou false???
        closeDrawFrame();
    }

    public void onSaveAnnotationPredef(Annotation annotation) {
        if (annotPredefFragment == null) {
            annotPredefFragment = new Fragment_AnnotPredef(listAnnotationsPredef, MainActivity.this);
        }

        File fileToDelete = new File(getExternalFilesDir("annotations"), annotation.getAnnotationTitle() + ".json");
        fileToDelete.delete();

        //Suppression éventuelle du fichier PNG
        if(annotation.getAnnotationType()== DRAW){
            //Le fichier png d'une annotation graphique prédéfinie se nomme différemment que pour une annotation graphique basique
            File drawToDelete = new File(getExternalFilesDir("annotations"), annotation.getAnnotationTitle() + ".png");
            drawToDelete.delete();
            Log.i("ANNOT PREDEF", "Draw updated : " + drawToDelete.getName());
        }

        //Suppression éventuelle du fichier MP3
        if(annotation.getAnnotationType()==AUDIO){
            File audioToDelete = new File(getExternalFilesDir("annotations"), annotation.getAudioFileName());
            audioToDelete.delete();
            Log.i("ANNOT PREDEF", "Audio updated : " + audioToDelete.getName());
        }

        this.currentVAnnot.getAnnotationList().remove(annotation);

//        Util.deleteRecursiveDirectory(annotPredefDirectory);
        System.out.println("DRAWFILENAME 2bis : "+ annotation.getDrawFileName());
        Util.saveAnnotationPredef(MainActivity.this, annotation);

//        annotPredefFragment.getListAnnotationsPredef().remove(annotation);
        annotPredefFragment.updateAnnotationList();
    }

    /**
     * Ajoute l'annotation passée en paramètre dans la liste des annotations de la vidéo courrante
     *
     * @param annotation
     */
    @Override
    public void onSaveAnnotation(Annotation annotation, boolean checkAnnotPredef, boolean isEditing) {

        // création de l'annotation
        if(!isEditing)
            annotation.setAnnotationStartTime(player.getCurrentPosition());

        if(drawFragment!=null && drawFragment.isEditing()){
            currentVAnnot.getAnnotationList().remove(annotation);
        }

        currentVAnnot.getAnnotationList().add(annotation);
        currentVAnnot.setLastModified(Util.DATE_FORMAT.format(new Date()));

        if (currentVAnnot != null && (currentVAnnot.getAnnotationList().size() > 0) && currentSubCategorie.getPath() != null) {
            Collections.sort(currentVAnnot.getAnnotationList(), new AnnotationComparator());
            String directory = currentSubCategorie.getPath() + File.separator + videoName;
            Util.saveVideoAnnotation(MainActivity.this, currentVAnnot, directory, videoName);

            //précise si l'annotation doit être sauvegardé parmis la liste des annotations prédéfinies
            if (checkAnnotPredef) {
                if (annotPredefFragment == null)
                    annotPredefFragment = new Fragment_AnnotPredef(listAnnotationsPredef,MainActivity.this);
                annotPredefFragment.getListAnnotationsPredef().add(annotation);
                System.out.println("NOM DU DRAW "+MainActivity.this.getExternalFilesDir("")+" "+annotation.getDrawFileName());
                // Util.saveVideoAnnotation(MainActivity.this, currentVAnnot, "annotations", videoName);

                if (annotation.getAnnotationType() == DRAW){
                    File imageAnnotation = new File(MainActivity.this.getExternalFilesDir(directory),annotation.getDrawFileName());
                    try {
                        FileUtils.copyFileToDirectory(imageAnnotation,this.annotPredefDirectory);
                        File file = new File(this.annotPredefDirectory, imageAnnotation.getName());
                        file.renameTo(new File(this.annotPredefDirectory, annotation.getAnnotationTitle()+".png"));
                        annotation.setDrawFileName(file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (annotation.getAnnotationType() == AUDIO){
                    File AudioAnnotation = new File(MainActivity.this.getExternalFilesDir(directory),annotation.getAudioFileName());
                    try {
                        FileUtils.copyFileToDirectory(AudioAnnotation,this.annotPredefDirectory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Util.saveAnnotationPredef(MainActivity.this, annotation);
            }

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

    public void setStatutProfil(boolean nouveauStatut) {
        statut_profil = nouveauStatut;
    }

    protected void setCurrentVAnnot() {
        //currentVAnnot = Util.createNewVideoAnnotation(); à supprimer?

        if (currentVideo.getVideoAnnotation() != null) {
            //if currentVideo.
            currentVAnnot = currentVideo.getVideoAnnotation();
        } else {
            currentVAnnot = Util.createNewVideoAnnotation();
        }
    }

    /**
     * Ferme la fragment de gestion de l'annotation graphique et affiche celui de la liste de annotations
     */
    @Override
    public void closeDrawFrame() {
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
        setAnnotButtonStatus(true);
    }

    /**
     * Implémentation du Listener de la class "FramentAnnotation"
     *
     * Gestion de la lecture/affichage de annotations au clic
     * @param annotation
     */
//    @Override
    public void onAnnotItemClick(final Annotation annotation) {

        onAnnotationLauched(annotation);

    }

    @Override
    public void closeAnnotPredef(){

        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        annotPredefFragment = (Fragment_AnnotPredef)fragmentManager.findFragmentByTag(FRAGMENT_ANNOT_PREDEF_TAG);
     /*   if (annotPredefFragment == null) {
            annotPredefFragment = new Fragment_AnnotPredef(listAnnotationsPredef);
            ft2.add(R.id.annotation_menu, annotPredefFragment, FRAGMENT_ANNOT_PREDEF_TAG);
            ft2.hide(annotPredefFragment);
            ft2.show(annotFragment);
            ft2.commit();
        } else {*/
        ft2.hide(annotPredefFragment);
        ft2.show(annotFragment);
        ft2.commit();
        setAnnotButtonStatus(true);
        //  }

    }

    /**
     *Méthode prenant en charge le lancement des annotations
     * @param annotation
     */
    protected void onAnnotationLauched(Annotation annotation) {

        if(ExoPlayerFullscreen) {

        }
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
                    break;
            }

    }

    @Override
    public void onSaveEditAnnot(Annotation annotation, String title, int duree) {
        for(int i = 0; i<currentVAnnot.getAnnotationList().size(); i++)
        {
            System.out.println("Titre : " +currentVAnnot.getAnnotationList().get(i).getAnnotationTitle() +"    "+ annotation.getAnnotationTitle());
            if(currentVAnnot.getAnnotationList().get(i).getAnnotationTitle().matches(annotation.getAnnotationTitle())) {
                System.out.println("Modification en cours");
                currentVAnnot.getAnnotationList().get(i).setAnnotationTitle(title);
                currentVAnnot.getAnnotationList().get(i).setAnnotationDuration(duree);
            }
            if(currentVideo.getVideoAnnotation().getAnnotationList().get(i).getAnnotationTitle().matches(annotation.getAnnotationTitle())){
                currentVideo.getVideoAnnotation().getAnnotationList().get(i).setAnnotationTitle(title);
                currentVideo.getVideoAnnotation().getAnnotationList().get(i).setAnnotationDuration(duree);
                System.out.println("Apres Video modif: " +currentVideo.getVideoAnnotation().getAnnotationList().get(i).getAnnotationTitle());
            }
        }
        annotation.setAnnotationTitle(title);
        annotation.setAnnotationDuration(duree);
        Collections.sort(currentVAnnot.getAnnotationList(), new AnnotationComparator());
        String directory = currentSubCategorie.getPath() + File.separator + videoName;
        Util.saveVideoAnnotation(MainActivity.this, currentVAnnot, directory,videoName);

    }

	// Renommer une annotation prédéfinie
    public void onRenommerAnnotationPredef(Annotation annotation, int position) {
        switch (annotation.getAnnotationType()) {
            case TEXT:
                Log.i("onRenommerAnnotPredef","TEXT");
                player.setPlayWhenReady(false);
                DialogRenameAnnotPredef dialogRenameAnnotPredefText = new DialogRenameAnnotPredef(MainActivity.this, 1);
                dialogRenameAnnotPredefText.showDialogBoxRenommer(annotation, MainActivity.this, position);
                break;

            case DRAW:
                Log.i("onRenommerAnnotPredef","draw");
                player.setPlayWhenReady(false);
                DialogRenameAnnotPredef dialogRenameAnnotPredefDraw = new DialogRenameAnnotPredef(MainActivity.this, 1);
                dialogRenameAnnotPredefDraw.showDialogBoxRenommer(annotation, MainActivity.this, position);
                break;

            case AUDIO:
                Log.i("onRenommerAnnotPredef","AUDIO");
                player.setPlayWhenReady(false);
                DialogRenameAnnotPredef dialogRenameAnnotPredefAudio = new DialogRenameAnnotPredef(MainActivity.this, 1);
                dialogRenameAnnotPredefAudio.showDialogBoxRenommer(annotation, MainActivity.this, position);
                break;
        }
    }

    // Modifier une annotation prédéfinie
    public void onUpdateAnnotationPredef(Annotation annotation, int position) {
        switch (annotation.getAnnotationType()) {
            case TEXT:
                Log.i("onUpdateAnnotPredef","TEXT");
                player.setPlayWhenReady(false);
                DialogAnnotPredefModification dialogAnnotPredefModificationText = new DialogAnnotPredefModification(MainActivity.this, 1);
                dialogAnnotPredefModificationText.showDialogBoxModifPredef(annotation, MainActivity.this, position);
                break;

            case DRAW:
                Log.i("onUpdateAnnotPredef","draw");
                //recupere le dessin de l'annotation a modifier
                Bitmap bitmap = Util.getBitmapFromAppDir(getApplicationContext(), "annotations", annotation.getAnnotationTitle() + ".png");
                player.setPlayWhenReady(false);
                drawView.setVisibility(View.VISIBLE);
                drawView.setOnTouchEnable(true);

                //Affichage du fragment draw : obligation de passer par l'activité pour communiquer entre les deux fragments
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                drawFragment = (Fragment_draw) manager.findFragmentByTag(FRAGMENT_DRAW_TAG);
                if (drawFragment == null) {
                    drawFragment = new Fragment_draw();
                    drawFragment.setEditing(true);
                    drawFragment.setPredef(true);
                    drawFragment.setPosition(position);
                    drawFragment.setDrawAnnotation(annotation);
                    ft.add(R.id.annotation_menu, drawFragment, FRAGMENT_DRAW_TAG);
                    ft.hide(annotFragment);
                    ft.hide(annotPredefFragment);
                    ft.show(drawFragment);
                    ft.commit();
                } else {
                    drawFragment.setEditing(true);
                    drawFragment.setPredef(true);
                    drawFragment.setPosition(position);
                    drawFragment.setDrawAnnotation(annotation);
                    ft.hide(annotFragment);
                    ft.hide(annotPredefFragment);
                    ft.show(drawFragment);
                    ft.commit();
                }
                drawView.invalidate();
                //ajout du dessin a editer a DrawView
                drawView.setmBitmap(bitmap);
                break;


            case AUDIO:
                Log.i("onUpdateAnnotPredef","AUDIO");
                player.setPlayWhenReady(false);
                // à developper
                String directoryPath = currentSubCategorie.getPath() + File.separator + videoName;
                DialogAudio dialog = new DialogAudio(MainActivity.this, directoryPath, player.getContentPosition());
                Annotation auDdioAnnotation = new Annotation(AUDIO);
                dialog.showDialogRecord(auDdioAnnotation, videoName);
                break;
        }
    }


    public void onEditAnnotation(Annotation annotation, int position) {
        final String annotFileDirectory = currentSubCategorie.getPath() + "/" + currentVideo.getFileName();
        //Afficher l'annotation prise en paramètre
        switch (annotation.getAnnotationType()) {
            case TEXT:
                annotFragment.updateAnnotationList(currentVAnnot);
                player.setPlayWhenReady(false);
                DialogAnnotModification dialogAnnotModificationtext = new DialogAnnotModification(MainActivity.this, 1);
                dialogAnnotModificationtext.showDialogBoxModif(annotation, MainActivity.this, position);
                break;
            case DRAW:
                //recupere le dessin de l'annotation a modifier
                Bitmap bitmap = Util.getBitmapFromAppDir(getApplicationContext(), annotFileDirectory, annotation.getDrawFileName());
                player.setPlayWhenReady(false);
                drawView.setVisibility(View.VISIBLE);
                drawView.setOnTouchEnable(true);

                //Affichage du fragment draw : obligation de passer par l'activité pour communiquer entre les deux fragments
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                drawFragment = (Fragment_draw) manager.findFragmentByTag(FRAGMENT_DRAW_TAG);
                if (drawFragment == null) {
                    drawFragment = new Fragment_draw();
                    drawFragment.setEditing(true);
                    drawFragment.setPredef(false);
                    drawFragment.setPosition(position);
                    drawFragment.setDrawAnnotation(annotation);
                    ft.add(R.id.annotation_menu, drawFragment, FRAGMENT_DRAW_TAG);
                    ft.hide(annotFragment);
                    ft.show(drawFragment);
                    ft.commit();
                } else {
                    drawFragment.setEditing(true);
                    drawFragment.setPredef(false);
                    drawFragment.setPosition(position);
                    drawFragment.setDrawAnnotation(annotation);
                    ft.hide(annotFragment);
                    ft.show(drawFragment);
                    ft.commit();
                }
                drawView.invalidate();
                //ajout du dessin a editer a DrawView
                drawView.setmBitmap(bitmap);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRenameAnnotationPredef(Annotation annotation, String oldTitle) {
        //Suppression du fichier JSON
        File fileToDelete = new File(getExternalFilesDir("annotations"), oldTitle + ".json");
        fileToDelete.delete();
        Log.i("ANNOT PREDEF", "File deleted : " + fileToDelete.getName());
        onSaveAnnotationPredef(annotation);
    }

    @Override
    public void onDeleteAnnotationPredef(Annotation annotation, int position) {
        //Récupère le fragment AnnotPredef
        if(annotPredefFragment == null) {
            annotPredefFragment = new Fragment_AnnotPredef(listAnnotationsPredef, MainActivity.this);
        }

        boolean isRemove = true;

        try{
            //Suppression du fichier JSON
            File fileToDelete = new File(getExternalFilesDir("annotations"), annotation.getAnnotationTitle() + ".json");
            fileToDelete.delete();
            Log.i("ANNOT PREDEF", "File deleted : " + fileToDelete.getName());

            //Suppression éventuelle du fichier PNG
            if(annotation.getAnnotationType()== DRAW){
                File drawToDelete = new File(getExternalFilesDir("annotations"), annotation.getDrawFileName());
                drawToDelete.delete();
                Log.i("ANNOT PREDEF", "Draw deleted : " + drawToDelete.getName());
            }

            //Suppression éventuelle du fichier MP3
            if(annotation.getAnnotationType()==AUDIO){
                File audioToDelete = new File(getExternalFilesDir("annotations"), annotation.getAudioFileName());
                audioToDelete.delete();
                Log.i("ANNOT PREDEF", "Audio deleted : " + audioToDelete.getName());
            }
        } catch (Exception e){
            isRemove = false;
            Log.e("ANNOT PREDEF", "Erreur dans la suppression de l'annotation prédéfinie");
        }

        // Rafraîchit la listView si l'element est bien supprimé
        if (isRemove){
            //Supprime l'annotation de la liste des annotations
            annotPredefFragment.getListAnnotationsPredef().remove(annotation);
            Log.i("ANNOT PREDEF","Element in AnnotationPredefList deleted : " + annotation.getAnnotationTitle());
            //Met à jour la liste dans le fragment
            annotPredefFragment.updateAnnotationList();
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
        //PERMISSION ENREGISTREMENT ECRAN
        if (CAST_PERMISSION_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
                startRecording();
            } else {
                AnnulationEnregistrementVideo();
            }
        }

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null && data.getData() != null) {
                Uri filePath = data.getData();
                String s = Util.getRealPathFromURI(this, filePath);
                fileVideoImport = new File(Util.getRealPathFromURI(this, filePath));
                videoImportName.setText(fileVideoImport.getName());
            }
        }

        //Gestion des Catégories
        if (requestCode == READ_CATEGORY_CODE && resultCode != Activity.RESULT_CANCELED) {
            if (!(data.getStringArrayListExtra("Categorie").isEmpty())) {
                categorieList = new ArrayList<>();
                List<Categorie> subcategorieList;
                categorieList = data.getParcelableArrayListExtra("Categorie");
                spinnerAdapter=new SpinnerAdapter(this, android.R.layout.simple_spinner_item, categorieList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategorie.setAdapter(spinnerAdapter);
//                Création des répertoires pour les catégories et sous-catégories
                File Dir = this.getExternalFilesDir("");
                System.out.println("DIR "+ Dir);
                File subDir;
                for(int i=1; i<categorieList.size(); i++){
                    File newDir=new File(Dir,categorieList.get(i).getOldName());
//                En cas de modification du nom de la catégorie, modification du nom du répertoire
                    if ((!categorieList.get(i).getName().matches(categorieList.get(i).getOldName())))
                    {
                        File renamed = new File(Dir,categorieList.get(i).getName());
                        System.out.println("Test3 "+Dir);
                        if (newDir.exists()) {
                            System.out.println("Nouvellement renommé !");
                            newDir.renameTo(renamed);
                        }
                    }
//                Si le repertoire n'existe pas on le crée
                    if (categorieList.get(i).getName().matches(categorieList.get(i).getOldName()) && !newDir.exists()) {
                        newDir.mkdir();
                        System.out.println("Nouvellement créé !");
                        subDir = this.getExternalFilesDir("./" + newDir.getName());
//                On crée les répertoires pour les sous-catégories de la catégorie
                        for(Categorie cat: categorieList.get(i).getSubCategories())
                        {
                            File newSubCat = new File(subDir,cat.getName());
                            newSubCat.mkdir();
                            System.out.println("subCat  :" + newSubCat.exists());
                        }
                    }

//                On fait de même pour les sous-catégories
                    subcategorieList=categorieList.get(i).getSubCategories();
                    for(int j=0; j<subcategorieList.size();j++){
//                        File newsubDir=new File("./"+categorieList.get(i).getName(),subcategorieList.get(j).getOldName());
                        File newsubDir=new File(Dir+"/"+subcategorieList.get(j).getParentName(),subcategorieList.get(j).getOldName());
//                        System.out.println("TEST path " + newsubDir);
                        if ((!subcategorieList.get(j).getName().matches(subcategorieList.get(j).getOldName())))
                        {
                            File subnamed = new File(Dir +"/"+subcategorieList.get(j).getParentName(),subcategorieList.get(j).getName());
                            if (newsubDir.exists()) {
                                System.out.println(newsubDir.renameTo(subnamed));
                            }
                        }
//                Si le repertoire n'existe pas on le crée
                        if (subcategorieList.get(j).getName().matches(subcategorieList.get(j).getOldName()) && !newsubDir.exists()) {
                            System.out.println("BRAVO! "+newsubDir.mkdir());
                        }
                    }
                }
//                Util.reInitCatList(this,categorieList);
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
    public void saveImportVideo(Categorie categorie, int difficulte) {
        Util.saveImportVideoFile(this,categorie,fileVideoImport);
        String difficulteString = "" + difficulte;
        Util.saveNewVideoDifficulte(this, fileVideoImport.getName(), difficulteString);
        videosAdapter.clear();
        videosAdapter.addAll(setVideoList(currentSubCategorie.getPath()));
        //Création de la vignette
        System.out.println(categorie.getName()+"/"+categorie.getParentName()+"/"+fileVideoImport.getName());
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(fileVideoImport.getAbsolutePath());
        int time = 1;
        Bitmap bitmap =retriever.getFrameAtTime(time,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        try {
            FileOutputStream fos = new FileOutputStream(new File(getExternalFilesDir(categorie.getParentName()+"/"+categorie.getName()+"/"+fileVideoImport.getName().replace(".mp4","")), "vignette" + ".png"));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        annotPredefBtn.setEnabled(status);
        vignetteBtn.setEnabled(status);
        exportVideoBtn.setEnabled(status);
    }

    //Edition des infos de la video via context menu
    //Modification du nom du repertoire et de la video
    @Override
    public void onSaveEditVideo(Video video, String title) {

        Util.saveEditVideoName(this, video.getName(), title);
        // A voir, de Sebastien,   String subCatDir= currentCategorie + "/" +video.getPath();
        String subCatDir= video.getPath();

        File subDirContent = this.getExternalFilesDir(subCatDir);
        File renamed = new File(this.getExternalFilesDir(currentCategorie + "/" + currentSubCategorie),title);
        subDirContent.renameTo(renamed);
        subDirContent=this.getExternalFilesDir(currentCategorie + "/" + currentSubCategorie+"/"+title);
        System.out.println("TOMATO" + subDirContent.getAbsolutePath());
        if (subDirContent.listFiles().length > 0) {
            for (File videoFileDir : subDirContent.listFiles()) {
                Log.e("SUB_CONT_FILE", videoFileDir.getAbsolutePath());
                    if (videoFileDir.getName().substring(videoFileDir.getName().lastIndexOf(".") + 1).equals("mp4")) {
                        File from = new File(subDirContent, video.getFileName() + ".mp4");
                        File to = new File(subDirContent, title + ".mp4");
                        if (from.exists()) {
                            from.renameTo(to);
                        }
                    }
                    if (videoFileDir.getName().substring(videoFileDir.getName().lastIndexOf(".") + 1).equals("json")) {
                        File from = new File(subDirContent,video.getFileName()+".json");
                        File to = new File(subDirContent,title+".json");
                        if(from.exists())
                            from.renameTo(to);
                        }
                    }
        } else {
            Log.e("SUB_CAT", "No content in " + subCatDir);
        }

        video.setPath(currentCategorie + File.separator+ currentSubCategorie + File.separator + title);
        //Modification dans le fichier JSON difficulte

        video.setFileName(title);
        video.setName(title);
        videosAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void onSaveEditDifficulte(Video video, int d) {
        Util.saveEditDifficulte(this, video.getName(), d);
    }

    // Copie les fichiers (images, fichiers mp4) du dossier d'annotations prédéfini vers le dossier de la vidéo courante
    public void CopyFileAnnotPredef (Annotation annotation){
        // Copie d'un fichier image
        if (annotation.getAnnotationType() == DRAW){
            File ImageAnnotation = new File(MainActivity.this.getExternalFilesDir("annotations"),annotation.getDrawFileName());
            File DossierCurrentVideo = new File(MainActivity.this.getExternalFilesDir(currentSubCategorie.getPath()),currentVideo.getFileName());
            try {
                FileUtils.copyFileToDirectory(ImageAnnotation,DossierCurrentVideo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Copie d'un fichier audio
        if (annotation.getAnnotationType() == AUDIO){
            File AudioAnnotation = new File(MainActivity.this.getExternalFilesDir("annotations"),annotation.getAudioFileName());
            File DossierCurrentVideo = new File(MainActivity.this.getExternalFilesDir(currentSubCategorie.getPath()),currentVideo.getFileName());
            try {
                FileUtils.copyFileToDirectory(AudioAnnotation,DossierCurrentVideo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Active/Désactive les boutons
   public void OnOffBoutons(boolean bouton){
       setAnnotButtonStatus(bouton);
   }

//EXPORT VIDEO
   public void exporterVideo(){
       mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

       if(recording){
           recording=false;
           releaseEncoders();
       }
       else {
           //Il y a besoin de recréer le mDrainEncodeRunnable à chaque enregistrement.
           mDrainEncoderRunnable = new Runnable() {
               @Override
               public void run() {
                   drainEncoder();
                   // Lorsqu'on arrive à la fin de la vidéo :
                   if(recording && player.getDuration() >=0 && player.getCurrentPosition() >= player.getDuration()) {
                       Log.e("testest", "Test fin vidéo.");
                       //- on l'enregistre
                       exporterVideo();
                       //- on quitte le plein écran
                       closeFullscreenDialog();
                       playButton.callOnClick();
                       //- on réaffiche le player de la vidéo
                       exoPlayerView.setUseController(true);
                   }
               }
           };
           //Dialog contenant l'avertissement pour l'utilisation de l'enregistrement de vidéo et le champ pour rentrer le nom de la vidéo.
           DialogValidationEnregistrementVideo dialogValidationEnregistrementVideo = new DialogValidationEnregistrementVideo();
           dialogValidationEnregistrementVideo.showDialogValidationEnregistrementVideo(MainActivity.this);
       }
   }


   //Si l'enregistrement est annulé, il faut enlever le grand écran.
   public void AnnulationEnregistrementVideo(){
        closeFullscreenDialog();
        recording = false;
   }

   public void validationEnregistrementVideo(){
       recording=true;
       //demande de permission d'enregistrer l'ecran
       Intent permissionIntent = mProjectionManager.createScreenCaptureIntent();
       startActivityForResult(permissionIntent, CAST_PERMISSION_CODE);
   }


    private void startRecording() {
        //lancement de la video au debut de l'enregistrement
//        playButton.callOnClick();

        DisplayManager dm = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
        Display defaultDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
        if (defaultDisplay == null) {
            throw new RuntimeException("No display found.");
        }
        prepareVideoEncoder();

        try {
            Log.e(Environment.getExternalStorageDirectory() + "/Android/data"+ File.separator, "on est là");
            //emplacement de stockage de la vidéo
            mMuxer = new MediaMuxer(Environment.getExternalStorageDirectory()+ File.separator + nomVideoAEnregistrer + ".mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            //Le nom de vidéo doit être réinitialisé.
            nomVideoAEnregistrer = "";
        } catch (IOException ioe) {
            throw new RuntimeException("MediaMuxer creation failed", ioe);
        }

        // Get the display size and density.
        metrics = getResources().getDisplayMetrics();
        screenDensity = metrics.densityDpi;
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        // Start the video input.
        mMediaProjection.createVirtualDisplay("Recording Display", screenWidth,
                screenHeight, screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY, mInputSurface,
                //.VIRTUAL_DISPLAY_FLAG_PRESENTATION, mInputSurface,
                null /* callback */, null /* handler */);

        // Start the encoders
        drainEncoder();
    }



    private final Handler mDrainHandler = new Handler(Looper.getMainLooper());
    private Runnable mDrainEncoderRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("run", "on y passe");
            drainEncoder();
            // Lorsqu'on arrive à la fin de la vidéo :
            if(recording && player.getDuration() >=0 && player.getCurrentPosition() >= player.getDuration()) {
                //- on l'enregistre
                exporterVideo();
                //- on quitte le plein écran
                closeFullscreenDialog();
                //- on réaffiche le player de la vidéo
                exoPlayerView.setUseController(true);
            }
        }
    };


    private void prepareVideoEncoder() {
        Log.e("prepareVideoEncoder", "on y passe");
        mVideoBufferInfo = new MediaCodec.BufferInfo();
        MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        int frameRate = 30; // 30 fps


        // Set some required properties. The media codec may fail if these aren't defined.
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000); // 6Mbps
        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        format.setInteger(MediaFormat.KEY_CAPTURE_RATE, frameRate);
        format.setInteger(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / frameRate);
        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); // 1 seconds between I-frames
        format.setInteger("rotation-degres", 90);


        try {
            mVideoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
            mVideoEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mInputSurface = mVideoEncoder.createInputSurface();
            mVideoEncoder.start();
        } catch (IOException e) {
            releaseEncoders();
        }
    }

    private boolean drainEncoder() {
        mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
        while (true) {
            int bufferIndex = mVideoEncoder.dequeueOutputBuffer(mVideoBufferInfo, 0);
            if (bufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // nothing available yet
                break;
            } else if (bufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // should happen before receiving buffers, and should only happen once
                if (mTrackIndex >= 0) {
                    throw new RuntimeException("format changed twice");
                }
                mTrackIndex = mMuxer.addTrack(mVideoEncoder.getOutputFormat());
                if (!mMuxerStarted && mTrackIndex >= 0) {
                    mMuxer.start();
                    mMuxerStarted = true;
                }
            } else if (bufferIndex < 0) {
                // not sure what's going on, ignore it
            } else {
                ByteBuffer encodedData = mVideoEncoder.getOutputBuffer(bufferIndex);
                if (encodedData == null) {
                    throw new RuntimeException("couldn't fetch buffer at index " + bufferIndex);
                }

                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    mVideoBufferInfo.size = 0;
                }

                if (mVideoBufferInfo.size != 0) {
                    if (mMuxerStarted) {
                        encodedData.position(mVideoBufferInfo.offset);
                        encodedData.limit(mVideoBufferInfo.offset + mVideoBufferInfo.size);
                        mMuxer.writeSampleData(mTrackIndex, encodedData, mVideoBufferInfo);
                    } else {
                        // muxer not started
                    }
                }

                mVideoEncoder.releaseOutputBuffer(bufferIndex, false);

                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }
        }

        mDrainHandler.postDelayed(mDrainEncoderRunnable, 10);
        return false;
    }

    private void releaseEncoders() {
        mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
        if (mMuxer != null) {
            if (mMuxerStarted) {
                mMuxer.stop();
            }
            mMuxer.release();
            mMuxer = null;
            mMuxerStarted = false;
        }
        if (mVideoEncoder != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
            mVideoEncoder = null;
        }
        if (mInputSurface != null) {
            mInputSurface.release();
            mInputSurface = null;
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        mVideoBufferInfo = null;
        mDrainEncoderRunnable = null;
        mTrackIndex = -1;
    }
    //END EXPORT VIDEO

/*
    public void writeDifficulteVideoIntoJSON(String newVideoName, int newDifficulte) {
        try {
            //Reader et Writer pour modifier le fichier
            FileReader reader = new FileReader(fileVideoDifficulte);

            //Il y aura un traitement légèremnt different si c'est le premier couple (vidéo, difficulte) que l'on ajoute, donc si le fichier est vide.
            boolean vide;

            //Variable pour le nouveau contenu du fichier
            String newcontent = "" ;

            //Variable pour l'ancien contenu du fichier
            String content = "";

            //La longueur du fichier est le nombre de caractères présents dans le fichiers JSON.
            char[] a = new char[(int)fileVideoDifficulte.length()];
            //On lit le fichier caractère par caratère.;
            reader.read(a);
            for (char c : a) {
                //On ajoute caractère par caractère le contenu du fichier dans un string
                content += c;
            }

            reader.close();
            // On supprime le fichier...
            fileVideoDifficulte.delete();
            // ... pour recréer un fichier vide ...
            fileVideoDifficulte.createNewFile();

            // ... dans lequel on va réécrire nos anciennes données ...
            FileWriter writer = new FileWriter(fileVideoDifficulte);
            if (content=="") {
                /**
                 * Si le fichier est vide, il faut donc créer la liste de couple (nom de video, difficulte)
                 * On commence donc par le nom du tableau (pour récupération)
                 * puis par l'accolade ouvrante
                 *//*
                content += "videodifficulte:[";
                vide = true;
            }
            else {
                /**
                 * Si le fichier n'est pas vide, il contient l'accolade fermante de la fin de la liste,
                 * or, on veut écrire juste avant la fin de la liste.
                 * On supprime les deux derniers caractères ('\n' et ']' )
                 *//*
                newcontent = content.substring(0,content.length()-2);
                vide = false;
            }

            // ... et les nouvelles données.
            if (vide) {
                /**
                 * Si le fichier est vide, il ne doit pas y avoir de virgule entre l'accolade ouvrante et le premier couple (nomvideo, difficulte)
                 *//*
                newcontent = content + "\n{\"name\": \"" + newVideoName + "\n\"difficulte\": \"" + newDifficulte + "\"}\n]";
            } else {
                /**
                 * Sinon, on ajoute à la fin de la liste le couple (nouvelle video, nouvelle difficulte, et on ferme la liste
                 *//*
                newcontent = content + ",\n{\"name\": \"" + newVideoName + "\n\"difficulte\": \"" + newDifficulte + "\"}\n]";
            }
            writer.write(newcontent);
            Log.i("writejson", newcontent);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void editDifficulteIntoJSON(final String videoName, int new_difficulte){
        JsonParser parser = new JsonParser();
        JsonElement couples;
        try {
            FileReader reader = new FileReader(fileVideoDifficulte);
            couples = parser.parse(reader);
            JsonArray videoDifficulte = couples.getAsJsonArray();
            int iter=0;
            boolean trouve = false;
            do {
                JsonElement couple = videoDifficulte.get(iter);
                String nom = couple.getAsJsonObject().get("name").toString();
                if (nom == videoName) {
                    trouve = true;
                    videoDifficulte.remove(iter);
                }
                iter++;
            } while ( (iter < videoDifficulte.size()) && (!trouve) );

            char[] a = new char[(int)fileVideoDifficulte.length()];
            reader.read(a);
            String content = "";
            for (char c : a) {
                content += c;
            }
            content.substring(0,content.length()-1);
            content += "\"name\":\"" + videoName + "\", \"difficulte\":\"" + new_difficulte + "\"\n]";

            FileWriter writer = new FileWriter(fileVideoDifficulte);
            // On supprime le fichier...
            fileVideoDifficulte.delete();
            // ... pour recréer un fichier vide ...
            fileVideoDifficulte.createNewFile();
            // ... dans lequel on va réécrire nos données ...
            writer.write(content);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void editVideoNameIntoJSON(final String videoName, String new_videoName) {
        JsonParser parser = new JsonParser();
        JsonElement couples;
        try {
            FileReader reader = new FileReader(fileVideoDifficulte);
            couples = parser.parse(reader);
            JsonArray videoDifficulte = couples.getAsJsonArray();
            int iter=0;
            String difficulte = "";
            boolean trouve = false;
            do {
                JsonElement couple = videoDifficulte.get(iter);
                String nom = couple.getAsJsonObject().get("name").toString();
                if (nom == videoName) {
                    trouve = true;
                    videoDifficulte.remove(iter);
                    difficulte = couple.getAsJsonObject().get("difficulte").toString();
                }
                iter++;
            } while ( (iter < videoDifficulte.size()) && (!trouve) );

            char[] a = new char[(int)fileVideoDifficulte.length()];
            reader.read(a);
            String content = "";
            for (char c : a) {
                content += c;
            }
            content.substring(0,content.length()-1);
            content += "\"name\":\"" + new_videoName + "\", \"difficulte\":\"" + difficulte + "\"\n]";

            FileWriter writer = new FileWriter(fileVideoDifficulte);
            // On supprime le fichier...
            fileVideoDifficulte.delete();
            // ... pour recréer un fichier vide ...
            fileVideoDifficulte.createNewFile();
            // ... dans lequel on va réécrire nos données ...
            writer.write(content);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getDifficulte(final String videoName) {

        JsonParser parser = new JsonParser();
        JsonElement jsonObject;
        try {
            jsonObject = parser.parse(new FileReader(fileVideoDifficulte));
            JsonArray videoDifficulte = jsonObject.getAsJsonArray();
            for (int iter = 0; iter < videoDifficulte.size(); iter++) {
                JsonElement couple = videoDifficulte.get(iter);
                String nom = couple.getAsJsonObject().get("name").toString();
                String findDifficulte = couple.getAsJsonObject().get("difficulte").toString();
                if (nom == videoName) {
                    switch (findDifficulte) {
                        case "1":
                            return 1;
                        case "2":
                            return 2;
                        case "3":
                            return 3;
                    } //ENDSWITCH
                } //ENDIF
            } //ENDFOR
        } catch (Exception e) {
            e.printStackTrace();
        } //ENDTRYCATCH
        return 0;
    } //ENDMETHODE

    public void deleteVideoIntoJSON(final String videoName) {
        JsonParser parser = new JsonParser();
        JsonElement jsonObject;
        try {
            FileReader reader = new FileReader(fileVideoDifficulte);
            jsonObject = parser.parse(reader);
            JsonArray videoDifficulte = jsonObject.getAsJsonArray();
            int iter=0;
            String difficulte = "";
            boolean trouve = false;
            do {
                JsonElement couple = videoDifficulte.get(iter);
                String nom = couple.getAsJsonObject().get("name").toString();
                if (nom == videoName) {
                    trouve = true;
                    videoDifficulte.remove(iter);
                }
                iter++;
            } while ( (iter < videoDifficulte.size()) && (!trouve) );

            char[] a = new char[(int)fileVideoDifficulte.length()];
            reader.read(a);
            String content = "";
            for (char c : a) {
                content += c;
            }

            FileWriter writer = new FileWriter(fileVideoDifficulte);
            // On supprime le fichier...
            fileVideoDifficulte.delete();
            // ... pour recréer un fichier vide ...
            fileVideoDifficulte.createNewFile();
            // ... dans lequel on va réécrire nos données ...
            writer.write(content);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }*/

//END RECORDER
   //Changement de vignette
    @Override
    public void onSaveEditVignette() {
        File videoFile= getExternalFilesDir(currentVideo.getPath()+"/"+currentVideo.getName()+".mp4");
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoFile.getAbsolutePath());
        //Récupère le temps en ms converti en µs
        long time = round(player.getCurrentPosition())*1000;
        System.out.println(videoFile.getAbsolutePath() + "      " + videoFile.exists() + "      " + (player.getCurrentPosition()));
        Bitmap bitmap =retriever.getFrameAtTime(time,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        try {
            FileOutputStream fos = new FileOutputStream(new File(getExternalFilesDir(currentVideo.getPath()).getAbsolutePath(), "vignette" + ".png"));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            videosAdapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
