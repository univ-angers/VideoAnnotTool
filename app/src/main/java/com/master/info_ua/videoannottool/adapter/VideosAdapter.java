package com.master.info_ua.videoannottool.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.master.info_ua.videoannottool.R;
import com.master.info_ua.videoannottool.custom.Video;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends ArrayAdapter<Video> {
    protected int selectedListItem=0;
    private TextView tvName;
    private ImageView ivName;

    public VideosAdapter(Context context, List<Video> videos) {
        super(context, 0, videos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Video video = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_video, parent, false);
        }
        // Lookup view for data population
        tvName = convertView.findViewById(R.id.tvName);
        ivName=convertView.findViewById(R.id.ivName);
        //ImageView moviePicture = convertView.findViewById(R.id.moviePicture);
        //TextView tvHome = convertView.findViewById(R.id.tvAuthor);
        // Populate the data into the template view using the data object
        tvName.setText(video.getName());
        /*moviePicture.setImageResource(R.drawable.movie);*/
        // Return the completed view to render on screen
        if(getContext().getExternalFilesDir(video.getPath()+"/"+"vignette.png").exists())
        {
            System.out.println(getContext().getExternalFilesDir(video.getPath()+"/"+"vignette.png").exists() + " bien "+ getContext().getExternalFilesDir(video.getPath()+"/"+"vignette.png").getAbsolutePath());
            Bitmap bitmap = BitmapFactory.decodeFile(getContext().getExternalFilesDir(video.getPath()+"/"+"vignette.png").toString());
            ivName.setImageBitmap(bitmap);
        }
        if (position == selectedListItem){
            convertView.setBackgroundResource(android.R.color.darker_gray);
        }
        else {
            convertView.setBackgroundResource(android.R.color.transparent);
        }
        return convertView;
    }

    public void setSelectedListItem(int position){
        selectedListItem=position;
    }

}

