package com.example.etudiant.videoannottool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.etudiant.videoannottool.annotation.Annotation;
import com.example.etudiant.videoannottool.R;
import com.example.etudiant.videoannottool.annotation.TextAnnotation;

import java.util.ArrayList;

public class AnnotationsAdapter extends ArrayAdapter<TextAnnotation> {
    public AnnotationsAdapter(Context context, ArrayList<TextAnnotation> annotations) {

        super(context, 0, annotations);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        Annotation annotation = (Annotation)  getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_annotation, parent, false);

        }

        // Lookup view for data population

        TextView tvTexte = (TextView) convertView.findViewById(R.id.tvTexte);


        // Populate the data into the template view using the data object

        tvTexte.setText(annotation.getAnnotationTitle());

        // Return the completed view to render on screen

        return convertView;

    }
}
