package com.master.info_ua.videoannottool.dialog;

import android.widget.TextView;

import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.util.Categorie;

public interface DialogCallback {

    void onSaveAnnotation(Annotation annotation);

    void onClickVideoFileImport();

    void updateImportVideoTextView(TextView videoImportTextView);

    void saveImportVideo(Categorie selectedSousCategorie);

}
