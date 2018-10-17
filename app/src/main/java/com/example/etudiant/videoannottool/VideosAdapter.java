package com.example.etudiant.videoannottool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class VideosAdapter extends ArrayAdapter {

    public VideosAdapter(Context context, ArrayList<Video> videos) {

        super(context, 0, videos);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        Video video = (Video)  getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_video, parent, false);

        }

        // Lookup view for data population

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        TextView tvHome = (TextView) convertView.findViewById(R.id.tvAuthor);

        // Populate the data into the template view using the data object

        tvName.setText(video.getName());

        tvHome.setText(video.getAuthor());

        // Return the completed view to render on screen

        return convertView;

    }
}

