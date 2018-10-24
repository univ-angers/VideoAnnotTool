package com.example.etudiant.videoannottool.annotation;


import java.util.Date;

public class TextAnnotation extends Annotation {

    private String annotationComment;

    public TextAnnotation() {
        super();
    }

    public TextAnnotation(String annotationTitle, Date annotationDate, int annotationStartTime, int annotationDuration, String annotationComment) {
        super(annotationTitle, annotationDate, annotationStartTime, annotationDuration);
        this.annotationComment = annotationComment;
    }

    public String getAnnotationText() {
        return annotationComment;
    }

    public void setAnnotationText(String annotationText) {
        this.annotationComment = annotationText;
    }
}
