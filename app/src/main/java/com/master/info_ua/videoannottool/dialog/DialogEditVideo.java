package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.custom.Video;
import com.master.info_ua.videoannottool.util.Util;

public class DialogEditVideo {

    private MainActivity context;
    private Button btnValid;
    private Button btnCancel;
    private EditText titre;
    private Dialog dialog;
    private EditVideoDialogListener dialogListener;
    private Video video;

    //Constructeur
    public DialogEditVideo(MainActivity context, Video video) {
        this.context = context;
        this.video = video;
        this.dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.boite_dialog_edit_video);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.TextDialogEditVideo);
        btnValid = dialog.findViewById(R.id.btnValiderEdit);
        btnCancel = dialog.findViewById(R.id.btnAnnulerEdit);
        titre = dialog.findViewById(R.id.ed_edit_titre);
        if (context instanceof EditVideoDialogListener) {
            dialogListener = context;
        }

        titre.setText(this.video.getFileName());
    }

    //Affiche la boîte de dialogue
    public void showDialogEdit() {
        dialog.show();
        btnValid.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
    }

    //Lors d'un clic sur le bouton Valider ou Annuler
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int btnId = v.getId();
            switch (btnId) {
                case R.id.btnValiderEdit:
                    String title = titre.getText().toString();
                    Util.FermerClavier(v);
                    if (title != null && !title.isEmpty()) {
                        dialogListener.onSaveEditVideo(video, title);
                    }
                    dialog.dismiss();
                    Toast.makeText(context,R.string.editValidateToast, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnAnnulerEdit:
                    //on ferme la boite de dialog
                    Util.FermerClavier(v);
                    dialog.cancel();
                    break;
            }
        }
    };

    public interface EditVideoDialogListener {
        void onSaveEditVideo(Video video, String title);
    }
}