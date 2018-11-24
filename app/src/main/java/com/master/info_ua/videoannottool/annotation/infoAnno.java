package com.master.info_ua.videoannottool.annotation;

public class infoAnno{
    public long getTime() {
        return time;
    }

    public int getIndex() {
        return index;
    }

    public boolean isDebut() {
        return debut;
    }

    private long time;
    private int index;
    private boolean debut; // true pour launch() et false pour stop()
}