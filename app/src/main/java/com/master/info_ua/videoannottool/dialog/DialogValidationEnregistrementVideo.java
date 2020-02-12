package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.fragment.Fragment_annotation;
import com.master.info_ua.videoannottool.util.Util;

import static com.master.info_ua.videoannottool.MainActivity.COACH;

public class DialogValidationEnregistrementVideo {
    //Sert à savoir si le bouton pour passer à la suite à été cliqué avec un nom de donnée.
    private boolean testBoutonCliquer = false;
    //Affichage de la boîte de dialog pour l'acceptation de l'enregistrement et du choix du nom de la vidéo.
    public void showDialogValidationEnregistrementVideo(final MainActivity main) {
        final Dialog dialog = new Dialog(main);
        testBoutonCliquer = false;
        //Le dialog à afficher.
        dialog.setContentView(R.layout.boite_dialog_start_recorder);
        //Le titre correspondant au dialog
        dialog.setTitle(R.string.text_titre_dialog_enregistrement_video);
        final EditText editTextNomVideo = dialog.findViewById(R.id.ed_nom_video);
        final TextView texteAvertissement = dialog.findViewById(R.id.tv_avertissement_enregistrement_video);
        Button btnPasserALaSuite = dialog.findViewById(R.id.btn_validation_enregistrement_video);
        btnPasserALaSuite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout btnLayout = main.findViewById(R.id.btn_layout_id);
                if(!editTextNomVideo.getText().toString().isEmpty() && !editTextNomVideo.getText().toString().equals("")) {
                    //Le bouton a été cliqué, il ne faut pas appliquer la fonction d'annulation d'enregistrement.
                    testBoutonCliquer = true;
                    main.nomVideoAEnregistrer = editTextNomVideo.getText().toString();
                    Util.FermerClavier(view);
                    main.validationEnregistrementVideo();
                    dialog.cancel();
                }
            }
        });
        //Attention, le dismiss est lancé avec le dialog.cancel() du bouton listener.Il faut donc tester si le bouton a été cliqué.
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!testBoutonCliquer) {
                    main.AnnulationEnregistrementVideo();
                }
            }
        });
        dialog.show();
    }
}
