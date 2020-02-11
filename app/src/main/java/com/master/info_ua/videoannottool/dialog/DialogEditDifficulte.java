package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.custom.Video;
import com.master.info_ua.videoannottool.util.Util;

public class DialogEditDifficulte {
    private MainActivity context;
    private Dialog dialog;
    private EditDifficulteDialogListener dialogListener;
    private Video video;
    private int difficulte;
    private Spinner spinner;
    private Button btnValid;

    public DialogEditDifficulte(final MainActivity context, Video video) {
        this.context = context;
        this.video = video;
        dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.boite_dialog_edit_difficulte);
        dialog.setCancelable(true);
        dialog.setTitle("Modifier la difficulté");
        spinner = dialog.findViewById(R.id.spinnerEditDifficulte);
        btnValid = dialog.findViewById(R.id.btnValidEditDiff);
        if (context instanceof EditDifficulteDialogListener) {
            dialogListener = context;
        }
        //Initialisation du spinner de difficulté
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(context, R.array.difficultes_import, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(difficulteSelectedListener);

    }


    public AdapterView.OnItemSelectedListener difficulteSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch(adapterView.getItemAtPosition(i).toString()) {
                case "Niveau 1" : difficulte = 1; break;
                case "Niveau 2" : difficulte = 2; break;
                case "Niveau 3" : difficulte = 3; break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int btnId = v.getId();
            if (btnId == R.id.btnValidEditDiff) {
                dialogListener.onSaveEditDifficulte(video, difficulte);
                Util.FermerClavier(v);
                dialog.dismiss();
                Toast.makeText(context, R.string.editValidateToast, Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void showDialogEditDiff() {
        dialog.show();
        btnValid.setOnClickListener(clickListener);
    }


    public interface EditDifficulteDialogListener {
        void onSaveEditDifficulte(Video video, int d);
    }

}
