package com.example.etudiant.videoannottool.annotation;

import java.io.Serializable;
import java.util.Date;

public class VAnnotation implements Serializable {

    private String annotationTitle;
    private Date annotationDate;
    private Integer annotationStartTime;
    private Integer annotationDuration;
    private String annotationType;
    private String audioFileName;
    private String  drawFileName;
    private String textComment;
    private Integer zoomRate;
    private Integer slowMotionSpeed;

    public VAnnotation(String annotationTitle) {
        this.annotationTitle = annotationTitle;
    }

    public VAnnotation(String annotationTitle, Date annotationDate, Integer annotationStartTime, Integer annotationDuration, String annotationType) {
        this.annotationTitle = annotationTitle;
        this.annotationDate = annotationDate;
        this.annotationStartTime = annotationStartTime;
        this.annotationDuration = annotationDuration;
        this.annotationType = annotationType;
    }

    public VAnnotation(String annotationTitle, Date annotationDate, Integer annotationStartTime, Integer annotationDuration, String annotationType, String audioFileName, String drawFileName, String textComment, Integer zoomRate, Integer slowMotionSpeed) {
        this.annotationTitle = annotationTitle;
        this.annotationDate = annotationDate;
        this.annotationStartTime = annotationStartTime;
        this.annotationDuration = annotationDuration;
        this.annotationType = annotationType;
        this.audioFileName = audioFileName;
        this.drawFileName = drawFileName;
        this.textComment = textComment;
        this.zoomRate = zoomRate;
        this.slowMotionSpeed = slowMotionSpeed;
    }

    /*
    * GETTERS && SETTERS
    */
    public String getAnnotationTitle() {
        return annotationTitle;
    }

    public void setAnnotationTitle(String annotationTitle) {
        this.annotationTitle = annotationTitle;
    }

    public Date getAnnotationDate() {
        return annotationDate;
    }

    public void setAnnotationDate(Date annotationDate) {
        this.annotationDate = annotationDate;
    }

    public Integer getAnnotationStartTime() {
        return annotationStartTime;
    }

    public void setAnnotationStartTime(Integer annotationStartTime) {
        this.annotationStartTime = annotationStartTime;
    }

    public Integer getAnnotationDuration() {
        return annotationDuration;
    }

    public void setAnnotationDuration(Integer annotationDuration) {
        this.annotationDuration = annotationDuration;
    }

    public String getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }

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

    public Integer getZoomRate() {
        return zoomRate;
    }

    public void setZoomRate(Integer zoomRate) {
        this.zoomRate = zoomRate;
    }

    public Integer getSlowMotionSpeed() {
        return slowMotionSpeed;
    }

    public void setSlowMotionSpeed(Integer slowMotionSpeed) {
        this.slowMotionSpeed = slowMotionSpeed;
    }


    public enum AnnotationType{
        TEXT("texte"),
        AUDIO("audio"),
        DRAW("graphique"),
        ZOOM("zoom"),
        SLOWMOTION("ralenti");


        private final String value;

        AnnotationType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
