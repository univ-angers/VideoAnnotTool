package com.master.info_ua.videoannottool.annotation;

public enum DirPath {
    CATEGORIE1("categorie1"),
    CATEGORIE2("categorie2"),
    CATEGORIE3("categorie3"),
    CATEGORIE4("categorie4"),
    CATEGORIE1_SUB1("categorie1/sous-categorie1"),
    CATEGORIE1_SUB2("categorie1/sous-categorie2"),
    CATEGORIE1_SUB3("categorie1/sous-categorie3"),
    CATEGORIE1_SUB4("categorie1/sous-categorie4"),
    CATEGORIE2_SUB1("categorie2/sous-categorie1"),
    CATEGORIE2_SUB2("categorie2/sous-categorie2"),
    CATEGORIE2_SUB3("categorie2/sous-categorie3"),
    CATEGORIE2_SUB4("categorie2/sous-categorie4"),
    CATEGORIE3_SUB1("categorie3/sous-categorie1"),
    CATEGORIE3_SUB2("categorie3/sous-categorie2"),
    CATEGORIE3_SUB3("categorie3/sous-categorie3"),
    CATEGORIE3_SUB4("categorie3/sous-categorie4"),
    CATEGORIE4_SUB1("categorie4/sous-categorie1"),
    CATEGORIE4_SUB2("categorie4/sous-categorie2"),
    CATEGORIE4_SUB3("categorie4/sous-categorie3"),
    CATEGORIE4_SUB4("categorie4/sous-categorie4");

    private final String value;

    DirPath(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}