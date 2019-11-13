package com.master.info_ua.videoannottool.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.adapter.AnnotationsAdapter;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.AnnotationType;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.master.info_ua.videoannottool.dialog.DialogCallback;
import com.master.info_ua.videoannottool.dialog.DialogEditAnnot;
import com.master.info_ua.videoannottool.dialog.DialogEditAnnotPredef;
import com.master.info_ua.videoannottool.util.Categorie;

import java.util.ArrayList;


public class Fragment_AnnotPredef extends Fragment implements DialogEditAnnotPredef.EditAnnotDialogListener {

    private AnnotationsAdapter annotationsAdapter;
    private ListView lv_Annotations_predef;
    private Button Cancel_btn;

    private AnnotFragmentListener mListener;

    // Listes de toutes les annotations prédéfinies
    private ArrayList<Annotation> ListAnnotationsPredef;

    private DialogCallback ContextMain;


    public Fragment_AnnotPredef(ArrayList<Annotation> LAnnotPredef, DialogCallback contextMain ){
        // Required empty public constructor
        this.ListAnnotationsPredef = LAnnotPredef;
        if (contextMain instanceof DialogCallback) {
            this.ContextMain = (DialogCallback) contextMain;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_annot_predef, container, false);
        lv_Annotations_predef = (ListView)view.findViewById(R.id.lv_annotations_predef);
        Cancel_btn = (Button)view.findViewById(R.id.button_annot_fragment);

        annotationsAdapter = new AnnotationsAdapter(getActivity(),ListAnnotationsPredef);

        lv_Annotations_predef.setAdapter(annotationsAdapter);


        lv_Annotations_predef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Annotation annotation = (Annotation) lv_Annotations_predef.getItemAtPosition(position);
                mListener.onAnnotPredefItemClick(annotation);
                onShowAnnotDialog(annotation);

            }
        });

        Cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.closeAnnotPredef();
                System.out.println("                    CLICK");
            }
        });

        return view;
    }

    protected void onShowAnnotDialog(Annotation annot){
        DialogEditAnnotPredef mon_dialogue = new DialogEditAnnotPredef(this,annot);
        mon_dialogue.showDialogEdit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AnnotFragmentListener) {
            mListener = (AnnotFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet Fragment_AnnotPredef.AnnotFragmentListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AnnotFragmentListener) {
            mListener = (AnnotFragmentListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet Fragment_AnnotPredef.AnnotFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveEditAnnot(Annotation annot, String title, int duree) {
        Annotation annotCourant = new Annotation(title, annot.getAnnotationStartTime(),duree, annot.getAnnotationType(), annot.getAudioFileName(), annot.getDrawFileName(), annot.getTextComment());
        this.ContextMain.onSaveAnnotation(annotCourant,false);
        this.ContextMain.CopyFileAnnotPredef(annot);

    }


    public ArrayList<Annotation> getListAnnotationsPredef() {
        return ListAnnotationsPredef;
    }

    public void setListAnnotationsPredef(ArrayList<Annotation> listAnnotationsPredef) {
        ListAnnotationsPredef = listAnnotationsPredef;
    }


    public interface AnnotFragmentListener {

        void onAnnotPredefItemClick(Annotation annotation);

        void closeAnnotPredef();

    }
}
