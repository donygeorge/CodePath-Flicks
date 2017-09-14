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

    private static class ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descTextView;
    }

    public MovieArrayAdapter(Context context, List<Movie> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.movieImageView);
            viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.titleTextView);
            viewHolder.descTextView = (TextView)convertView.findViewById(R.id.descTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.titleTextView.setText(movie.getTitle());
        viewHolder.descTextView.setText(movie.getOverview());
        viewHolder.imageView.setImageResource(0);
        Picasso.with(getContext()).load(movie.getPosterURL()).into(viewHolder.imageView);

        return convertView;
    }
}
