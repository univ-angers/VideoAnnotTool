package com.master.info_ua.videoannottool.util;

import java.util.List;

public class Categorie {

    private String name;
    private String parentName;
    private String path;

    private List<Categorie> subCategories;


    public Categorie(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Categorie(String name, String parentName, String path) {
        this.name = name;
        this.parentName = parentName;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Categorie> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Categorie> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
