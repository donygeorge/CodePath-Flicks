package com.donygeorge.flicks.activities;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.helper.Constants;
import com.donygeorge.flicks.helper.VideoPlayerHelper;
import com.donygeorge.flicks.models.Video;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends YouTubeBaseActivity {
    @BindView(R.id.detailsVideoPlayer)
    YouTubePlayerView videoPlayer;
    @BindView(R.id.detailsTitleTextView)
    TextView titleTextView;
    @BindView(R.id.detailReleaseTextView)
    TextView releaseDateTextView;
    @BindView(R.id.detailSummaryTextView)
    TextView overviewTextView;
    @BindView(R.id.detailsRatingBar)
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        final int movieId = getIntent().getIntExtra(Constants.movieId, -1);
        String movieTitle = getIntent().getStringExtra(Constants.movieTitle);
        String movieOverview = getIntent().getStringExtra(Constants.movieOverview);
        String movieReleaseDate = getIntent().getStringExtra(Constants.movieReleaseDate);
        double movieStars = getIntent().getDoubleExtra(Constants.movieStars, 0);
        if (movieId == -1) {
            // Should not happen
            exitActivity();
        }

        titleTextView.setText(movieTitle);
        overviewTextView.setText(movieOverview);
        releaseDateTextView.setText(getString(R.string.release_date_prefix) + movieReleaseDate);
        ratingBar.setRating((float)movieStars / 2);

        VideoPlayerHelper.queryAndPrepareVideo(movieId, this, videoPlayer, new VideoPlayerHelper.Callback() {
            @Override
            public void onSuccess(YouTubePlayer player, Video video) {
                player.cueVideo(video.getKey());
            }

            @Override
            public void onFailure() {
                exitActivity();
            }
        });

    }

    private void exitActivity() {
        // TODO: Add an error alert
        finish();
    }
}
