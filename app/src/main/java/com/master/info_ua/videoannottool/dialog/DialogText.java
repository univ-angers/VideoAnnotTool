package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.Context;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.annotation.Annotation;

/**
 * Classe permettant l'affichage d'une boite de dialogue pour acquérir les annotations textuelles
 */
public class DialogText {
    private int starttime;
    private DialogCallback textAnnotDialogCallback;

    /**
     * Constructeur de la classe
     *
     * @param context
     * @param startTime
     */
    public DialogText(Context context, int startTime) {
        this.starttime = startTime;

        if (context instanceof DialogCallback) {
            textAnnotDialogCallback = (DialogCallback) context;
        }
    }

    /**
     * Ouvre la boite de dialogue pour la saisie d'annotation textuelles
     *
     * @param annotation
     * @param main
     */
    public void showDialogBox(final Annotation annotation, final MainActivity main) {
        final Dialog dialogBox = new Dialog(main);

        dialogBox.setContentView(R.layout.boite_dialog_text);
        dialogBox.setTitle(R.string.TitleDialogText);
        Button btnValider = dialogBox.findViewById(R.id.btnValiderTextAnnot);
        Button btnAnnuler = dialogBox.findViewById(R.id.btnAnnulerTextAnnot);
        final EditText ed_texte_titre = dialogBox.findViewById(R.id.ed_texte_titre);
        final TextView title_texte_error = dialogBox.findViewById(R.id.error_title_texte);
        final EditText etAnnot = dialogBox.findViewById(R.id.etAnnotText);
        final EditText etDuration = dialogBox.findViewById(R.id.etDurationAnnot);

        btnValider.setOnClickListener(new View.OnClickListener() {
            private String texte = "";

            @Override
            public void onClick(View view) {
                if(!ed_texte_titre.getText().toString().isEmpty()){

                    this.texte = etAnnot.getText().toString();
                    dialogBox.cancel();

                    annotation.setAnnotationDuration(Integer.parseInt(etDuration.getText().toString()) * 1000);
                    annotation.setTextComment(texte);
                    annotation.setAnnotationTitle(ed_texte_titre.getText().toString());
                    textAnnotDialogCallback.addTextAnnot(annotation);
                    Log.i("TEXT_DIALOG-BOX", "Validation :" + texte);
                    Toast toastConfirmAnnot;
                    toastConfirmAnnot = Toast.makeText(main, "Annotation Enregistrée", Toast.LENGTH_LONG);
                    toastConfirmAnnot.show();
                }else{
                    title_texte_error.setVisibility(View.VISIBLE);
                }

            }
        });
        btnAnnuler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.i("TEXT_DIALOG-BOX", "Annulation");
                dialogBox.cancel();


            }
        });


        dialogBox.show();
    }

}

