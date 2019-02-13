package com.master.info_ua.videoannottool.annotation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.master.info_ua.videoannottool.util.Ecouteur;
import com.master.info_ua.videoannottool.util.InfoAnno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ControllerAnnotation implements Runnable {
    // Context utiliser pour crée les tache envoyé au Main Thread
    private Context _mainActivity;
    // L'interface de notre player pour avoir accés aux methodes de ce dernier
    private Ecouteur m_ecouteur;
    // file d'excécution contenant des objet indiquant le temps, la position et si il faut lancer ou arreter l'annotation
    private ArrayList<InfoAnno> listInfoAnno = new ArrayList<>();
    // liste des annotation contituer a partir de celle de Video anotaion
    private List<Annotation> annotationList;
    // index sur la file permettant de parcourir la file la ou nous en étions rendu
    private int last_pos = 0;
    // si passée a true arret soft du thread, nescessaire en android
    private boolean cancelled = false;
    private boolean isAnnotListEmpty = false;
    // nous permet d'envoyer des tache a éxecuter a dans notre mainActivity
    private Handler mainHandler;

    // constructeurs comme expliquer dans le rapport nous lui passont dans le main  deux fois la même valeur mais a termes cela devrait changer
    public ControllerAnnotation(Context context, Ecouteur m_e, VideoAnnotation videoAnnotation, Handler _mainHandler) {
        mainHandler = _mainHandler;
        _mainActivity = context;
        m_ecouteur = m_e;
        annotationList = new ArrayList<>();

        if (videoAnnotation != null) {
            annotationList = videoAnnotation.getAnnotationList();
            if(annotationList.size() < 1){
                isAnnotListEmpty = true;
                cancelled = true;

                // traitement de videoAnnotation
                int i = 0;
                for (Annotation annotation : annotationList) {
                    InfoAnno tmp = new InfoAnno(arronditSeconde(annotation.getAnnotationStartTime()), i, true);
                    listInfoAnno.add(tmp);
                    i++;
                }
                Collections.sort(listInfoAnno);
            }

        }else {
            isAnnotListEmpty = true;
            cancelled = true;
        }
    }


    // verifie si il existe des annotation qui n'ont pas été lancer depuis le dernier appel et positionne le curseur sur le prochain element a vérifier
    // stop aussi les annotation qui doive se terminer
    public boolean checkTime() {
        //verifier le temps et lancer launch avec le paramétre correspondant
        if (m_ecouteur.getPlayer().getDuration() >= m_ecouteur.getVideoCurrentPosition()) {
            if (last_pos < listInfoAnno.size()) { // ==> Switch while() to if()
                Log.e("Pos_VS_Size", "Last_Pos ["+last_pos+"] Size ["+listInfoAnno.size()+"]");
                if (arronditSeconde(m_ecouteur.getVideoCurrentPosition()) == listInfoAnno.get(last_pos).getTime()) {
                    if (listInfoAnno.get(last_pos).isDebut()) {
                        launch(annotationList.get(listInfoAnno.get(last_pos).getIndex()));
                        Log.e("Tag_Last_Pos", "Last position: "+last_pos+" Title: ["+ annotationList.get(listInfoAnno.get(last_pos).getIndex()).getAnnotationTitle()+"]");
                        listInfoAnno.remove(last_pos);
                        //listInfoAnno.get(last_pos).setDebut(false);
                        //annotActive.add(listInfoAnno.get(last_pos).getIndex());
                    } else {
                        //stop(annotationList.get(listInfoAnno.get(last_pos).getIndex()));
                        Log.e("Tag_Last_Pos", "[STOP] - Last position: "+last_pos+" Title: ["+ annotationList.get(listInfoAnno.get(last_pos).getIndex()).getAnnotationTitle()+"]");
                        //annotActive.remove(annotActive.lastIndexOf(listInfoAnno.get(last_pos).getIndex()));
                    }
                }
                last_pos++;
            }
            last_pos = 0;

        } else {

        }

        if ( !isAnnotListEmpty && (listInfoAnno.size() == 0)){
            resetInfoAnnoList();
            cancelled = false;
            Log.e("RESET_THREAD", "RESET CONTROLLER THREAD" );
        }

        return true;
    }

    // arrondi un long représentant des milliseconde a la seconde inférieur
    public long arronditSeconde(long time) {
        return (time / 1000) * 1000;
    }

    // methode pour lancer les annotation en foncton de leur type
    public void launch(Annotation annotation) {

        Message message = mainHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putSerializable("annotation", annotation);
        message.setData(bundle);
        mainHandler.sendMessage(message);

    }


    // methode override de Runnable pour pouvoir lancer un thread a partir de notre classe
    @Override
    public void run() {
        cancelled = false;
        while (checkTime() && (!cancelled)) {
            synchronized (this) {
                try {
                    this.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // setter de last_pos
    public void setLast_pos(int last_pos) {
        this.last_pos = last_pos;
    }

    // methode pour stopper notre thread
    public void cancel() {
        cancelled = true;
    }


    public void resetInfoAnnoList(){

        int i = 0;
        for (Annotation annotation : annotationList) {
            InfoAnno tmp = new InfoAnno(arronditSeconde(annotation.getAnnotationStartTime()), i, true);
            listInfoAnno.add(tmp);
            i++;
        }
        Collections.sort(listInfoAnno);
    }
}