package com.master.info_ua.videoannottool.annotation;

public class InfoAnno implements Comparable{
    private long time;
    private int index;
    private boolean debut; // true pour launch() et false pour stop()

    public InfoAnno(long _time, int _index, boolean _debut){
        time = _time;
        index = _index;
        debut = _debut;
    }

    @Override
    public int compareTo(Object comparestu) {
        int compareTime=(int)((InfoAnno)comparestu).getTime();
        /* For Ascending order*/
        return (int)(this.time)-compareTime;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }

    public long getTime() {
        return time;
    }

    public int getIndex() {
        return index;
    }

    public boolean isDebut() {
        return debut;
    }


}