package com.master.info_ua.videoannottool.annotation;

import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.master.info_ua.videoannottool.Ecouteur;
import com.master.info_ua.videoannottool.annotation.infoAnno;

import java.util.ArrayList;
import java.util.List;


public class ControlerAnnotation {

    private Ecouteur m_ecouteur = null;
    private ArrayList<infoAnno> ListInfoAnno;
    private VideoAnnotation listAnno;
    private int last_pos = 0;
    private List<Integer> annotActive;

    public ControlerAnnotation(Ecouteur m_e, VideoAnnotation videoAnnotation){
        m_ecouteur = m_e;
        listAnno = videoAnnotation;
        //ttt de videoAnnotation
    }

    public void updateAnno(VideoAnnotation update){
        listAnno = update;
    }

    // verifie si il existe des annotation qui n'ont pas été lancer depuis le dernier appel et positionne le curseur sur le prochain element a vérifier
    // stop aussi les annotation qui doive se terminer
    public void checkTime(){
        //verifier le temps et lancer launch avec le paramétre correspondant
        while((last_pos<ListInfoAnno.size()&&(arronditSeconde(m_ecouteur.getVideoTime())<ListInfoAnno.get(last_pos).getTime()))){
            if(arronditSeconde(m_ecouteur.getVideoTime())==ListInfoAnno.get(last_pos).getTime()) {
                if (ListInfoAnno.get(last_pos).isDebut()) {
                    launch(listAnno.getAnnotationList().get(ListInfoAnno.get(last_pos).getIndex()));
                    annotActive.add(ListInfoAnno.get(last_pos).getIndex());
                } else {
                    stop(listAnno.getAnnotationList().get(ListInfoAnno.get(last_pos).getIndex()));
                    annotActive.remove(ListInfoAnno.get(last_pos).getIndex());
                }
            }
            last_pos++;
        }
    }

    public long arronditSeconde(long time){
        return (time % 1000)*1000;
    }

    public void launch(Annotation anno){
        switch(anno.getAnnotationType()){
            case DRAW:
                launchDraw();
                break;
            case TEXT:
                launchText();
                break;
            case ZOOM:
                launchZoom();
                break;
            case AUDIO:
                launchAudio();
                break;
            case SLOWMOTION:
                launchSlowmo(anno.getSlowMotionSpeed());
                break;
            default:
                System.out.println("Error de lancement");
                break;
    }
}

    public void stop(Annotation anno){
        switch(anno.getAnnotationType()){
            case DRAW:
                stopDraw();
                break;
            case TEXT:
                stopText();
                break;
            case ZOOM:
                stopZoom();
                break;
            case AUDIO:
                stopAudio();
                break;
            case SLOWMOTION:
                stopSlowmo();
                break;
            default:
                System.out.println("Error de d'arret");
                break;
        }
    }

    public void launchDraw(){
        //encore du travail
    }

    public void launchText(){
        //encore du travail
    }

    public void launchZoom(){
        //encore du travail
    }

    public void launchAudio(){
        //encore du travail
    }

    public void launchSlowmo(float slow){
        m_ecouteur.setSpeed(slow);
    }

    public void stopDraw(){
        //encore du travail
    }
    public void stopText(){
        //encore du travail
    }
    public void stopZoom(){
        //encore du travail
    }
    public void stopAudio(){
        //encore du travail
    }
    public void stopSlowmo(){
        m_ecouteur.setSpeed(1f);
    }
}