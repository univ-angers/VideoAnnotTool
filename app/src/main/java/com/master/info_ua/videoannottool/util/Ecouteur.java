package com.master.info_ua.videoannottool.util;

import com.google.android.exoplayer2.SimpleExoPlayer;

public interface Ecouteur {
    // Permet d'acceder au lecteur
    SimpleExoPlayer getPlayer();
    // Modifie la vitesse de lecture de l'aplication < 1.0f pour réduire et > pour accélerer
    void setSpeed(float speed);
    // Donne le temps courant de la video
    long getVideoCurrentPosition();
}
