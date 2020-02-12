package com.master.info_ua.videoannottool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.master.info_ua.videoannottool.util.Categorie;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Categorie> {
    private List<Categorie> categories;

    public SpinnerAdapter(Context context, int resource, List<Categorie> categories) {
        super(context, resource, 0, categories);
        this.categories = categories;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            // First item will be use for hint
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(categories.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(categories.get(position).getName());
        if (position == 0) {
            // Set the hint text color gray
            label.setTextColor(Color.GRAY);
        } else {
            label.setTextColor(Color.BLACK);
        }
        return label;
    }
}
