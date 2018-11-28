package com.master.info_ua.videoannottool.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation_dessin.DrawView;


public class Fragment_draw extends Fragment {

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
    //Listener des boutons
    View.OnClickListener btnClickListener = new View.OnClickListener() {
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
                    listener.enregistrer_image();
                    listener.fermer_fragment();
                    break;
                case R.id.b_draw_blue:
                    listener.setColor(Color.BLUE);
                    break;
                case R.id.b_draw_red:
                    listener.setColor(Color.RED);
                    break;
                case R.id.b_draw_yellow:
                    listener.setColor(Color.YELLOW);
                    break;
                case R.id.b_draw_black:
                    listener.setColor(Color.BLACK);
                    break;
                case R.id.b_draw_green:
                    listener.setColor(Color.GREEN);
                    break;
                case R.id.b_draw_white:
                    listener.setColor(Color.WHITE);
                    break;
            }
        }
    };

    public interface Listener_fonction
    {
        void resetCanvas();
        void setOnTouchEnable(boolean bool);
        void enregistrer_image();
        void setColor(int color);
        void fermer_fragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
