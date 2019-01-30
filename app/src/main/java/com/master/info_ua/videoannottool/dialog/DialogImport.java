package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.adapter.SpinnerAdapter;
import com.master.info_ua.videoannottool.util.Categorie;

import java.util.ArrayList;
import java.util.List;


public class DialogImport {

    private Button btnChoisirVideo ;
    private Button btnImporter;
    private Button btnAnnuler;

    private Dialog dialog;

    private DialogCallback dialogCallback;

    public void showDialogImport(Context context) {

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.boite_dialog_import);
        dialog.setTitle(R.string.ImportVideo);

        //Initialisation du spinner categorie
        Spinner spinnerCategorie = dialog.findViewById(R.id.spinner_import_cat);

        List<Categorie> categorieList = new ArrayList<>();
        categorieList.add(new Categorie("Categorie", null, "/"));
        //categorieList.addAll(context.setCatSpinnerList());

        ArrayAdapter<Categorie> spinnerAdapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, categorieList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(spinnerAdapter);


        //Initialisation du spinner sous-categorie
        Spinner spinnerSubCategorie = dialog.findViewById(R.id.spinner_import_sub_cat);

        List<Categorie> spinnerList2 = new ArrayList<>();
        spinnerList2.add(new Categorie("Sous-categorie", null, "/"));
        //spinnerList2.addAll(main.setSubCatSpinnerList(DirPath.CATEGORIE1.getPath()));


        ArrayAdapter<Categorie> spinnerAdapter2 = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, spinnerList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategorie.setAdapter(spinnerAdapter2);

        if (context instanceof DialogCallback) {
            dialogCallback = (DialogCallback) context;
        }


        btnChoisirVideo = dialog.findViewById(R.id.btnChoisirVideoImport);
        btnChoisirVideo.setOnClickListener(btnClickListener);

        btnImporter = dialog.findViewById(R.id.btnImport);
        btnImporter.setOnClickListener(btnClickListener);

        btnAnnuler = dialog.findViewById(R.id.btnCancelImport);
        btnAnnuler.setOnClickListener(btnClickListener);


        dialog.show();
    }


    protected View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int btnId = v.getId();

            switch (btnId){
                case R.id.btnChoisirVideoImport:
                    dialogCallback.onClickVideoFileImport();
                    break;

                case R.id.btnImport:
                    break;

                case R.id.btnCancelImport:
                    dialog.cancel();
                    break;
            }
        }
    };
}