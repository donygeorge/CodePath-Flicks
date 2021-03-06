package com.donygeorge.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;

import com.donygeorge.flicks.R;
import com.donygeorge.flicks.adapters.MovieArrayAdapter;
import com.donygeorge.flicks.helper.APIHelper;
import com.donygeorge.flicks.helper.Constants;
import com.donygeorge.flicks.helper.ErrorDialogHelper;
import com.donygeorge.flicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    @BindView(R.id.movies_list_view) ListView movieslistview;

    // Infine scrolling
    ReentrantReadWriteLock lock;
    boolean loading; // only accessed while lock is held
    int page; // only accessed while lock is held
    int totalPages; // only accessed while lock is held

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        movieslistview.setAdapter(movieArrayAdapter);

        lock = new ReentrantReadWriteLock();
        loading = false;
        page = 0;
        totalPages = 1;

        movieslistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                // Ignore
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount + Constants.visibleThreshold) >= totalItemCount) {
                    queryMovies();
                }
            }
        });
    }

    private void queryMovies() {
        lock.readLock().lock();
        if (loading || page >= totalPages) {
            lock.readLock().unlock();
            return;
        }
        lock.readLock().unlock();
        setLoading(true);
        APIHelper.queryMovies(++page, new APIHelper.APICallback() {
            @Override
            public void onSuccess(JSONObject object) {
                JSONArray movieJSONResults = null;
                try {
                    movieJSONResults = object.getJSONArray("results");
                    final ArrayList<Movie> movies = Movie.fromJSONArray(movieJSONResults);
                    final int pages = object.getInt("total_pages");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MovieListActivity.this.movies.addAll(movies);
                            movieArrayAdapter.notifyDataSetChanged();
                            lock.writeLock().lock();
                            loading = false;
                            totalPages = pages;
                            lock.writeLock().unlock();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    setLoading(false);
                    showErrorDialog();
                }
            }

            @Override
            public void onFailure() {
                setLoading(false);
                showErrorDialog();
            }

            private void showErrorDialog() {
                ErrorDialogHelper.showDialog(MovieListActivity.this, "Could not retrieve list of movies");
            }
        });
    }

    private void setLoading(boolean value) {
        lock.writeLock().lock();
        loading = value;
        lock.writeLock().unlock();
    }
}
