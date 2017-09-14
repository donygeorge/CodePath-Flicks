package com.donygeorge.flicks.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(Context context, List<Movie> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.movieImageView);
        TextView titleTextView = (TextView)convertView.findViewById(R.id.titleTextView);
        TextView descTextView = (TextView)convertView.findViewById(R.id.descTextView);

        titleTextView.setText(movie.getTitle());
        descTextView.setText(movie.getOverview());

        imageView.setImageResource(0);
        Picasso.with(getContext()).load(movie.getPosterURL()).into(imageView);

        return convertView;
    }
}
