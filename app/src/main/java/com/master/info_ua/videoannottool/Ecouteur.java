package com.master.info_ua.videoannottool;

import com.google.android.exoplayer2.SimpleExoPlayer;

public interface Ecouteur {
    public SimpleExoPlayer getPlayer();
    public void setSpeed(float speed);
    public long getVideoTime();
}
