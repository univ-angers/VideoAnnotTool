package com.example.etudiant.videoannottool.annotation;

import java.util.Date;

public class Annotation {

    private String annotationTitle;
    private Date annotationDate;
    private int annotationStartTime;
    private int annotationDuration;

    public Annotation() {

    }

    public Annotation(String annotationTitle, Date annotationDate, int annotationStartTime, int annotationDuration) {
        this.annotationTitle = annotationTitle;
        this.annotationDate = annotationDate;
        this.annotationStartTime = annotationStartTime;
        this.annotationDuration = annotationDuration;
    }

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

    public int getAnnotationStartTime() {
        return annotationStartTime;
    }

    public void setAnnotationStartTime(int annotationStartTime) {
        this.annotationStartTime = annotationStartTime;
    }

    public int getAnnotationDuration() {
        return annotationDuration;
    }

    public void setAnnotationDuration(int annotationDuration) {
        this.annotationDuration = annotationDuration;
    }
}
