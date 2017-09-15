package com.donygeorge.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.helper.SingleHttpClient;
import com.donygeorge.flicks.models.Movie;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    static class ViewHolder {
        @BindView(R.id.movieImageView) ImageView imageView;
        @Nullable @BindView(R.id.titleTextView) TextView titleTextView;
        @Nullable @BindView(R.id.descTextView) TextView descTextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private enum StoryTypes {
        POPULAR, NONPOPULAR
    }

    public MovieArrayAdapter(Context context, List<Movie> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag
        int orientation = getContext().getResources().getConfiguration().orientation;
        int type = getItemViewType(position, orientation);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = getInflatedLayoutForType(type, parent);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder.titleTextView != null) {
            viewHolder.titleTextView.setText(movie.getTitle());
        }
        if (viewHolder.descTextView != null) {
            viewHolder.descTextView.setText(movie.getOverview());
        }
        viewHolder.imageView.setImageResource(0);
        String imageURL = null;
        int placeholderID = 0;
        boolean loadPoster = false;
        if (orientation == Configuration.ORIENTATION_PORTRAIT && type == StoryTypes.NONPOPULAR.ordinal()) {
            loadPoster = true;
        }

        imageURL = loadPoster ? movie.getPosterURL() : movie.getBackdropURL();
        placeholderID = loadPoster ? R.drawable.loading_portrait : R.drawable.loading_land;
        Picasso picasso = new Picasso.Builder(getContext()).downloader(new OkHttp3Downloader(SingleHttpClient.getInstance())).build();
        picasso.with(getContext())
                .load(imageURL)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(10, 10))
                .placeholder(placeholderID)
                .into(viewHolder.imageView);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        int orientation = getContext().getResources().getConfiguration().orientation;
        return getItemViewType(position, orientation);
    }

    @Override
    public int getViewTypeCount() {
        return StoryTypes.values().length;
    }

    private int getItemViewType(int position, int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return StoryTypes.NONPOPULAR.ordinal();
        }
        return (getItem(position).isPopular() ? StoryTypes.POPULAR : StoryTypes.NONPOPULAR).ordinal();
    }

    private View getInflatedLayoutForType(int type, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (type == StoryTypes.POPULAR.ordinal()) {
            return inflater.inflate(R.layout.item_popular_movie, parent, false);
        } else if (type == StoryTypes.NONPOPULAR.ordinal()) {
            return inflater.inflate(R.layout.item_movie, parent, false);
        } else {
            return null;
        }
    }
}
