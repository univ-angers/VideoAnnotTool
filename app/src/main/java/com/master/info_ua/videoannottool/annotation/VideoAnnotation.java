package com.master.info_ua.videoannottool.annotation;

import java.util.ArrayList;
import java.util.List;

public class VideoAnnotation {

    private String creationDate;
    private String lastModified;

    //Une liste d'annotation différente pour chaque vidéo
    private List<Annotation> annotationList;

    public VideoAnnotation(String creationDate, String lastModified, List<Annotation> annotationList) {
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.annotationList = annotationList;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public List<Annotation> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<Annotation> annotationList) {
        this.annotationList = annotationList;
    }

}
