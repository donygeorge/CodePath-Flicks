package com.donygeorge.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.adapters.MovieArrayAdapter;
import com.donygeorge.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    ArrayList<Movie> mMovies;
    MovieArrayAdapter mMovieArrayAdapter;
    ListView mMoviesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mMovies = new ArrayList<>();
        mMoviesListView = (ListView)findViewById(R.id.moviesListView);
        mMovieArrayAdapter = new MovieArrayAdapter(this, mMovies);
        mMoviesListView.setAdapter(mMovieArrayAdapter);

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJSONResults = null;
                try {
                    movieJSONResults = response.getJSONArray("results");
                    mMovies.addAll(Movie.fromJSONArray(movieJSONResults));
                    mMovieArrayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }
}
