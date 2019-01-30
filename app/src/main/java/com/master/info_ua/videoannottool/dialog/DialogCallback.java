package com.master.info_ua.videoannottool.dialog;

import com.master.info_ua.videoannottool.annotation.Annotation;

public interface DialogCallback {

    void addAudioAnnot(Annotation annotation);

    void addTextAnnot(Annotation annotation);

    void onClickVideoFileImport();

}
