package com.master.info_ua.videoannottool.fragment;

        import android.app.Activity;
        import android.content.Context;
        import android.net.Uri;
        import android.os.Bundle;
        import android.app.Fragment;
        import android.view.ContextMenu;
        import android.view.LayoutInflater;
        import android.view.MenuInflater;
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

// Class qui gère le fragment des annotations prédéfnies
public class Fragment_AnnotPredef extends Fragment implements DialogEditAnnotPredef.EditAnnotDialogListener {

    private AnnotationsAdapter annotationsAdapter;
    private ListView lv_Annotations_predef;
    private Button Cancel_btn;

    private AnnotFragmentListener mListener;

    // Listes de toutes les annotations prédéfinies
    private ArrayList<Annotation> ListAnnotationsPredef;

    public AnnotationsAdapter getAnnotationsAdapter() {
        return annotationsAdapter;
    }

    public Fragment_AnnotPredef.AnnotFragmentListener getFragmentListener() {
        return mListener;
    }

    private DialogCallback ContextMain;

    //A modifier : utiliser setArguments(bundle)
    public Fragment_AnnotPredef(ArrayList<Annotation> LAnnotPredef, DialogCallback contextMain ){
        // Required empty public constructor
        this.ListAnnotationsPredef = LAnnotPredef;
        if (contextMain instanceof DialogCallback) {
            this.ContextMain = (DialogCallback) contextMain;
        }

    }

    public void updateAnnotationList() {

        annotationsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onStart() {
        super.onStart();

        lv_Annotations_predef.setAdapter(annotationsAdapter);
        lv_Annotations_predef.setClickable(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        annotationsAdapter = new AnnotationsAdapter(getActivity(), new ArrayList<Annotation>()); //Initilisatisation de la liste d'annotations (vide)

    }

    // Crée le contextmenu pour les annotations qui utilisera le listener dans le MainActivity
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_annotations_predef) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            menu.findItem(R.id.edit_item_annot).setVisible(false);
            menu.findItem(R.id.delete_item_annot).setVisible(false);
            menu.findItem(R.id.edit_item_infos_annot).setVisible(false);
            menu.findItem(R.id.edit_item_video).setVisible(false);
            menu.findItem(R.id.delete_item_video).setVisible(false);
            menu.findItem(R.id.renommer_annot_predef).setVisible(true);
            menu.findItem(R.id.modifier_annot_predef).setVisible(true);
            menu.findItem(R.id.supprimer_annot_predef).setVisible(true);
            menu.findItem(R.id.edit_difficulte).setVisible(false);
        }
    }

    /**
     * Listener pour le clic sur la liste d'annotations
     */
    protected AdapterView.OnItemClickListener annotationItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Annotation annotation = (Annotation) lv_Annotations_predef.getItemAtPosition(position);
            onShowAnnotDialog(annotation);

            }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_annot_predef, container, false);
        lv_Annotations_predef = (ListView) view.findViewById(R.id.lv_annotations_predef);
        Cancel_btn = (Button) view.findViewById(R.id.button_annot_fragment);
        annotationsAdapter = new AnnotationsAdapter(getActivity(), ListAnnotationsPredef);
        lv_Annotations_predef.setAdapter(annotationsAdapter);
        lv_Annotations_predef.setOnItemClickListener(annotationItemClickListener);
        registerForContextMenu(lv_Annotations_predef);

        //Ferme le fragment
        Cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.closeAnnotPredef();
            }
        });

        return view;
    }

    // Affiche la boite de dialog qui permet de valider ou non le choix de l'annotation
    protected void onShowAnnotDialog(Annotation annot){
        DialogEditAnnotPredef mon_dialogue = new DialogEditAnnotPredef(this,annot);
        mon_dialogue.showDialogEdit();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view =  inflater.inflate(R.layout.fragment_annot_predef, container, false);
//        lv_Annotations_predef = (ListView)view.findViewById(R.id.lv_annotations_predef);
//        Cancel_btn = (Button)view.findViewById(R.id.button_annot_fragment);
//
//        annotationsAdapter = new AnnotationsAdapter(getActivity(),ListAnnotationsPredef);
//
//        lv_Annotations_predef.setAdapter(annotationsAdapter);
//
//
//        lv_Annotations_predef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Annotation annotation = (Annotation) lv_Annotations_predef.getItemAtPosition(position);
//                mListener.onAnnotPredefItemClick(annotation);
//                onShowAnnotDialog(annotation);
//
//            }
//        });

//        Cancel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mListener.closeAnnotPredef();
//                System.out.println("                    CLICK");
//            }
//        });
//
//        return view;
//    }

//    protected void onShowAnnotDialog(Annotation annot){
//        DialogEditAnnotPredef mon_dialogue = new DialogEditAnnotPredef(this,annot);
//        mon_dialogue.showDialogEdit();
//    }

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

        // Supprime une annotaion prédéfinie
        void onDeleteAnnotationPredef(Annotation annotation, int position);

        // Renommer une annotation prédéfinie
        void onRenommerAnnotationPredef(Annotation annotation, int position);

        // Modifier une annotation prédéfinie
        void onUpdateAnnotationPredef(Annotation annotation, int position);


        void closeAnnotPredef();

    }
}
