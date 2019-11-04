package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.fragment.Fragment_draw;

public class DialogDraw {

    private Fragment_draw context;
    private Button btnValid;
    private Button btnCancel;
    private EditText titre;
    private EditText duree;
    private Dialog dialog;
    private CheckBox checkAnnotPredef;
    private DrawAnnotDialogListener dialogListener;

    //Constructeur
    public DialogDraw(Fragment_draw context) {
        this.context = context;
        this.dialog = new Dialog(this.context.getActivity());
        dialog.setContentView(R.layout.boite_dialog_draw);
        //On ne peut pas annuler cette boîte de dialogue?
        dialog.setCancelable(false);
        dialog.setTitle(R.string.TextDialogDraw);
        btnValid = dialog.findViewById(R.id.btnValiderDraw);
        btnCancel = dialog.findViewById(R.id.btnAnnulerDraw);
        titre = dialog.findViewById(R.id.ed_draw_titre);
        duree = dialog.findViewById(R.id.ed_draw_temps);
        checkAnnotPredef = dialog.findViewById(R.id.CheckAnnotDraw);
        if (context instanceof DrawAnnotDialogListener) {
            dialogListener = context;
        }
    }

    //Affiche la boîte de dialogue
    public void showDialogDraw() {
        dialog.show();
        btnValid.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
    }

    //Lors d'un clic sur le bouton Valider ou Annuler
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int btnId = v.getId();
            switch (btnId) {
                case R.id.btnValiderDraw:
                    String title = titre.getText().toString();
                    String duration = duree.getText().toString();
                    if (title != null && !title.isEmpty() && duration != null && !duration.isEmpty()) {
                        //drawAnnot.setAnnotationTitle(title);
                        //drawAnnot.setAnnotationDuration(Integer.parseInt(duration));
                        //précise si l'annotation doit être sauvegardé parmis la liste des annotations prédéfinies
                        dialogListener.onSaveDrawImage(title, Integer.parseInt(duration)*1000,checkAnnotPredef.isChecked());
                    }
                    break;
                case R.id.btnAnnulerDraw:
                    dialogListener.onResetCanvas();
                    dialog.cancel();
                    break;
            }
            //Vérifie si les champs titre et duree sont vides
            if (titre.getText().length() != 0 && duree.getText().length() != 0) {
                //Création de l'annotation, il faut encore utiliser le titre et la durée, voir à passer en paramètre de creer_annotation() ???
                dialog.cancel();
            } else {
                Toast.makeText(context.getActivity(), "Il manque des informations", Toast.LENGTH_SHORT).show(); // ce toast est pas visible
            }
        }
    };

    public interface DrawAnnotDialogListener {
        //Appelée lors de la sauvegarde d'une annotation de type dessin
        void onSaveDrawImage(String title, int duration, boolean check);
        //Appelée lors de la réinitialisation de la toile
        void onResetCanvas();
    }
}

