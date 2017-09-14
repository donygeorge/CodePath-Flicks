package com.donygeorge.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {

    String posterURL;
    String title;
    String overview;

    public String getPosterURL() {
        return "https://image.tmdb.org/t/p/w342" + posterURL;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Movie(JSONObject jsonMovie) throws JSONException {
        this.posterURL = jsonMovie.getString("poster_path");
        this.title = jsonMovie.getString("original_title");
        this.overview = jsonMovie.getString("overview");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Movie> results = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i ++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                results.add(new Movie(object));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
