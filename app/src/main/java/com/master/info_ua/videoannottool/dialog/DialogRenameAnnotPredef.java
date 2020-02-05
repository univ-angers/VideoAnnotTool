package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;

//Classe permettant l'affichage une boîte de dialog pour renommer les trois types d'annotation(TEXT, AUDIO, DRAW)
public class DialogRenameAnnotPredef {

    private int startTime;
    private DialogCallback textAnnotDialogCallback;

    //Constructeur
    public DialogRenameAnnotPredef(Context context, int startTime) {
        this.startTime = startTime;
        if (context instanceof DialogCallback) {
            textAnnotDialogCallback = (DialogCallback) context;
        }
    }

    //Ouvre la boite de dialogue pour la modification d'annotation prédéfinie
    public void showDialogBoxRenommer(final Annotation annotation, final MainActivity main, final int position) {
        Log.i("showDialogBoxRenommer",annotation.getAnnotationTitle());
        final Dialog dialogBox = new Dialog(main);
        dialogBox.setContentView(R.layout.boite_dialog_renomme_annot_predef);
        dialogBox.setTitle(R.string.RenommerDialogText);

        Button btnValider = dialogBox.findViewById(R.id.btnValiderRenommer);
        Button btnAnnuler = dialogBox.findViewById(R.id.btnAnnulerRenommer);

//        final CheckBox checkAnnotPredef = dialogBox.findViewById(R.id.CheckAnnotText);
        final EditText ed_texte_titre_predef = dialogBox.findViewById(R.id.ed_edit_titre_predef);

        ed_texte_titre_predef.setText(annotation.getAnnotationTitle());

        final EditText etDuration = dialogBox.findViewById(R.id.ed_edit_temps);
        etDuration.setText(""+annotation.getAnnotationDuration()/1000);

        btnValider.setOnClickListener(new View.OnClickListener() {

            //Lors d'un clic sur le bouton valider
            @Override
            public void onClick(View view) {
                if(!ed_texte_titre_predef.getText().toString().isEmpty() && !etDuration.getText().toString().isEmpty()){

                    dialogBox.cancel();
                    String oldTitle = annotation.getAnnotationTitle();
                    long oldDuration = annotation.getAnnotationDuration();
                    annotation.setAnnotationTitle(ed_texte_titre_predef.getText().toString());
                    annotation.setAnnotationDuration(Integer.parseInt(etDuration.getText().toString())*1000);
                    textAnnotDialogCallback.onRenameAnnotationPredef(annotation, oldTitle);
                    Log.i("TEXT_DIALOG-BOX", "Validation :" + annotation.getAnnotationTitle());
                    Toast.makeText(main, "Annotation Prédéfinie Renommer", Toast.LENGTH_LONG).show();
                } //else {
//                    comment_texte_error.setVisibility(View.VISIBLE);
//                }
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

}
