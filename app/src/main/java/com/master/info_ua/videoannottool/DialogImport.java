package com.master.info_ua.videoannottool;

import android.app.Dialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.master.info_ua.videoannottool.adapter.SpinnerAdapter;
import com.master.info_ua.videoannottool.annotation.DirPath;
import com.master.info_ua.videoannottool.util.Categorie;

import java.util.ArrayList;
import java.util.List;


public class DialogImport {


    public void showDialogImport(final MainActivity main) {
        final Dialog dialog = new Dialog(main);





        dialog.setContentView(R.layout.boite_dialog_import);
        dialog.setTitle(R.string.ImportVideo);

        //Initialisation du spinner categorie
        Spinner spinnerCategorie = dialog.findViewById(R.id.spinner_import_cat);

        List<Categorie> categorieList = new ArrayList<>();
        categorieList.add(new Categorie("Categorie", null, "/"));
        categorieList.addAll(main.setCatSpinnerList());

        ArrayAdapter<Categorie> spinnerAdapter = new SpinnerAdapter(main, android.R.layout.simple_spinner_item, categorieList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(spinnerAdapter);


        //Initialisation du spinner sous-categorie
        Spinner spinnerSubCategorie = dialog.findViewById(R.id.spinner_import_sub_cat);

        List<Categorie> spinnerList2 = new ArrayList<>();
        spinnerList2.add(new Categorie("Sous-categorie", null, "/"));
        spinnerList2.addAll(main.setSubCatSpinnerList(DirPath.CATEGORIE1.getPath()));


        ArrayAdapter<Categorie> spinnerAdapter2 = new SpinnerAdapter(main, android.R.layout.simple_spinner_item, spinnerList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategorie.setAdapter(spinnerAdapter2);


        Button btnChoisirVideo = dialog.findViewById(R.id.btnChoisirVideoImport);
        Button btnImporter = dialog.findViewById(R.id.btnImport);
        Button btnAnnuler = dialog.findViewById(R.id.btnCancelImport);

        btnChoisirVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

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