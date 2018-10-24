package com.example.etudiant.videoannottool.annotation;

import java.util.ArrayList;
import java.util.Date;

public class VideoAnnotation{
    private Date creationDate;
    private Date lastModified;
    private ArrayList<TextAnnotation> textAnnotationArrayList;
    private ArrayList<AudioAnnotation> audioAnnotationArrayList;
    private ArrayList<DrawAnnotation> drawAnnotationArrayList;
    private ArrayList<SlowMotionAnnotation> slowMotionAnnotationArrayList;
    private ArrayList<ZoomMotionAnnotation> zoomMotionAnnotationArrayList;

    public VideoAnnotation(Date creationDate, Date lastModified, ArrayList<TextAnnotation> textAnnotationArrayList, ArrayList<AudioAnnotation> audioAnnotationArrayList, ArrayList<DrawAnnotation> drawAnnotationArrayList, ArrayList<SlowMotionAnnotation> slowMotionAnnotationArrayList, ArrayList<ZoomMotionAnnotation> zoomMotionAnnotationArrayList) {
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.textAnnotationArrayList = textAnnotationArrayList;
        this.audioAnnotationArrayList = audioAnnotationArrayList;
        this.drawAnnotationArrayList = drawAnnotationArrayList;
        this.slowMotionAnnotationArrayList = slowMotionAnnotationArrayList;
        this.zoomMotionAnnotationArrayList = zoomMotionAnnotationArrayList;
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

    public ArrayList<TextAnnotation> getTextAnnotationArrayList() {
        return textAnnotationArrayList;
    }

    public void setTextAnnotationArrayList(ArrayList<TextAnnotation> textAnnotationArrayList) {
        this.textAnnotationArrayList = textAnnotationArrayList;
    }

    public ArrayList<AudioAnnotation> getAudioAnnotationArrayList() {
        return audioAnnotationArrayList;
    }

    public void setAudioAnnotationArrayList(ArrayList<AudioAnnotation> audioAnnotationArrayList) {
        this.audioAnnotationArrayList = audioAnnotationArrayList;
    }

    public ArrayList<DrawAnnotation> getDrawAnnotationArrayList() {
        return drawAnnotationArrayList;
    }

    public void setDrawAnnotationArrayList(ArrayList<DrawAnnotation> drawAnnotationArrayList) {
        this.drawAnnotationArrayList = drawAnnotationArrayList;
    }

    public ArrayList<SlowMotionAnnotation> getSlowMotionAnnotationArrayList() {
        return slowMotionAnnotationArrayList;
    }

    public void setSlowMotionAnnotationArrayList(ArrayList<SlowMotionAnnotation> slowMotionAnnotationArrayList) {
        this.slowMotionAnnotationArrayList = slowMotionAnnotationArrayList;
    }

    public ArrayList<ZoomMotionAnnotation> getZoomMotionAnnotationArrayList() {
        return zoomMotionAnnotationArrayList;
    }

    public void setZoomMotionAnnotationArrayList(ArrayList<ZoomMotionAnnotation> zoomMotionAnnotationArrayList) {
        this.zoomMotionAnnotationArrayList = zoomMotionAnnotationArrayList;
    }
}
