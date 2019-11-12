package com.master.info_ua.videoannottool.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

// Les catégories ont pour but de classer les vidéos en fonction de leur contenu
public class Categorie implements Parcelable {
    //Nom de la catégorie
    private String name;
    //Ancien nom en cas de modification du nom de la catégorie
    private String oldname;
    //Nom de la catégorie mère
    private String parentName;

    //Chemin de la catégorie
    private String path;
    //List des sous-catégories
    private List<Categorie> subCategories;

    /*
     * Constructeurs
     */

    public Categorie(String name, String parentName, String path) {
        this.name = name;
        this.oldname= name;
        this.parentName = parentName;
        this.path = path;
        this.subCategories=new ArrayList<>();
    }

    protected Categorie(Parcel in) {
        this.getFromParcel(in);
    }

    public static final Parcelable.Creator<Categorie> CREATOR = new Parcelable.Creator<Categorie>() {
        @Override
        public Categorie createFromParcel(Parcel in) {
            return new Categorie(in);
        }

        @Override
        public Categorie[] newArray(int size) {
            return new Categorie[size];
        }
    };

    /*
     * Getters & setters
     */
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getOldName(){ return oldname; }

    private void setOldName(String old) { oldname=old; }

    public String getParentName() { return parentName; }

    public void setParentName(String parentName) { this.parentName = parentName; }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    public List<Categorie> getSubCategories() { return subCategories; }

    public void setSubCategories(List<Categorie> subCategories) { this.subCategories = subCategories; }

    public void removeSubCategorie(Categorie categorie) { this.subCategories.remove(categorie); }


    @Override
    public String toString() { return this.name; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(oldname);
        dest.writeString(parentName);
        dest.writeString(path);
        dest.writeList(subCategories);
    }

    public void getFromParcel(Parcel in){
        this.setName(in.readString());
        this.setOldName(in.readString());
        this.setParentName(in.readString());
        this.setPath(in.readString());
        this.subCategories = new ArrayList<Categorie>();
        in.readList(this.subCategories, Categorie.class.getClassLoader());
    }
}
