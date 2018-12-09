package com.master.info_ua.videoannottool.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.AnnotationType;
import com.master.info_ua.videoannottool.annotation_dessin.DrawView;
import com.master.info_ua.videoannottool.annotation_dialog.DialogDraw;
import com.master.info_ua.videoannottool.util.Util;

import java.io.File;
import java.util.Date;


public class Fragment_draw extends Fragment implements DialogDraw.DrawAnnotDialogListener {

    private Button b_clear;
    private Button b_cancel;
    private Button b_save;
    private Button b_blue;
    private Button b_red;
    private Button b_yellow;
    private Button b_black;
    private Button b_green;
    private Button b_white;

    private Listener_fonction listener;

    private Annotation drawAnnotation;

    public Fragment_draw() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_draw, container, false);

        b_clear = view.findViewById(R.id.b_draw_reset);
        b_clear.setOnClickListener(btnClickListener);

        b_cancel = view.findViewById(R.id.b_draw_cancel);
        b_cancel.setOnClickListener(btnClickListener);

        b_save = view.findViewById(R.id.b_draw_save);
        b_save.setOnClickListener(btnClickListener);

        b_blue = view.findViewById(R.id.b_draw_blue);
        b_blue.setOnClickListener(btnClickListener);

        b_red = view.findViewById(R.id.b_draw_red);
        b_red.setOnClickListener(btnClickListener);
        // couleur par défaut
        b_red.setEnabled(false);

        b_yellow = view.findViewById(R.id.b_draw_yellow);
        b_yellow.setOnClickListener(btnClickListener);

        b_black = view.findViewById(R.id.b_draw_black);
        b_black.setOnClickListener(btnClickListener);

        b_green = view.findViewById(R.id.b_draw_green);
        b_green.setOnClickListener(btnClickListener);

        b_white = view.findViewById(R.id.b_draw_white);
        b_white.setOnClickListener(btnClickListener);

        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof Listener_fonction){
            listener = (Listener_fonction) context;
        }
        else {
            throw new ClassCastException(context.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //Listener des boutons
    protected View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int btnId = v.getId();

            switch (btnId){
                case R.id.b_draw_reset:
                    listener.resetCanvas();
                    break;
                case R.id.b_draw_cancel:
                    listener.setOnTouchEnable(false);
                    listener.fermer_fragment();
                    break;
                case R.id.b_draw_save:
                    listener.setOnTouchEnable(false);
                    String drawFileName = listener.saveDrawImage();
                    setDrawAnnotation(drawFileName);
                    onShowDrawAnnotDialog();
                    //listener.lancement_dialogue();
                    //listener.fermer_fragment();
                    break;
                case R.id.b_draw_blue:
                    listener.setColor(Color.BLUE);
                    setAllEnable();
                    b_blue.setEnabled(false);
                    break;
                case R.id.b_draw_red:
                    listener.setColor(Color.RED);
                    setAllEnable();
                    b_red.setEnabled(false);
                    break;
                case R.id.b_draw_yellow:
                    listener.setColor(Color.YELLOW);
                    setAllEnable();
                    b_yellow.setEnabled(false);
                    break;
                case R.id.b_draw_black:
                    listener.setColor(Color.BLACK);
                    setAllEnable();
                    b_black.setEnabled(false);
                    break;
                case R.id.b_draw_green:
                    listener.setColor(Color.GREEN);
                    setAllEnable();
                    b_green.setEnabled(false);
                    break;
                case R.id.b_draw_white:
                    listener.setColor(Color.WHITE);
                    setAllEnable();
                    b_white.setEnabled(false);
                    break;
            }
        }
    };

    public void setAllEnable(){
        b_red.setEnabled(true);
        b_black.setEnabled(true);
        b_blue.setEnabled(true);
        b_yellow.setEnabled(true);
        b_white.setEnabled(true);
        b_green.setEnabled(true);
    }

    @Override
    public void onSaveDrawImage(String title, int duration) {
        drawAnnotation.setAnnotationTitle(title);
        drawAnnotation.setAnnotationDuration(duration);
        drawAnnotation.setAnnotationDate(new Date());
        listener.enregistrer_image(drawAnnotation);
        //listener.fermer_fragment();
    }

    @Override
    public void onResetCanvas() {

    }

    protected void onShowDrawAnnotDialog(){
        DialogDraw mon_dialogue = new DialogDraw(this);
        mon_dialogue.showDialogDraw();
    }

    protected void setDrawAnnotation(String drawFileName){
        this.drawAnnotation = new Annotation(AnnotationType.DRAW);
        this.drawAnnotation.setDrawFileName(drawFileName);
    }

    public interface Listener_fonction {
        void resetCanvas();
        void setOnTouchEnable(boolean bool);
        String saveDrawImage();
        void enregistrer_image(Annotation annotation);
        void setColor(int color);
        void fermer_fragment();
    }

}
