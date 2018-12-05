package com.master.info_ua.videoannottool.annotation_dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;



public class DialogDraw {

    String image_name;


    public void showDialogDraw(final MainActivity main, String videoName) {
        final Dialog dialog = new Dialog(main);

        dialog.setContentView(R.layout.boite_dialog_draw);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.TextDialogDraw);


        final Button btnValid   = dialog.findViewById(R.id.btnValiderDraw);
        final Button btnCancel  = dialog.findViewById(R.id.btnAnnulerDraw);
        final EditText titre    = dialog.findViewById(R.id.ed_draw_titre);
        final EditText duree    = dialog.findViewById(R.id.ed_draw_temps);

        dialog.show();

        btnValid.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(titre.getText().length() != 0  && duree.getText().length() != 0) { // vérifie si les champs sont vides
                   // main.creer_annotation_draw();
                    // création de l'annotation, il faut encore utiliser le titre et la durée, voir a passer en paramètre de creer_annotation()
                    dialog.cancel();
                }
                else {
                    Toast.makeText(main,"Il manque des informations", Toast.LENGTH_SHORT).show(); // ce toast est pas visible
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                main.resetCanvas();
                dialog.cancel();
            }
        });
    }

    public DialogDraw(){

    }
}

