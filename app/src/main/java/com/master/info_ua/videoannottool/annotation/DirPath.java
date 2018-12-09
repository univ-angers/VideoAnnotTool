package com.master.info_ua.videoannottool.annotation;

public enum DirPath {
    CATEGORIE1("SOLO", "solo", false),
    CATEGORIE2("DUO", "duo", false),
    CATEGORIE3("TRIO", "trio", false),
    CATEGORIE4("QUATUOR", "quatuor", false),
    CATEGORIE1_SUB1("Souplesse", "solo/souplesse", true),
    CATEGORIE1_SUB2("Maintien", "solo/maintien", true),
    CATEGORIE1_SUB3("Agilité", "solo/agilite", true),
    CATEGORIE1_SUB4("Dynamique", "solo/dynamique", true),
    CATEGORIE2_SUB1("Statiques positions variées", "duo/statiques-variees", true),
    CATEGORIE2_SUB2("Statiques ATR", "duo/statiques-atr", true),
    CATEGORIE2_SUB3("Dynamiques rattrapes", "duo/dynamique-rattrappes", true),
    CATEGORIE2_SUB4("Dynamiques sorties", "duo/dynamique-sorties", true),
    CATEGORIE3_SUB1("Statiques positions variées", "trio/statiques-variees", true),
    CATEGORIE3_SUB2("Statiques ATR", "trio/statiques-atr", true),
    CATEGORIE3_SUB3("Dynamiques rattrapes", "trio/dynamique-rattrappes", true),
    CATEGORIE3_SUB4("Dynamiques sorties", "trio/dynamique-sorties", true),
    CATEGORIE4_SUB1("Statiques positions variées", "quatuor/statiques-variees", true),
    CATEGORIE4_SUB2("Statiques ATR", "quatuor/statiques-atr", true),
    CATEGORIE4_SUB3("Dynamiques rattrapes", "quatuor/dynamique-rattrappes", true),
    CATEGORIE4_SUB4("Dynamiques sorties", "quatuor/dynamique-sorties", true);

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