package com.master.info_ua.videoannottool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.annotation.Annotation;

import java.util.List;

public class AnnotationsAdapter extends ArrayAdapter<Annotation> {
    public AnnotationsAdapter(Context context, List<Annotation> annotations) {
        super(context, 0, annotations);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Annotation annotation = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_annotation, parent, false);
        }
        // Lookup view for data population
        TextView tvTexte = convertView.findViewById(R.id.tvTexte);
        ImageView iconAnnotation = convertView.findViewById(R.id.iconAnnotation);
        // Populate the data into the template view using the data object
        tvTexte.setText(annotation.getAnnotationTitle());
        switch (annotation.getAnnotationType()){
            case TEXT:{
                iconAnnotation.setImageResource(R.drawable.text_editor_32);
                break;
            }
            case DRAW:{
                iconAnnotation.setImageResource(R.drawable.draw_pencil_32);
                break;
            }
            case AUDIO:{
                iconAnnotation.setImageResource(R.drawable.microphone_32);
                break;
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
