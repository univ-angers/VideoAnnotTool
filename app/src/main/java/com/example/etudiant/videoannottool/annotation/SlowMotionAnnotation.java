package com.example.etudiant.videoannottool.annotation;

import java.util.Date;

public class SlowMotionAnnotation extends Annotation{

    private int annotationSlowSpeed;

    public SlowMotionAnnotation(){
        super();
    }

    public SlowMotionAnnotation(String annotationTitle, Date annotationDate, int annotationStartTime, int annotationDuration, int annotationSlowSpeed) {
        super(annotationTitle, annotationDate, annotationStartTime, annotationDuration);
        this.annotationSlowSpeed = annotationSlowSpeed;
    }

    public int getAnnotationSlowSpeed() {
        return annotationSlowSpeed;
    }

    public void setAnnotationSlowSpeed(int annotationSlowSpeed) {
        this.annotationSlowSpeed = annotationSlowSpeed;
    }
}
