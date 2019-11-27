package com.master.info_ua.videoannottool.dialog;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.custom.Video;

public class DialogEditDifficulte {
    private MainActivity context;
    private Dialog dialog;
    private EditDifficulteDialogListener dialogListener;
    private Video video;
    private int difficulte;
    private Spinner spinner;
    private Button btnValid;

    public DialogEditDifficulte(MainActivity context, Video video) {
        this.context = context;
        this.video = video;
        dialog = new Dialog(this.context);
        dialogListener = new EditDifficulteDialogListener() {
            @Override
            public void onSaveEditDifficulte(Video video, String title) {

            }
        };
        dialog.setContentView(R.layout.boite_dialog_edit_difficulte);
        dialog.setCancelable(true);
        dialog.setTitle("Modifier la difficulté");
        spinner = dialog.findViewById(R.id.spinnerEditDifficulte);
        btnValid = dialog.findViewById(R.id.btnValidEditDiff);
    }


    public void showDialogEditDiff() {
        dialog.show();
        btnValid.setOnClickListener(clickListener);
    }

    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int btnId = v.getId();
            if (btnId == R.id.btnValidEditDiff) {
                String title = video.getFileName() + difficulte;
                dialogListener.onSaveEditDifficulte(video, title);
                dialog.dismiss();
                Toast.makeText(context, R.string.editValidateToast, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public interface EditDifficulteDialogListener {
        void onSaveEditDifficulte(Video video, String title);
    }

}
