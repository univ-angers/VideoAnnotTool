package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.adapter.SpinnerAdapter;
import com.master.info_ua.videoannottool.util.DirPath;
import com.master.info_ua.videoannottool.util.Categorie;
import com.master.info_ua.videoannottool.util.Util;

import java.util.ArrayList;
import java.util.List;


public class DialogShare {


    public void showDialogShare(Context context) {
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.boite_dialog_share);
        dialog.setTitle(R.string.ShareVideo);

        //Initialisation du spinner categorie
        Spinner spinnerCategorie = dialog.findViewById(R.id.spinner_share_cat);

        List<Categorie> categorieList = new ArrayList<>();
        categorieList.add(new Categorie("Categorie", null, "/"));
        categorieList.addAll(Util.setCatSpinnerList(context));

        ArrayAdapter<Categorie> spinnerAdapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, categorieList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(spinnerAdapter);

        //Initialisation du spinner sous-categorie
        Spinner spinnerSubCategorie = dialog.findViewById(R.id.spinner_share_sub_cat);

        List<Categorie> spinnerList2 = new ArrayList<>();
        spinnerList2.add(new Categorie("Sous-categorie", null, "/"));
        spinnerList2.addAll(Util.setSubCatSpinnerList(DirPath.CATEGORIE1.getPath()));

        ArrayAdapter<Categorie> spinnerAdapter2 = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, spinnerList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategorie.setAdapter(spinnerAdapter2);



        Button btnChoisirVideo = dialog.findViewById(R.id.btnChoisirVideoShare);

        Button btnPartager = dialog.findViewById(R.id.btnPartager);
        Button btnAnnuler = dialog.findViewById(R.id.btnAnnulerPartage);

        btnChoisirVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        //Fonctionnalités à implanter quand on valide le partage/exportation
        btnPartager.setOnClickListener(new View.OnClickListener() {

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