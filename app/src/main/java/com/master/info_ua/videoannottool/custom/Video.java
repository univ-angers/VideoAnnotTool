package com.master.info_ua.videoannottool.custom;

import com.master.info_ua.videoannottool.annotation.VideoAnnotation;

public class Video {

    //Nom du fichier vidéo
    private String fileName;
    //Chemin du fichier vidéo
    private String path;
    //Instance de VideoAnnotation
    private VideoAnnotation videoAnnotation;

    /*
     * Constructeurs
     */
    public Video() {
    }

    public Video(String fileName, String path, VideoAnnotation videoAnnotation) {
        this.fileName = fileName;
        this.path = path;
        this.videoAnnotation = videoAnnotation;
    }

    /*
     * Getters & setters
     */
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
}
