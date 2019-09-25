package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;

import static com.master.info_ua.videoannottool.MainActivity.COACH;


public class DialogProfil {

    //Affichage de la boîte de dialog pour le choix du profil (Profil Coach ou Profil Athlète)
    public void showDialogProfil(final MainActivity main,final MenuItem item) {
        final Dialog dialog = new Dialog(main);
        final ImageButton audioAnnotBtn = main.findViewById(R.id.audio_annot_btn);
        final ImageButton textAnnotBtn = main.findViewById(R.id.text_annot_btn);
        final ImageButton graphAnnotBtn = main.findViewById(R.id.graphic_annot_btn);
        dialog.setContentView(R.layout.boite_dialog_profil);
        dialog.setTitle(R.string.profil);
        final EditText editTextModeCoach = dialog.findViewById(R.id.editTextModeCoach);
        final TextView invalidPassword = dialog.findViewById(R.id.invalidPassword);
        Button btnPasserCoach = dialog.findViewById(R.id.btnPasserCoach);
        Button btnAnnuler = dialog.findViewById(R.id.btnAnnulerPasserCoach);
        btnPasserCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout btnLayout = main.findViewById(R.id.btn_layout_id);
                //Gestion du mot de passe à changer?
                if(!editTextModeCoach.getText().toString().isEmpty() && editTextModeCoach.getText().toString().equals("123")) {
                    btnLayout.setVisibility(View.VISIBLE);
                    audioAnnotBtn.setEnabled(true);
                    textAnnotBtn.setEnabled(true);
                    graphAnnotBtn.setEnabled(true);
                    main.setStatutProfil(COACH);
                    item.setTitle("Mode consultation");
                    dialog.cancel();
                } else {
                    invalidPassword.setVisibility(View.VISIBLE);
                }
            }
        });
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}