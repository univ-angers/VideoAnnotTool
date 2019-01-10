package com.master.info_ua.videoannottool.annotation_dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
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


    private DrawAnnotDialogListener dialogListener;

    public DialogDraw(Fragment_draw context) {

        this.context = context;
        this.dialog = new Dialog(this.context.getActivity());

        dialog.setContentView(R.layout.boite_dialog_draw);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.TextDialogDraw);

        btnValid = dialog.findViewById(R.id.btnValiderDraw);
        btnCancel = dialog.findViewById(R.id.btnAnnulerDraw);
        titre = dialog.findViewById(R.id.ed_draw_titre);
        duree = dialog.findViewById(R.id.ed_draw_temps);

        if (context instanceof DrawAnnotDialogListener) {
            dialogListener = context;
        }

    }


    public void showDialogDraw() {

        dialog.show();

        btnValid.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
    }

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

                        dialogListener.onSaveDrawImage(title, Integer.parseInt(duration));
                    }
                    break;
                case R.id.btnAnnulerDraw:
                    //context.resetCanvas();
                    dialogListener.onResetCanvas();
                    dialog.cancel();
                    break;
            }
            if (titre.getText().length() != 0 && duree.getText().length() != 0) { // vérifie si les champs sont vides
                //main.onSaveDrawAnnotation(titre.getText().toString(), valueOf(duree.getText().toString()));
                // création de l'annotation, il faut encore utiliser le titre et la durée, voir a passer en paramètre de creer_annotation()
                dialog.cancel();
            } else {
                Toast.makeText(context.getActivity(), "Il manque des informations", Toast.LENGTH_SHORT).show(); // ce toast est pas visible
            }
        }
    };

    public interface DrawAnnotDialogListener {
        void onSaveDrawImage(String title, int duration);

        void onResetCanvas();
    }
}

