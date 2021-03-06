package com.master.info_ua.videoannottool.annotation;

import java.io.Serializable;
import java.util.Date;

public class Annotation implements Serializable {

    //Titre
    private String annotationTitle;
    //Date
    private Date annotationDate;
    //Départ de l'annotation (en seconde?)
    private long annotationStartTime;
    //Durée de l'annotation (en seconde?)
    private long annotationDuration;
    //Type de l'annotation
    private AnnotationType annotationType;
    //Nom du fichier audio
    private String audioFileName;
    //Nom du fichier dédié au dessin
    private String drawFileName;
    //Contenu de l'annotation textuelle
    private String textComment;

    /*
     * Constructeurs
     */
    public Annotation(AnnotationType annotationType) {
        this.annotationType = annotationType;
        this.annotationDate = new Date();
    }

    public Annotation(String annotationTitle, AnnotationType annotationType) {
        this.annotationTitle = annotationTitle;
        this.annotationType = annotationType;
        this.annotationDate = new Date();
    }

    public Annotation(String annotationTitle, Integer annotationStartTime, Integer annotationDuration, AnnotationType annotationType) {
        this.annotationTitle = annotationTitle;
        this.annotationDate = new Date();
        this.annotationStartTime = annotationStartTime;
        this.annotationDuration = annotationDuration;
        this.annotationType = annotationType;
    }

    public Annotation(String annotationTitle, Long annotationStartTime, Integer annotationDuration, AnnotationType annotationType, String audioFileName, String drawFileName, String textComment) {
        this.annotationTitle = annotationTitle;
        this.annotationDate = new Date();
        this.annotationStartTime = annotationStartTime;
        this.annotationDuration = annotationDuration;
        this.annotationType = annotationType;
        this.audioFileName = audioFileName;
        this.drawFileName = drawFileName;
        this.textComment = textComment;
    }

    /*
     * GETTERS & SETTERS
     */
    public String getAnnotationTitle() {
        return annotationTitle;
    }

    public void setAnnotationTitle(String annotationTitle) { this.annotationTitle = annotationTitle; }

    public Date getAnnotationDate() {
        return annotationDate;
    }

    public void setAnnotationDate(Date annotationDate) {
        this.annotationDate = annotationDate;
    }

    public long getAnnotationStartTime() {
        return annotationStartTime;
    }

    public void setAnnotationStartTime(long annotationStartTime) { this.annotationStartTime = annotationStartTime; }

    public long getAnnotationDuration() {
        return annotationDuration;
    }

    public void setAnnotationDuration(long annotationDuration) { this.annotationDuration = annotationDuration; }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) { this.annotationType = annotationType; }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }

    public String getDrawFileName() {
        return drawFileName;
    }

    public void setDrawFileName(String drawFileName) {
        this.drawFileName = drawFileName;
    }

    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }
}
