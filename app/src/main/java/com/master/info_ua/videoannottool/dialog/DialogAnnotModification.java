package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.util.Util;

//Classe permettant l'affichage d'une boite de dialogue pour modifier les annotations textuelles
public class DialogAnnotModification {

    private int startTime;
    private DialogCallback textAnnotDialogCallback;

    //Constructeur
    public DialogAnnotModification(Context context, int startTime) {
        this.startTime = startTime;
        if (context instanceof DialogCallback) {
            textAnnotDialogCallback = (DialogCallback) context;
        }
    }

    //Ouvre la boite de dialogue pour la modification d'annotation textuelles
    public void showDialogBoxModif(final Annotation annotation, final MainActivity main, final int position) {
        Log.i("showDialogBoxModif",annotation.getAnnotationTitle());
        final Dialog dialogBox = new Dialog(main);
        dialogBox.setContentView(R.layout.boite_dialog_text);
        dialogBox.setTitle(R.string.ModifDialogText);
        Button btnValider = dialogBox.findViewById(R.id.btnValiderTextAnnot);
        Button btnAnnuler = dialogBox.findViewById(R.id.btnAnnulerTextAnnot);
        final CheckBox checkAnnotPredef = dialogBox.findViewById(R.id.CheckAnnotText);
        checkAnnotPredef.setEnabled(false);
        final EditText ed_texte_titre = dialogBox.findViewById(R.id.ed_texte_titre);
        ed_texte_titre.setText(annotation.getAnnotationTitle());
        // EditText disable
        ed_texte_titre.setFocusable(false);
        final TextView comment_texte_error = dialogBox.findViewById(R.id.error_comment_texte);
        final EditText etAnnot = dialogBox.findViewById(R.id.etAnnotText);
        etAnnot.setText(annotation.getTextComment());
        final EditText etDuration = dialogBox.findViewById(R.id.etDurationAnnot);
        etDuration.setText(""+annotation.getAnnotationDuration()/1000);
        etDuration.setFocusable(false);
        btnValider.setOnClickListener(new View.OnClickListener() {
            private String texte = "";
            //Lors d'un clic sur le bouton valider
            @Override
            public void onClick(View view) {
                if(!etAnnot.getText().toString().isEmpty()){
                    this.texte = etAnnot.getText().toString();
                    dialogBox.cancel();
                    annotation.setTextComment(texte);
                    //précise si l'annotation doit être sauvegardée parmi la liste des annotations prédéfinies
                    textAnnotDialogCallback.onSaveAnnotation(annotation,checkAnnotPredef.isChecked(), false);
                    Log.i("TEXT_DIALOG-BOX", "Validation :" + texte);
                    Toast.makeText(main, "Annotation Enregistrée", Toast.LENGTH_LONG).show();
                    Util.FermerClavier(view);
                } else {
                    comment_texte_error.setVisibility(View.VISIBLE);
                }
            }
        });
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TEXT_DIALOG-BOX", "Annulation");
                textAnnotDialogCallback.OnOffBoutons(true);
                dialogBox.cancel();
                ed_texte_titre.setFocusable(true);
                etDuration.setFocusable(true);
                checkAnnotPredef.setEnabled(true);
                Util.FermerClavier(view);
            }
        });
        dialogBox.show();
    }
}
