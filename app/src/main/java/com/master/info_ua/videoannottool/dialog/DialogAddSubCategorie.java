package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.master.info_ua.videoannottool.CategoryActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.util.Categorie;

public class DialogAddSubCategorie {

        private CategoryActivity context;
        private Button btnValid;
        private Button btnCancel;
        private EditText titre;
        private Dialog dialog;
        private Categorie categorie;
        private DialogAddSubCategorie.AddSubCategoryDialogListener dialogListener;

        public DialogAddSubCategorie(CategoryActivity context, Categorie categorie) {
            this.context = context;
            this.categorie = categorie;
            this.dialog = new Dialog(this.context);
            dialog.setContentView(R.layout.boite_dialog_add_sub_category);
            dialog.setCancelable(true);
            dialog.setTitle(R.string.TextDialogAddSubCategorie);
            btnValid = dialog.findViewById(R.id.btnValiderAdd);
            btnCancel = dialog.findViewById(R.id.btnAnnulerAdd);
            titre = dialog.findViewById(R.id.ed_add_titre);

            if (context instanceof DialogAddSubCategorie.AddSubCategoryDialogListener) {
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
                    case R.id.btnValiderAdd:
                        String title = titre.getText().toString();
                        boolean isValid = true;
                        if (title.isEmpty()){
                            Toast.makeText(context, "Veuillez renseigner un nom de sous-catégorie", Toast.LENGTH_LONG).show();
                            isValid = false;
                        }
                        for(Categorie sub : categorie.getSubCategories()){
                            if(sub.getName().toUpperCase().equals(title.toUpperCase())){
                                Toast.makeText(context, "Cette sous-catégorie existe déjà", Toast.LENGTH_LONG).show();
                                isValid = false;
                                break;
                            }
                        }
                        if (isValid) {
                            dialogListener.onSaveAddSubCategory(categorie, title);
                            dialog.dismiss();
                            Toast.makeText(context, R.string.editValidateToast, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.btnAnnulerAdd:
                        //on ferme la boite de dialog
                        dialog.cancel();
                        break;
                }
            }
        };

        public interface AddSubCategoryDialogListener {
            void onSaveAddSubCategory(Categorie categorie, String title);
        }
}
