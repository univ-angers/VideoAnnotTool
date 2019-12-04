package com.master.info_ua.videoannottool.dialog;

import android.widget.TextView;

import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.util.Categorie;

public interface DialogCallback {
    //Appelée lors d'une sauvegarde d'une annotation
    void onSaveAnnotation(Annotation annotation, boolean checkAnnotPredef);
    //Appelée lors d'un clic sur l'import d'une vidéo
    void onClickVideoFileImport();
    //Appelée lors d'un clic sur l'import d'une vidéo
    void updateImportVideoTextView(TextView videoImportTextView);
    //Appelée lors de la sauvegarde de la vidéo
    void saveImportVideo(Categorie selectedSousCategorie, int difficulte);

    void CopyFileAnnotPredef (Annotation annotation);

    void OnOffBoutons(boolean bonton);
}
