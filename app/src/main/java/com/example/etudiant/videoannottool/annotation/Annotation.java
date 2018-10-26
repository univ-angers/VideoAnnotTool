package com.example.etudiant.videoannottool.annotation;

import java.io.Serializable;
import java.util.Date;

public class Annotation implements Serializable {

    private String annotationTitle;
    private Date annotationDate;
    private Integer annotationStartTime;
    private Integer annotationDuration;
    private AnnotationType annotationType;
    private String audioFileName;
    private String  drawFileName;
    private String textComment;
    private Integer zoomRate;
    private Integer slowMotionSpeed;

    public Annotation(String annotationTitle) {
        this.annotationTitle = annotationTitle;
    }

    public Annotation(String annotationTitle, Date annotationDate, Integer annotationStartTime, Integer annotationDuration, AnnotationType annotationType) {
        this.annotationTitle = annotationTitle;
        this.annotationDate = annotationDate;
        this.annotationStartTime = annotationStartTime;
        this.annotationDuration = annotationDuration;
        this.annotationType = annotationType;
    }

    public Annotation(String annotationTitle, Date annotationDate, Integer annotationStartTime, Integer annotationDuration, AnnotationType annotationType, String audioFileName, String drawFileName, String textComment, Integer zoomRate, Integer slowMotionSpeed) {
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

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) {
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

}
