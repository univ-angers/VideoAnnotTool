package com.master.info_ua.videoannottool.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.adapter.AnnotationsAdapter;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.master.info_ua.videoannottool.dialog.DialogEditAnnot;

import java.util.ArrayList;
import java.util.List;

import static com.master.info_ua.videoannottool.MainActivity.COACH;
import static com.master.info_ua.videoannottool.MainActivity.ELEVE;


public class Fragment_annotation extends Fragment  implements DialogEditAnnot.EditAnnotDialogListener {

    public AnnotationsAdapter getAnnotationsAdapter() {
        return annotationsAdapter;
    }

    private AnnotationsAdapter annotationsAdapter;
    private ListView listViewAnnotations;

    private AnnotFragmentListener fragmentListener;

    public AnnotFragmentListener getFragmentListener() {
        return fragmentListener;
    }

    private boolean statut_profil=ELEVE;

    private static final String FRAGMENT_DRAW_TAG = "drawFragment";


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

        if (context instanceof AnnotFragmentListener) {
            fragmentListener = (AnnotFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet Fragment_annotation.AnnotFragmentListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof AnnotFragmentListener) {
            fragmentListener = (AnnotFragmentListener) activity;
        } else {
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
        registerForContextMenu(listViewAnnotations);

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
        if (videoAnnot != null) {
            List<Annotation> annotationList = videoAnnot.getAnnotationList();
            //Collections.sort(annotationList, new AnnotationComparator());
            annotationsAdapter.addAll(annotationList);
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

    // Crée le contextmenu pour les annotations qui utilisera le listener dans le MainActivity
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_annotations && statut_profil == COACH) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            menu.findItem(R.id.edit_item_annot).setVisible(true);
            menu.findItem(R.id.delete_item_annot).setVisible(true);
            menu.findItem(R.id.edit_item_infos_annot).setVisible(true);
            menu.findItem(R.id.edit_item_video).setVisible(false);
            menu.findItem(R.id.delete_item_video).setVisible(false);
        }
    }

    public void setStatut_profil(boolean profil){ statut_profil = profil; }

    @Override
    public void onSaveEditAnnot(Annotation annotation, String title, int duree) {
        annotation.setAnnotationTitle(title);
        annotation.setAnnotationDuration(duree);
        annotationsAdapter.notifyDataSetChanged();
    }

    public interface AnnotFragmentListener {

        void onAnnotItemClick(Annotation annotation);
        void onEditAnnotation(Annotation annotation, int position);
        void onDeleteAnnotation(Annotation annotation);
    }
}
