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

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.annotation.Annotation;

//Classe permettant l'affichage d'une boite de dialogue pour acquérir les annotations textuelles
public class DialogText {

    private int startTime;
    private DialogCallback textAnnotDialogCallback;

    //Constructeur
    public DialogText(Context context, int startTime) {
        this.startTime = startTime;
        if (context instanceof DialogCallback) {
            textAnnotDialogCallback = (DialogCallback) context;
        }
    }

    //Ouvre la boite de dialogue pour la saisie d'annotation textuelles
    public void showDialogBox(final Annotation annotation, final MainActivity main) {
        final Dialog dialogBox = new Dialog(main);
        dialogBox.setContentView(R.layout.boite_dialog_text);
        dialogBox.setTitle(R.string.TitleDialogText);
        Button btnValider = dialogBox.findViewById(R.id.btnValiderTextAnnot);
        Button btnAnnuler = dialogBox.findViewById(R.id.btnAnnulerTextAnnot);
        final CheckBox checkAnnotPredef = dialogBox.findViewById(R.id.CheckAnnotText);
        final EditText ed_texte_titre = dialogBox.findViewById(R.id.ed_texte_titre);
        final TextView title_texte_error = dialogBox.findViewById(R.id.error_title_texte);
        final EditText etAnnot = dialogBox.findViewById(R.id.etAnnotText);
        final EditText etDuration = dialogBox.findViewById(R.id.etDurationAnnot);
        btnValider.setOnClickListener(new View.OnClickListener() {
            private String texte = "";
            //Lors d'un clic sur le bouton valider
            @Override
            public void onClick(View view) {
                if(!ed_texte_titre.getText().toString().isEmpty()){
                    this.texte = etAnnot.getText().toString();
                    dialogBox.cancel();
                    annotation.setAnnotationDuration(Integer.parseInt(etDuration.getText().toString()) * 1000);
                    annotation.setTextComment(texte);
                    annotation.setAnnotationTitle(ed_texte_titre.getText().toString());
                    annotation.setAnnotationTitle(ed_texte_titre.getText().toString());
                    //précise si l'annotation doit être sauvegardé parmis la liste des annotations prédéfinies
                    textAnnotDialogCallback.onSaveAnnotation(annotation,checkAnnotPredef.isChecked());
                    Log.i("TEXT_DIALOG-BOX", "Validation :" + texte);
                    Toast.makeText(main, "Annotation Enregistrée", Toast.LENGTH_LONG).show();
                } else {
                    title_texte_error.setVisibility(View.VISIBLE);
                }
            }
        });
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TEXT_DIALOG-BOX", "Annulation");
                textAnnotDialogCallback.OnOffBoutons(true);
                dialogBox.cancel();
            }
        });
        dialogBox.show();
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
//                    annotation.setAnnotationDuration(Integer.parseInt(etDuration.getText().toString()) * 1000);
                    annotation.setTextComment(texte);
//                    annotation.setAnnotationTitle(ed_texte_titre.getText().toString());
//                    //précise si l'annotation doit être sauvegardé parmis la liste des annotations prédéfinies
                    textAnnotDialogCallback.onSaveTextAnnotation(annotation,checkAnnotPredef.isChecked(), position);
                    Log.i("TEXT_DIALOG-BOX", "Validation :" + texte);
                    Toast.makeText(main, "Annotation Enregistrée", Toast.LENGTH_LONG).show();
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
            }
        });
        dialogBox.show();
    }
}

