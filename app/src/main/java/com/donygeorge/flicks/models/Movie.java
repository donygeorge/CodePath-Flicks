package com.donygeorge.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {

    String posterURL;
    String backdropURL;
    String title;
    String overview;
    String releaseDate;
    double stars;
    int id;

    public double getStars() {
        return stars;
    }

    public String getPosterURL() {
        return "https://image.tmdb.org/t/p/w342" + posterURL;
    }

    public String getBackdropURL() {
        return "https://image.tmdb.org/t/p/w500/" + backdropURL;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public boolean isPopular() {
        return (getStars() > 5);
    }

    public int getId() {
        return id;
    }

    public Movie(JSONObject jsonMovie) throws JSONException {
        this.posterURL = jsonMovie.getString("poster_path");
        this.backdropURL = jsonMovie.getString("backdrop_path");
        this.title = jsonMovie.getString("original_title");
        this.overview = jsonMovie.getString("overview");
        this.releaseDate = jsonMovie.getString("release_date");
        this.stars = jsonMovie.getDouble("vote_average");
        this.id = jsonMovie.getInt("id");
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
