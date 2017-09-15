package com.donygeorge.flicks.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.adapters.MovieArrayAdapter;
import com.donygeorge.flicks.helper.SingleHttpClient;
import com.donygeorge.flicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class MovieListActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    @BindView(R.id.moviesListView)
    ListView movieslistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        movieslistview.setAdapter(movieArrayAdapter);

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.themoviedb.org/3/movie/now_playing?").newBuilder();
        urlBuilder.addQueryParameter("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed");
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        SingleHttpClient.getInstance().newCall(request).enqueue(
            new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    showErrorDialog();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        showErrorDialog();
                    }

                    JSONArray movieJSONResults = null;
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        movieJSONResults = json.getJSONArray("results");
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
                    }
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
            }
        );
    }
}
