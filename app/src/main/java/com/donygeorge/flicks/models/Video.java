package com.donygeorge.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Video {
    public int getMovieId() {
        return movieId;
    }

    public String getKey() {
        return key;
    }

    int movieId;
    String key;

    public Video(int movieId, JSONObject jsonVideo) throws JSONException {
        this.movieId = movieId;
        this.key = jsonVideo.getString("key");
    }

    public static ArrayList<Video> fromJSONArray(int movieId, JSONArray jsonArray) {
        ArrayList<Video> results = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i ++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                results.add(new Video(movieId, object));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
