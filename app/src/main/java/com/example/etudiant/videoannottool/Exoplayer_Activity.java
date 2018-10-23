package com.example.etudiant.videoannottool;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;

public class Exoplayer_Activity extends Activity {
    //0. déclaration des éléments pour le lecteur
    SimpleExoPlayer player;
    MediaSource videoSource;
    DefaultExtractorsFactory DataSourceFactory;

     /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 2);
                }
            }
    else{
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.test;


        SimpleExoPlayerView exoPlayerView = findViewById(R.id.player_view);

        //1. creating an ExoPlayer with default parameters from Getting Started guide(https://google.github.io/ExoPlayer/guide.html)
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        DataSourceFactory = new DefaultExtractorsFactory();
        //2. prepare video source from url
        //        videoSource = new ExtractorMediaSource(Uri.parse(path), DataSourceFactory,
        //                new DefaultExtractorsFactory(), null, null);
        Uri uri = Uri.fromFile(new java.io.File("/sdcard/DCIM/Camera/test.mp4"));
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
        player.setPlayWhenReady(true);
        player.prepare(videoSource, false, false);


//3. partie pour gerer l'audio du lecteur
/*       AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


public boolean onKeyDown(int keyCode, KeyEvent event) {
if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN){
volumeSeekBar.setProgress(audioManager.getStreamVolume(player.getAudioStreamType()));
}
return super.onKeyDown(keyCode, event);
}

@Override
public boolean onKeyUp(int keyCode, KeyEvent event) {
if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
volumeSeekBar.setProgress(audioManager.getStreamVolume(player.getAudioStreamType()));
}
return super.onKeyUp(keyCode, event);
}

volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
@Override
public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
audioManager.setStreamVolume(player.getAudioStreamType(), i, 0);
}

@Override
public void onStartTrackingTouch(SeekBar seekBar) {

}

@Override
public void onStopTrackingTouch(SeekBar seekBar) {

}
});*/

    }
}
