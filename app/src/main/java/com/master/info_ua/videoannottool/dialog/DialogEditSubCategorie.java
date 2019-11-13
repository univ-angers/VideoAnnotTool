package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.master.info_ua.videoannottool.CategoryActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.master.info_ua.videoannottool.util.Categorie;

public class DialogEditSubCategorie {

    private CategoryActivity context;
    private Button btnValid;
    private Button btnCancel;
    private EditText titre;
    private Dialog dialog;
    private Categorie categorie;
    private DialogEditSubCategorie.EditSubCategoryDialogListener dialogListener;

    public DialogEditSubCategorie(CategoryActivity context, Categorie categorie) {
        this.context = context;
        this.categorie = categorie;
        this.dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.boite_dialog_edit_sub_category);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.TextDialogEditSubCategorie);
        btnValid = dialog.findViewById(R.id.btnValiderEdit);
        btnCancel = dialog.findViewById(R.id.btnAnnulerEdit);
        titre = dialog.findViewById(R.id.ed_edit_titre);
        titre.setText(this.categorie.getName());

        if (context instanceof DialogEditSubCategorie.EditSubCategoryDialogListener) {
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
                    if (title != null && !title.isEmpty()) {
                        dialogListener.onSaveEditSubCategorie(categorie, title);
                    }
                    dialog.dismiss();
                    Toast.makeText(context,R.string.editValidateToast, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnAnnulerEdit:
                    //on ferme la boite de dialog
                    dialog.cancel();
                    break;
            }
        }
    };

    public interface EditSubCategoryDialogListener {
        void onSaveEditSubCategorie(Categorie categorie, String title);
    }
}
