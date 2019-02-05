package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.adapter.SpinnerAdapter;
import com.master.info_ua.videoannottool.util.Categorie;
import com.master.info_ua.videoannottool.util.Util;

import java.util.ArrayList;
import java.util.List;


public class DialogImport {

    private Button btnChoisirVideo ;
    private Button btnImporter;
    private Button btnAnnuler;
    private TextView videoImportTextView;
    private Categorie currentCategorie;
    private Categorie currentSubCategorie;
    private ArrayAdapter<Categorie> spinnerAdapter2;
    private ArrayAdapter<Categorie> spinnerAdapter;
    private Spinner spinnerCategorie;
    private Spinner spinnerSubCategorie;

    private Dialog dialog;

    private DialogCallback dialogCallback;

    public void showDialogImport(Context context) {
        dialog = new Dialog(context);

        dialog.setContentView(R.layout.boite_dialog_import);
        dialog.setTitle(R.string.ImportVideo);

        //Initialisation du spinner categorie
        spinnerCategorie = dialog.findViewById(R.id.spinner_import_cat);

        List<Categorie> categorieList = new ArrayList<>();
        categorieList.add(new Categorie("Categorie", null, "/"));
        categorieList.addAll(Util.setCatSpinnerList(context));

        spinnerAdapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, categorieList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(spinnerAdapter);


        //Initialisation du spinner sous-categorie
        spinnerSubCategorie = dialog.findViewById(R.id.spinner_import_sub_cat);

        List<Categorie> spinnerList2 = new ArrayList<>();
        spinnerList2.add(new Categorie("Sous-categorie", null, "/"));
        //spinnerList2.addAll(main.setSubCatSpinnerList(DirPath.CATEGORIE1.getPath()));


        spinnerAdapter2 = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, spinnerList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategorie.setAdapter(spinnerAdapter2);

        spinnerCategorie.setOnItemSelectedListener(catItemSelectedListener);
        spinnerCategorie.setSelection(1);


        if (context instanceof DialogCallback) {
            dialogCallback = (DialogCallback) context;
        }


        btnChoisirVideo = dialog.findViewById(R.id.btnChoisirVideoImport);
        btnChoisirVideo.setOnClickListener(btnClickListener);

        videoImportTextView = dialog.findViewById(R.id.videoImportTextView);

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
                    dialogCallback.updateImportVideoTextView(videoImportTextView);
                    break;

                case R.id.btnImport:
                    dialogCallback.saveImportVideo((Categorie) spinnerSubCategorie.getSelectedItem());
                    dialog.cancel();
                    break;

                case R.id.btnCancelImport:
                    dialog.cancel();
                    break;
            }
        }
    };

    /**
     * listener d'écoute pour la sélection d'un item catégorie
     */
    public AdapterView.OnItemSelectedListener catItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

            // Here you get the current item that is selected by its position
            currentCategorie = (Categorie) adapterView.getItemAtPosition(position);


            spinnerAdapter2.clear();
            spinnerAdapter2.addAll(Util.setSubCatSpinnerList(currentCategorie.getPath()));
            spinnerAdapter2.notifyDataSetChanged();
            spinnerSubCategorie.setSelection(1);
            Log.e("SELECT_CAT", currentCategorie.getPath());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapter) {
        }
    };
}