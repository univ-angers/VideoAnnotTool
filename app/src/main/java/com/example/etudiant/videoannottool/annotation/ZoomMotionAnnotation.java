package com.example.etudiant.videoannottool.annotation;

import java.util.Date;

public class ZoomMotionAnnotation extends Annotation{

    private int annotationZoomRate;

    public ZoomMotionAnnotation(){
        super();
    }

    public ZoomMotionAnnotation(String annotationTitle, Date annotationDate, int annotationStartTime, int annotationDuration, int annotationZoomRate) {
        super(annotationTitle, annotationDate, annotationStartTime, annotationDuration);
        this.annotationZoomRate = annotationZoomRate;
    }

    public int getAnnotationZoomRate() {
        return annotationZoomRate;
    }

    public void setAnnotationZoomRate(int annotationZoomRate) {
        this.annotationZoomRate = annotationZoomRate;
    }
}
