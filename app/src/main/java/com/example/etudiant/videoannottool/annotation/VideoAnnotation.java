package com.example.etudiant.videoannottool.annotation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoAnnotation{
    private Date creationDate;
    private Date lastModified;
    private List<VAnnotation> annotationList;
    /*
    private List<TextAnnotation> textAnnotationArrayList;
    private List<AudioAnnotation> audioAnnotationArrayList;
    private List<DrawAnnotation> drawAnnotationArrayList;
    private List<SlowMotionAnnotation> slowMotionAnnotationArrayList;
    private List<ZoomMotionAnnotation> zoomMotionAnnotationArrayList;


    public VideoAnnotation(Date creationDate, Date lastModified, ArrayList<TextAnnotation> textAnnotationArrayList, ArrayList<AudioAnnotation> audioAnnotationArrayList, ArrayList<DrawAnnotation> drawAnnotationArrayList, ArrayList<SlowMotionAnnotation> slowMotionAnnotationArrayList, ArrayList<ZoomMotionAnnotation> zoomMotionAnnotationArrayList) {
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.textAnnotationArrayList = textAnnotationArrayList;
        this.audioAnnotationArrayList = audioAnnotationArrayList;
        this.drawAnnotationArrayList = drawAnnotationArrayList;
        this.slowMotionAnnotationArrayList = slowMotionAnnotationArrayList;
        this.zoomMotionAnnotationArrayList = zoomMotionAnnotationArrayList;
    }
    */


    public VideoAnnotation() {

    }

    public VideoAnnotation(Date creationDate, Date lastModified, List<VAnnotation> annotationList) {
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.annotationList = annotationList;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public List<VAnnotation> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<VAnnotation> annotationList) {
        this.annotationList = annotationList;
    }

/*
    public List<TextAnnotation> getTextAnnotationArrayList() {
        return textAnnotationArrayList;
    }

    public void setTextAnnotationArrayList(ArrayList<TextAnnotation> textAnnotationArrayList) {
        this.textAnnotationArrayList = textAnnotationArrayList;
    }

    public List<AudioAnnotation> getAudioAnnotationArrayList() {
        return audioAnnotationArrayList;
    }

    public void setAudioAnnotationArrayList(ArrayList<AudioAnnotation> audioAnnotationArrayList) {
        this.audioAnnotationArrayList = audioAnnotationArrayList;
    }

    public List<DrawAnnotation> getDrawAnnotationArrayList() {
        return drawAnnotationArrayList;
    }

    public void setDrawAnnotationArrayList(ArrayList<DrawAnnotation> drawAnnotationArrayList) {
        this.drawAnnotationArrayList = drawAnnotationArrayList;
    }

    public List<SlowMotionAnnotation> getSlowMotionAnnotationArrayList() {
        return slowMotionAnnotationArrayList;
    }

    public void setSlowMotionAnnotationArrayList(ArrayList<SlowMotionAnnotation> slowMotionAnnotationArrayList) {
        this.slowMotionAnnotationArrayList = slowMotionAnnotationArrayList;
    }

    public List<ZoomMotionAnnotation> getZoomMotionAnnotationArrayList() {
        return zoomMotionAnnotationArrayList;
    }

    public void setZoomMotionAnnotationArrayList(ArrayList<ZoomMotionAnnotation> zoomMotionAnnotationArrayList) {
        this.zoomMotionAnnotationArrayList = zoomMotionAnnotationArrayList;
    }
    */
}
