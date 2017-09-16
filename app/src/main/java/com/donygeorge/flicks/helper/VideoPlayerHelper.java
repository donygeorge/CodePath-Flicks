package com.donygeorge.flicks.helper;

import android.app.Activity;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.models.Video;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoPlayerHelper {

    public static interface Callback {
        abstract void onSuccess(YouTubePlayer player, Video video);
        abstract void onFailure();
    }

    public static void queryAndPrepareVideo(final int movieId, final Activity activity, final YouTubePlayerView playerView, final Callback callback) {
        APIHelper.queryVideos(movieId, new APIHelper.APICallback() {
            @Override
            public void onSuccess(JSONObject object) {
                JSONArray videoJSONResults = null;
                try {
                    videoJSONResults = object.getJSONArray("results");
                    final ArrayList<Video> videos = Video.fromJSONArray(movieId, videoJSONResults);
                    if (videos.size() > 0) {
                        prepareVideo(videos.get(0), activity, playerView, callback);
                    } else {
                        callback.onFailure();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    private static void prepareVideo(final Video video, final Activity activity, final YouTubePlayerView playerView, final Callback callback) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerView.initialize(activity.getString(R.string.youtube_api_key),
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                YouTubePlayer youTubePlayer, boolean b) {
                                callback.onSuccess(youTubePlayer, video);
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult youTubeInitializationResult) {
                                callback.onFailure();
                            }
                        });
            }
        });
    }

}
