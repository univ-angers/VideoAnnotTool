package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.master.info_ua.videoannottool.CategoryActivity;
import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.custom.Video;
import com.master.info_ua.videoannottool.util.Categorie;
import com.master.info_ua.videoannottool.util.Util;

public class DialogEditCategorie {
    private CategoryActivity context;
    private Button btnValid;
    private Button btnCancel;
    private EditText titre;
    private Dialog dialog;
    private Categorie categorie;
    private EditCategoryDialogListener dialogListener;

    public DialogEditCategorie(CategoryActivity context, Categorie categorie) {
        this.context = context;
        this.categorie = categorie;
        this.dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.boite_dialog_edit_categorie);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.TextDialogEditCategorie);
        btnValid = dialog.findViewById(R.id.btnValiderEdit);
        btnCancel = dialog.findViewById(R.id.btnAnnulerEdit);
        titre = dialog.findViewById(R.id.ed_edit_titre);
        titre.setText(this.categorie.getName());

        if (context instanceof DialogEditCategorie.EditCategoryDialogListener) {
            dialogListener = context;
        }
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
                        dialogListener.onSaveEditCategorie(categorie, title);
                    }
                    dialog.dismiss();
                    Toast.makeText(context,R.string.editValidateToast, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnAnnulerEdit:
                    Util.FermerClavier(v);
                    //on ferme la boite de dialog
                    dialog.cancel();
                    break;
            }
        }
    };

    public interface EditCategoryDialogListener {
        void onSaveEditCategorie(Categorie categorie, String title);
    }
}
