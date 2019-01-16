package com.master.info_ua.videoannottool.util;

import com.master.info_ua.videoannottool.annotation.Annotation;

import java.util.Comparator;

/**
 * <p>
 *     Classe utilitaire pour la comparaison de deux objets 'Annotation'
 *
 *     La comparaison porte sur la propriété 'annotationStartTime'.
 *
 *     Utilisé pour ordonner les annotations d'une vidéo par ordre croissant du 'annotationStartTime'
 * </p>
 *
 */
public class AnnotationComparator implements Comparator<Annotation> {

    @Override
    public int compare(Annotation o1, Annotation o2) {
        return (int) (o1.getAnnotationStartTime() - o2.getAnnotationStartTime());
    }
}
