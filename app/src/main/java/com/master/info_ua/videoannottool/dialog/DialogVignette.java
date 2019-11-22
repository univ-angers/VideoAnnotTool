package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.custom.Video;

public class DialogVignette {

    private MainActivity context;
    private Button btnValidVignette;
    private Button btnCancelVignette;
    private TextView titre;
    private Dialog dialog;
    private EditVignetteDialogListener dialogListener;

    //Constructeur
    public DialogVignette(MainActivity context) {
        this.context = context;
        this.dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.boite_dialog_vignette);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.ChangeVignette);
        btnValidVignette = dialog.findViewById(R.id.btnValiderVignette);
        btnCancelVignette = dialog.findViewById(R.id.btnAnnulerVignette);
        titre = dialog.findViewById(R.id.tv_vignette);
        if (context instanceof EditVignetteDialogListener) {
            dialogListener = context;
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
                    Toast.makeText(context,R.string.validateVignette, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnAnnulerVignette:
                    //on ferme la boite de dialog
                    dialog.cancel();
                    break;
            }
        }
    };

    public interface EditVignetteDialogListener {
        void onSaveEditVignette();
    }
}