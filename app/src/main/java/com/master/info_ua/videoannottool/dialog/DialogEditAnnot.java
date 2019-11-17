package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.fragment.Fragment_annotation;

public class DialogEditAnnot {

    private Fragment_annotation context;
    private Button btnValid;
    private Button btnCancel;
    private EditText titre;
    private EditText duree;
    private Dialog dialog;
    private EditAnnotDialogListener dialogListener;
    private Annotation annotation;

    //Constructeur
    public DialogEditAnnot(Fragment_annotation context, Annotation annot) {
        this.context = context;
        this.annotation = annot;
        this.dialog = new Dialog(this.context.getActivity());
        dialog.setContentView(R.layout.boite_dialog_edit_annot);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.TextDialogEditAnnot);
        btnValid = dialog.findViewById(R.id.btnValiderEdit);
        btnCancel = dialog.findViewById(R.id.btnAnnulerEdit);
        titre = dialog.findViewById(R.id.ed_edit_titre);
        duree = dialog.findViewById(R.id.ed_edit_temps);
        if (context instanceof EditAnnotDialogListener) {
            dialogListener = context;
        }

        titre.setText(this.annotation.getAnnotationTitle());
        duree.setText(String.valueOf(this.annotation.getAnnotationDuration()/1000));
    }

    //Affiche la boîte de dialogue
    public void showDialogEdit() {
        dialog.show();
        btnValid.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
    }

    //Lors d'un clic sur le bouton Valider ou Annuler
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int btnId = v.getId();
            switch (btnId) {
                case R.id.btnValiderEdit:
                    String title = titre.getText().toString();
                    String duration = duree.getText().toString();
                    if (title != null && !title.isEmpty() && duration != null && !duration.isEmpty()) {
                        dialogListener.onSaveEditAnnot(annotation, title, Integer.parseInt(duration)*1000);
                    }
                    dialog.dismiss();
                    Toast.makeText(context.getActivity(),R.string.editValidateToast, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnAnnulerEdit:
                    //on ferme la boite de dialog
                    dialog.cancel();
                    break;
            }
        }
    };

    public interface EditAnnotDialogListener {
        void onSaveEditAnnot(Annotation annot, String title, int duree);
    }
}
