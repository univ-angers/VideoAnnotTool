package com.example.etudiant.videoannottool.annotation;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.etudiant.videoannottool.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
