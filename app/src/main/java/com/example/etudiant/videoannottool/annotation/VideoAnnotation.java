package com.example.etudiant.videoannottool.annotation;

import java.util.Date;
import java.util.List;

public class VideoAnnotation{
    private Date creationDate;
    private Date lastModified;
    private List<Annotation> annotationList;



    public VideoAnnotation() {

    }

    public VideoAnnotation(Date creationDate, Date lastModified, List<Annotation> annotationList) {
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

    public List<Annotation> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<Annotation> annotationList) {
        this.annotationList = annotationList;
    }

}
