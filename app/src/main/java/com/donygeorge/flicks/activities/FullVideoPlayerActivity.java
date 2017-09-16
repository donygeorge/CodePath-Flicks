package com.donygeorge.flicks.activities;

import android.os.Bundle;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.helper.Constants;
import com.donygeorge.flicks.helper.ErrorDialogHelper;
import com.donygeorge.flicks.helper.VideoPlayerHelper;
import com.donygeorge.flicks.models.Video;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullVideoPlayerActivity extends YouTubeBaseActivity {
    @BindView(R.id.full_video_player)
    YouTubePlayerView fullVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video_player);
        ButterKnife.bind(this);
        final int movieId = getIntent().getIntExtra(Constants.movieId, -1);
        if (movieId == -1) {
            exitActivity();
        }

        VideoPlayerHelper.queryAndPrepareVideo(movieId, this, fullVideoPlayer, new VideoPlayerHelper.Callback() {
                    @Override
                    public void onSuccess(YouTubePlayer player, Video video) {
                        player.loadVideo(video.getKey());
                    }

                    @Override
                    public void onFailure() {
                        exitActivity();
                    }
                });
    }

    private void exitActivity() {
        ErrorDialogHelper.showDialog(this, "Fail to load video");
        finish();
    }
}
