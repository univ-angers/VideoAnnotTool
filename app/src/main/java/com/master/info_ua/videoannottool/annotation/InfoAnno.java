package com.master.info_ua.videoannottool.annotation;

public class InfoAnno implements Comparable {
    // date de lancement de la méthode arrondit a la seconde inférieur
    private long time;
    // position de l'annotation dans la list
    private int index;
    // true pour launch() et false pour stop()
    private boolean debut;

    public InfoAnno(long _time, int _index, boolean _debut) {
        time = _time;
        index = _index;
        debut = _debut;
    }

    // methode de Override de comparable peremettant d'utiliser des librairies sort sur les des Arraylist<InfoAnno>
    @Override
    public int compareTo(Object comparestu) {
        int compareTime = (int) ((InfoAnno) comparestu).getTime();
        /* For Ascending order*/
        return (int) (this.time) - compareTime;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }

    //setter et getters
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