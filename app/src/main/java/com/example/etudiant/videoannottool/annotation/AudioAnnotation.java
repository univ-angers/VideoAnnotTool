package com.example.etudiant.videoannottool.annotation;

import java.util.Date;

public class AudioAnnotation extends Annotation{

    private String annotationFileName;

    public AudioAnnotation() {
        super();
    }

    public AudioAnnotation(String annotationTitle, Date annotationDate, int annotationStartTime, int annotationDuration, String annotationFileName) {
        super(annotationTitle, annotationDate, annotationStartTime, annotationDuration);
        this.annotationFileName = annotationFileName;
    }

    public String getAnnotationFileName() {
        return annotationFileName;
    }

    public void setAnnotationFileName(String annotationFileName) {
        this.annotationFileName = annotationFileName;
    }
}
