package com.master.info_ua.videoannottool.custom;

import com.master.info_ua.videoannottool.annotation.VideoAnnotation;

public class Video {

    //Nom du fichier vidéo qui permet de le retrouver dans le stockage
    private String fileName;
    //Chemin du fichier vidéo
    private String path;
    //Instance de VideoAnnotation
    private VideoAnnotation videoAnnotation;
    //Nom de la vidéo dans la listview
    private String name;
    /*
     * Constructeurs
     */
    public Video() {
    }

    public Video(String fileName, String path, VideoAnnotation videoAnnotation) {
        this.fileName = fileName;
        this.path = path;
        this.videoAnnotation = videoAnnotation;
        this.name = fileName;
    }

    /*
     * Getters & setters
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
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
}
