package com.donygeorge.flicks.activities;

import android.os.Bundle;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.helper.APIHelper;
import com.donygeorge.flicks.models.Video;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        final int movieId = getIntent().getIntExtra("movie_id", -1);
        if (movieId == -1) {
            exitActivity();
        }

        APIHelper.queryVideos(movieId, new APIHelper.APICallback() {
            @Override
            public void onSuccess(JSONObject object) {
                JSONArray videoJSONResults = null;
                try {
                    videoJSONResults = object.getJSONArray("results");
                    final ArrayList<Video> videos = Video.fromJSONArray(movieId, videoJSONResults);
                    if (videos.size() > 0) {
                        playVideo(videos.get(0));
                    } else {
                        exitActivity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    exitActivity();
                }
            }

            @Override
            public void onFailure() {
                exitActivity();
            }
        });
    }

    private void playVideo(final Video video) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fullVideoPlayer.initialize(getString(R.string.youtube_api_key),
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                YouTubePlayer youTubePlayer, boolean b) {
                                youTubePlayer.loadVideo(video.getKey());
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult youTubeInitializationResult) {
                                exitActivity();
                            }
                        });
            }
        });
    }

    private void exitActivity() {
        // TODO: Add an error alert
        finish();
    }
}
