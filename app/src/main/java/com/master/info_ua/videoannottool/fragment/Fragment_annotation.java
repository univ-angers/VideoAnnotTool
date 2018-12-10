package com.master.info_ua.videoannottool.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.adapter.AnnotationsAdapter;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;

import java.util.ArrayList;


public class Fragment_annotation extends Fragment {

    private AnnotationsAdapter annotationsAdapter;
    private ListView listViewAnnotations;

    private AnnotFragmentListener fragmentListener;


    public Fragment_annotation() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        annotationsAdapter = new AnnotationsAdapter(getActivity(), new ArrayList<Annotation>()); //Initilisatisation de la liste d'annotations (vide)
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AnnotFragmentListener){
            fragmentListener = (AnnotFragmentListener) context;
        }
        else {
            throw new ClassCastException(context.toString()
                    + " must implemenet Fragment_annotation.AnnotFragmentListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof AnnotFragmentListener){
            fragmentListener = (AnnotFragmentListener) activity;
        }
        else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet Fragment_annotation.AnnotFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_annotation, container, false);

        listViewAnnotations = view.findViewById(R.id.lv_annotations);
        listViewAnnotations.setOnItemClickListener(annotationItemClickListener);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        listViewAnnotations.setAdapter(annotationsAdapter);
        listViewAnnotations.setClickable(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        fragmentListener = null;
    }

    public void updateAnnotationList(VideoAnnotation videoAnnot) {

        annotationsAdapter.clear();
        //Mise à jour de la liste
        if (videoAnnot != null){
            annotationsAdapter.addAll(videoAnnot.getAnnotationList());
        }
        annotationsAdapter.notifyDataSetChanged();

    }

    /**
     * Listener pour le clic sur la liste d'annotations
     */
    protected AdapterView.OnItemClickListener annotationItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Annotation annotation = (Annotation) listViewAnnotations.getItemAtPosition(position);
            fragmentListener.onAnnotItemClick(annotation);
        }
    };

    public interface AnnotFragmentListener{

        void onAnnotItemClick(Annotation annotation);
    }
}
