package com.master.info_ua.videoannottool.menu;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;

import static com.master.info_ua.videoannottool.MainActivity.COACH;


public class DialogProfil {


    public void showDialogProfil(final MainActivity main) {

        final Dialog dialog = new Dialog(main);
        final ImageButton audioAnnotBtn = main.findViewById(R.id.audio_annot_btn);
        final ImageButton textAnnotBtn = main.findViewById(R.id.text_annot_btn);
        final ImageButton graphAnnotBtn = main.findViewById(R.id.graphic_annot_btn);


        dialog.setContentView(R.layout.boite_dialog_profil);
        dialog.setTitle(R.string.profil);
        Button btnPasserCoach = dialog.findViewById(R.id.btnPasserCoach);
        Button btnAnnuler = dialog.findViewById(R.id.btnAnnulerPasserCoach);

        btnPasserCoach.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                audioAnnotBtn.setEnabled(true);
                textAnnotBtn.setEnabled(true);
                graphAnnotBtn.setEnabled(true);
                main.setStatutProfil(COACH);
                dialog.cancel();
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