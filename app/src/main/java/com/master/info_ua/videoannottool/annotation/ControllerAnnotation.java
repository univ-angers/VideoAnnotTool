package com.master.info_ua.videoannottool.annotation;

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
    // L'interface de notre player pour avoir accès aux méthodes de ce dernier
    private Ecouteur m_ecouteur;
    // File d'exécution contenant des objet indiquant le temps, la position et si il faut lancer ou arrêter l'annotation
    private ArrayList<InfoAnno> listInfoAnno = new ArrayList<>();
    // Liste des annotations constituée à partir de celle de VideoAnnotation
    private List<Annotation> annotationList;
    // Index sur la file permettant de parcourir la file là où nous en étions rendu
    private int last_pos = 0;
    // Si True : arrêt soft du thread, nécessaire en android
    private boolean cancelled = false;
    // Détermine si la liste d'annotation est vide ou non
    private boolean isAnnotListEmpty = false;
    // Permet d'envoyer des tâches à exécuter dans MainActivity
    private Handler mainHandler;
    // VideoAnnotation
    private VideoAnnotation videoAnnotation;

    // Constructeur
    public ControllerAnnotation(Ecouteur m_e, VideoAnnotation vAnnotation, Handler _mainHandler) {
        this.mainHandler = _mainHandler;
        this.m_ecouteur = m_e;
        this.videoAnnotation = vAnnotation;
        this.annotationList = new ArrayList<>();

        //Si la vidéo est différente de null
        if (videoAnnotation != null) {
            //Récupération de la liste d'annotations
            annotationList = videoAnnotation.getAnnotationList();
            //Si cette liste est vide
            if(annotationList.size() < 1){
                //On la déclare vide
                isAnnotListEmpty = true;
                //Le Thread doit s'arrêter
                cancelled = true;
                // Définit la position de l'annotation dans la liste
                int i = 0;
                //Pour chacune des annotations de la liste
                for (Annotation annotation : annotationList) {
                    //Initialise une InfoAnno
                    InfoAnno tmp = new InfoAnno(annotation.getAnnotationStartTime(), i, true);
//                    InfoAnno tmp = new InfoAnno(arronditDeciSeconde(annotation.getAnnotationStartTime()), i, true);
                    //Ajout à la liste d'InfoAnno
                    listInfoAnno.add(tmp);
                    i++;
                }
                //Tri des InfoAnno
                Collections.sort(listInfoAnno);
            }
        } else {
            //Si la vidéo n'existe pas, on déclare la liste d'annotations à vide et on signale que le Thread doit s'arrêter
            isAnnotListEmpty = true;
            cancelled = true;
        }
    }

    /*
     * Getters & setters
     */
    public VideoAnnotation getVideoAnnotation() {
        return videoAnnotation;
    }

    public void setVideoAnnotation(VideoAnnotation videoAnnotation) { this.videoAnnotation = videoAnnotation; }

    public void setLast_pos(int last_pos) {
        this.last_pos = last_pos;
    }

    public ArrayList<InfoAnno> getListInfoAnno() {
        return listInfoAnno;
    }

    public void setListInfoAnno(ArrayList<InfoAnno> listInfoAnno) { this.listInfoAnno = listInfoAnno; }

    // Vérifie s'il existe des annotations qui n'ont pas été lancées depuis le dernier appel et positionne le curseur sur le prochain élément à vérifier
    // Stoppe aussi les annotations qui doivent se terminer
    public boolean checkTime() {
        //Vérifie le temps et lance l'annotation avec le paramétre correspondant
        if (m_ecouteur.getPlayer().getDuration() >= m_ecouteur.getVideoCurrentPosition()) {
            //Si la position dans la liste de la dernière annotation lancée est inférieure à la taille de la liste
            if (last_pos < listInfoAnno.size()) { // ==> Switch while() to if()
                Log.e("Pos_VS_Size", "Last_Pos ["+last_pos+"] Size ["+listInfoAnno.size()+"]");
                //Si la valeur du curseur (arrondie à la seconde inférieure) est égale à la date de lancement de l'annotation (seconde inférieure également)
                if (arronditDeciSeconde(m_ecouteur.getVideoCurrentPosition()) == arronditDeciSeconde(listInfoAnno.get(last_pos).getTime())) {
//                if (arronditDeciSeconde(m_ecouteur.getVideoCurrentPosition()) == listInfoAnno.get(last_pos).getTime()) {
                    //Si l'annotation est en cours de diffusion
                    if (listInfoAnno.get(last_pos).isDebut()) {
                        //Lance l'annotation
                        launch(annotationList.get(listInfoAnno.get(last_pos).getIndex()));
                        Log.e("Tag_Last_Pos", "Last position: "+last_pos+" Title: ["+ annotationList.get(listInfoAnno.get(last_pos).getIndex()).getAnnotationTitle()+"]");
                        //Supprime l'élément InfoAnno dans la liste d'InfoAnno
                        listInfoAnno.remove(last_pos);
                    }
                }
                //Incrémente l'index de la dernière annotation diffusée
                last_pos++;
            }
            //Si toutes les annotations ont été diffusées, on réinitialise l'index de la dernière annotation diffusée à 0
            last_pos = 0;
        }
        // Si les listes d'annotations et d'InfoAnno ne sont pas vides
        if ( !isAnnotListEmpty && (listInfoAnno.size() == 0)){
            //Réinitialise la liste d'InfoAnno
            resetInfoAnnoList();
            //Réinitialise le Thread
            cancelled = false;
            Log.e("RESET_THREAD", "RESET CONTROLLER THREAD" );
        }
        //Retourne toujours vrai??
        return true;
    }

    // Arrondit un long représentant des millisecondes à la seconde inférieure
    public long arronditDeciSeconde(long time) {
        return (time / 100) * 100;
    }

    // Lance les annotations en fonction de leur type
    public void launch(Annotation annotation) {
        Message message = mainHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putSerializable("annotation", annotation);
        message.setData(bundle);
        mainHandler.sendMessage(message);
    }

    // Lance un thread
    @Override
    public void run() {
        cancelled = false;
        while (checkTime() && (!cancelled)) {
            synchronized (this) {
                try {
                    this.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Stoppe le thread
    public void cancel() {
        cancelled = true;
    }

    //Réinitialise la liste d'annotations
    public void resetInfoAnnoList(){
        annotationList = null;
        //Récupère la liste d'annotations à partir de la vidéo
        annotationList = videoAnnotation.getAnnotationList();
        int i = 0;
        //Pour chacune de ces annotations
        for (Annotation annotation : annotationList) {
            //Initialisation de l'InfoAnno correspondante
            InfoAnno tmp = new InfoAnno(annotation.getAnnotationStartTime(), i, true);
//            InfoAnno tmp = new InfoAnno(arronditDeciSeconde(annotation.getAnnotationStartTime()), i, true);
            //Ajout de l'InfoAnno à la liste
            listInfoAnno.add(tmp);
            i++;
        }
        //Tri de la liste d'InfoAnno
        Collections.sort(listInfoAnno);
    }
}