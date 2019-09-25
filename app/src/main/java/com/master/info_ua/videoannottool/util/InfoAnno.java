package com.master.info_ua.videoannottool.util;

public class InfoAnno implements Comparable {
    // Date de lancement de la méthode arrondie à la seconde inférieure
    private long time;
    // Position de l'annotation dans la liste
    private int index;
    // True pour launch() et false pour stop()
    private boolean debut;

    public InfoAnno(long _time, int _index, boolean _debut) {
        time = _time;
        index = _index;
        debut = _debut;
    }

    // Permet de trier des éléments de type ArrayList<InfoAnno>
    @Override
    public int compareTo(Object comparestu) {
        int compareTime = (int) ((InfoAnno) comparestu).getTime();
        return (int) (this.time) - compareTime;
    }

    // Getters & setters
    public long getTime() { return time; }
    public int getIndex() {
        return index;
    }
    public boolean isDebut() {
        return debut;
    }


}