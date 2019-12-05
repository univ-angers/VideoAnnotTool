package com.master.info_ua.videoannottool.custom;
import java.util.ArrayList;

//Classe gérant l'association d'un niveau de difficulté à une vidéo
public class Difficulte {
    private ArrayList<Association> associations;

    public Difficulte() {
        this.associations = new ArrayList<>();
    }

    public Difficulte(String jsonFileContent) {
        this.associations = new ArrayList<>();
        String[] contentSplit = jsonFileContent.split("\n") ;
        String new_video_name;
        String new_difficulte;
        for (String association : contentSplit) {
            if ( association.matches("name") && association.matches("difficulte") ) {
                //On ne prend que les associations qui matchent les informations importantes : nom de la vidéo et niveau de difficulte.
                //Les autres sont les accolades ouvrantes et fermantes de notre liste
                String[] parties = association.split(",");
                //parties[1] est de la forme  : { "name":"videoName"
                new_video_name = parties[1].substring(9,parties[1].length()-1);
                //parties[2] est de la forme : "difficulte":"niveau"}
                new_difficulte = parties[2].substring(14,parties[2].length()-1);
                this.add(new_video_name,new_difficulte);
            }
        }
    }

    public ArrayList<Association> getAssociations() {
        return associations;
    }


    public void editVideoName(final String videoName, String new_videoName) {
        for (Association a : associations) {
            if (a.videoName.equals(videoName)) {
                a.setVideoName(new_videoName);
                break;
            }
        }
    }

    public void editDifficulte(final String videoName, String new_difficulte) {
        for (Association a : associations) {
            if (a.videoName.equals(videoName)) {
                a.setDifficulte(new_difficulte);
                break;
            }
        }
    }

    public int getDifficulte(final String videoName) {
        for (Association a : associations) {
            if (a.videoName.equals(videoName)) {
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
            if (a.videoName.equals(videoName)) {
                associations.remove(a);
                break;
            }
        }
    }

    public String toJson() {
        String res = "[\n";
        int iter=0;
        for (Association a : associations) {

            res += "{\"name\":\"" + a.getVideoName() + "\",\"difficulte\":\""+a.getDifficulte() +"\"}";

            if (iter<associations.size()-1){
                res+=",\n";
            }
            else if (iter==associations.size()-1) {
                //Si on est dans la dernière association, on n'ajoute pas de virgule.
                res+="\n";
            }
            iter++;
        }
        res += "]";
        return res;
    }


    public void addAll(Difficulte difficulte){
        if(difficulte != null && difficulte.associations != null)
        this.associations.addAll(difficulte.associations);
    }

    public static class Association {
        private String videoName;
        private String difficulte;

        public Association(String vn, String d) {
            this.videoName = vn;
            this.difficulte = d;
        }


        public String getVideoName() {
            return videoName;
        }

        public String getDifficulte() {
            return difficulte;
        }

        public void setVideoName(String v) {
            this.videoName = v;
        }

        public void setDifficulte(String d) {
            this.difficulte = d;
        }

    }

}
