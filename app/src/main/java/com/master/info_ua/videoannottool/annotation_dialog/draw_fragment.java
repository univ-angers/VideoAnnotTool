package com.master.info_ua.videoannottool.annotation_dialog;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation_dessin.DrawView;


public class draw_fragment extends Fragment {

    private Button b_clear;
    private Button b_cancel;
    private Button b_save;
    private Button b_blue;
    private Button b_red;
    private Button b_yellow;
    private Button b_black;
    private Button b_green;
    private Button b_white;


    private DrawView drawView;

    public draw_fragment() {
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
        View view = inflater.inflate(R.layout.draw_fragment, container, false);

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

        drawView = view.findViewById(R.id.draw_view);

        return view;
    }

    //Listener des boutons
    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int btnId = v.getId();

            switch (btnId){
                case R.id.b_draw_reset:
                    drawView.resetCanvas();
                    break;
                case R.id.b_draw_cancel:
                    drawView.setOnTouchEnable(false);
                    break;
                case R.id.b_draw_save:
                    dialogCallback.onCloseDrawDialog();
                    break;
                case R.id.b_draw_blue:
                    drawView.setColor("blue");
                    break;
                case R.id.b_draw_red:
                    drawView.setColor("red");
                    break;
                case R.id.b_draw_yellow:
                    drawView.setColor("yellow");
                    break;
                case R.id.b_draw_black:
                    drawView.setColor("black");
                    break;
                case R.id.b_draw_green:
                    drawView.setColor("green");
                    break;
                case R.id.b_draw_white:
                    drawView.setColor("white");
                    break;
            }
        }
    };
}
