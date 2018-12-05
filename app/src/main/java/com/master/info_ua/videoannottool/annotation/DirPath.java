package com.master.info_ua.videoannottool.annotation;

public enum DirPath {
    CATEGORIE1("Catégorie 1", "categorie1", false),
    CATEGORIE2("Catégorie 2", "categorie2", false),
    CATEGORIE3("Catégorie 3", "categorie3", false),
    CATEGORIE4("Catégorie 4", "categorie4", false),
    CATEGORIE1_SUB1("Sous-catégorie 1", "categorie1/sous-categorie1", true),
    CATEGORIE1_SUB2("Sous-catégorie 2", "categorie1/sous-categorie2", true),
    CATEGORIE1_SUB3("Sous-catégorie 3", "categorie1/sous-categorie3", true),
    CATEGORIE1_SUB4("Sous-catégorie 4", "categorie1/sous-categorie4", true),
    CATEGORIE2_SUB1("Sous-catégorie 1", "categorie2/sous-categorie1", true),
    CATEGORIE2_SUB2("Sous-catégorie 2", "categorie2/sous-categorie2", true),
    CATEGORIE2_SUB3("Sous-catégorie 3", "categorie2/sous-categorie3", true),
    CATEGORIE2_SUB4("Sous-catégorie 4", "categorie2/sous-categorie4", true),
    CATEGORIE3_SUB1("Sous-catégorie 1", "categorie3/sous-categorie1", true),
    CATEGORIE3_SUB2("Sous-catégorie 2", "categorie3/sous-categorie2", true),
    CATEGORIE3_SUB3("Sous-catégorie 3", "categorie3/sous-categorie3", true),
    CATEGORIE3_SUB4("Sous-catégorie 4", "categorie3/sous-categorie4", true),
    CATEGORIE4_SUB1("Sous-catégorie 1", "categorie4/sous-categorie1", true),
    CATEGORIE4_SUB2("Sous-catégorie 2", "categorie4/sous-categorie2", true),
    CATEGORIE4_SUB3("Sous-catégorie 3", "categorie4/sous-categorie3", true),
    CATEGORIE4_SUB4("Sous-catégorie 4", "categorie4/sous-categorie4", true);

    private final String name;
    private final String path;
    private final boolean isSubDir;

    DirPath(String name, String path, boolean isSubDir) {
        this.name = name;
        this.path = path;
        this.isSubDir = isSubDir;
    }

    public String getPath() {
        return path;
    }

    public boolean isSubDir() {
        return isSubDir;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.path;
    }
}