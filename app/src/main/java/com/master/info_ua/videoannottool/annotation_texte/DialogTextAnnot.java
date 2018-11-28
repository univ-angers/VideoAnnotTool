package com.master.info_ua.videoannottool.annotation_texte;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.MainActivity;

import static android.graphics.Color.WHITE;

public class DialogTextAnnot {


    public void showDialogText (final MainActivity main){
        final Dialog dialog = new Dialog(main);

        dialog.setContentView(R.layout.boite_dialog_text);
        dialog.setTitle(R.string.TitleDialogText);
        Button btnValider =  dialog.findViewById(R.id.btnValiderTexte);
        Button btnAnnuler = dialog.findViewById(R.id.btnAnnulerTexte);
        final EditText etAnnot = dialog.findViewById(R.id.etAnnotText);

        btnValider.setOnClickListener(new View.OnClickListener() {
            private String texte="";

            @Override
            public void onClick(View view) {
                this.texte=etAnnot.getText().toString();
                System.out.println(texte);
                dialog.cancel();

                Toast messagefin;
                messagefin = Toast.makeText(main, "Annotation Enregistrée", Toast.LENGTH_LONG);
                messagefin.show();




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

