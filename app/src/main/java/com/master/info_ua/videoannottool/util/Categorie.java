package com.master.info_ua.videoannottool.util;

import java.util.ArrayList;
import java.util.List;

// Les catégories ont pour but de classer les vidéos en fonction de leur contenu
public class Categorie {
    //Nom de la catégorie
    private String name;
    //Nom de la catégorie mère
    private String parentName;
    //Chemin de la catégorie
    private String path;
    //List des sous-catégories
    private List<Categorie> subCategories;

    /*
     * Constructeurs
     */
    public Categorie(String name, String path) {
        this.name = name;
        this.path = path;
        subCategories = new ArrayList<>();
    }

    public Categorie(String name, String parentName, String path) {
        this.name = name;
        this.parentName = parentName;
        this.path = path;

        subCategories = new ArrayList<>();
    }

    /*
     * Getters & setters
     */
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getParentName() { return parentName; }

    public void setParentName(String parentName) { this.parentName = parentName; }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    public List<Categorie> getSubCategories() { return subCategories; }

    public void setSubCategories(List<Categorie> subCategories) { this.subCategories = subCategories; }

    @Override
    public String toString() { return this.name; }
}
