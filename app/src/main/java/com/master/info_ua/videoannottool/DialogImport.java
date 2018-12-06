package com.master.info_ua.videoannottool;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;


public class DialogImport {


    public void showDialogImport(final MainActivity main) {
        final Dialog dialog = new Dialog(main);

        dialog.setContentView(R.layout.boite_dialog_import);
        dialog.setTitle(R.string.ImportVideo);
        Button btnImporter = dialog.findViewById(R.id.btnImport);
        Button btnAnnuler = dialog.findViewById(R.id.btnCancelImport);

        //Fonctionnalités à implanter quand on valide l'importation
        btnImporter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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