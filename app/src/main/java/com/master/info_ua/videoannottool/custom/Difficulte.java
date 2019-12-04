package com.master.info_ua.videoannottool.custom;


import java.util.ArrayList;

//Classe gérant l'association d'un niveau de difficulté à une vidéo
public class Difficulte {
    private ArrayList<Association> associations;

    public Difficulte() {
        this.associations = new ArrayList<>();
    }

    public void editVideoName(final String videoName, String new_videoName) {
        for (Association a : associations) {
            if (a.videoName == videoName) {
                a.setVideoName(new_videoName);
                break;
            }
        }
    }

    public void editDifficulte(final String videoName, String new_difficulte) {
        for (Association a : associations) {
            if (a.videoName == videoName) {
                a.setDifficulte(new_difficulte);
                break;
            }
        }
    }

    public int getDifficulte(final String videoName) {
        for (Association a : associations) {
            if (a.videoName == videoName) {
                return Integer.parseInt(a.difficulte);
            }
        }
        return 0;
    }


    public void add(String videoName, String difficulte) {
        Association new_a = new Association(videoName, difficulte);
        this.associations.add(new_a);
    }

    public void remove(final String videoName) {
        for (Association a : associations) {
            if (a.videoName == videoName) {
                associations.remove(a);
                break;
            }
        }
    }

    public static class Association {
        private String videoName;
        private String difficulte;

        public Association() {
        }

        public Association(String vn, String d) {
            this.videoName = vn;
            this.difficulte = d;
        }

        public void setVideoName(String v) {
            this.videoName = v;
        }

        public void setDifficulte(String d) {
            this.difficulte = d;
        }
    }

}
