package com.example.etudiant.videoannottool.annotation;

import java.util.Date;

public class DrawAnnotation extends Annotation{

    private String annotationPictureName;

    public DrawAnnotation() {
        super();
    }

    public DrawAnnotation(String annotationTitle, Date annotationDate, int annotationStartTime, int annotationDuration, String annotationPictureName) {
        super(annotationTitle, annotationDate, annotationStartTime, annotationDuration);
        this.annotationPictureName = annotationPictureName;
    }

    public String getAnnotationPictureName() {
        return annotationPictureName;
    }

    public void setAnnotationPictureName(String annotationPictureName) {
        this.annotationPictureName = annotationPictureName;
    }
}
