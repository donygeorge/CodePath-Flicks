package com.donygeorge.flicks.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.adapters.MovieArrayAdapter;
import com.donygeorge.flicks.helper.APIHelper;
import com.donygeorge.flicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    @BindView(R.id.moviesListView) ListView movieslistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        movieslistview.setAdapter(movieArrayAdapter);

        APIHelper.queryMovies(new APIHelper.APICallback() {
            @Override
            public void onSuccess(JSONObject object) {
                JSONArray movieJSONResults = null;
                try {
                    movieJSONResults = object.getJSONArray("results");
                    final ArrayList<Movie> movies = Movie.fromJSONArray(movieJSONResults);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MovieListActivity.this.movies.addAll(movies);
                            movieArrayAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorDialog();
                }
            }

            @Override
            public void onFailure() {
                showErrorDialog();
            }

            private void showErrorDialog() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(MovieListActivity.this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Could not retrieve list of movies");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do not do anything yet
                                    }
                                });
                        alertDialog.show();
                    }
                });
            }
        });
    }
}
