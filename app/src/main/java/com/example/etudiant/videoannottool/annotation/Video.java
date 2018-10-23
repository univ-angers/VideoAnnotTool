package com.example.etudiant.videoannottool.annotation;

import com.example.etudiant.videoannottool.annotation.Annotation;

import java.util.ArrayList;

public class Video{
    private String name;
    private String author;
    protected ArrayList<Annotation> annotations;

    public Video(String name,String author,ArrayList<Annotation> annotations){
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
    public ArrayList<Annotation> getAnnotations(){ return  this.annotations;}
}
