package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.custom.Video;

public class DialogVignette {

    private MainActivity main;
    private Button btnValidVignette;
    private Button btnCancelVignette;
    private Dialog dialog;
    private DialogCallback dialogCallback;
    private EditVignetteDialogListener dialogListener;

    //Constructeur
    public DialogVignette(MainActivity main) {
        this.main = main;
        this.dialog = new Dialog(this.main);
        this.dialogCallback= (DialogCallback) main;
        dialog.setContentView(R.layout.boite_dialog_vignette);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.ChangeVignette);
        btnValidVignette = dialog.findViewById(R.id.btnValiderVignette);
        btnCancelVignette = dialog.findViewById(R.id.btnAnnulerVignette);
        if (main instanceof EditVignetteDialogListener) {
            dialogListener = main;
        }
    }

    //Affiche la boîte de dialogue
    public void showDialogVignette() {
        dialog.show();
        btnValidVignette.setOnClickListener(clickListener);
        btnCancelVignette.setOnClickListener(clickListener);
    }

    //Lors d'un clic sur le bouton Valider ou Annuler
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int btnId = v.getId();
            switch (btnId) {
                case R.id.btnValiderVignette:
                    dialogListener.onSaveEditVignette();
                    dialog.dismiss();
                    Toast.makeText(main,R.string.validateVignette, Toast.LENGTH_SHORT).show();
                    dialogCallback.OnOffBoutons(true);
                    break;
                case R.id.btnAnnulerVignette:
                    //on ferme la boite de dialog
                    dialogCallback.OnOffBoutons(true);
                    dialog.cancel();
                    break;
            }
            System.out.println("je passe");
        }
    };

    public interface EditVignetteDialogListener {
        void onSaveEditVignette();
    }
}