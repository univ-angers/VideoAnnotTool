package com.example.etudiant.videoannottool.annotation;

import com.example.etudiant.videoannottool.annotation.Annotation;

import java.util.ArrayList;

public class Video{

    private String fileName;
    private String path;
    private VideoAnnotation videoAnnotation;

    public Video() {
    }

    public Video(String fileName, String path, VideoAnnotation videoAnnotation) {
        this.fileName = fileName;
        this.path = path;
        this.videoAnnotation = videoAnnotation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public VideoAnnotation getVideoAnnotation() {
        return videoAnnotation;
    }

    public void setVideoAnnotation(VideoAnnotation videoAnnotation) {
        this.videoAnnotation = videoAnnotation;
    }

    //protected ArrayList<Annotation> annotations;

    /*public Video(String name,String author,ArrayList<Annotation> annotations){
        this.name=name;
        this.author=author;
        this.annotations=annotations;
    }

    public String getName(){
        return this.name;
    }
    public String getAuthor(){
        return this.author;
    }
    public ArrayList<Annotation> getAnnotations(){ return  this.annotations;}*/
}
