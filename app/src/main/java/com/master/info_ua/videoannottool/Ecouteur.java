package com.master.info_ua.videoannottool;

import com.google.android.exoplayer2.SimpleExoPlayer;

public interface Ecouteur {

    // permet d'acceder au lecteur
    public SimpleExoPlayer getPlayer();
    // modifie la vitesse de lecture de l'aplication < 1.0f pour réduire et > pour accélerer
    public void setSpeed(float speed);
    // donne le temps courant de la video
    public long getVideoTime();
}
