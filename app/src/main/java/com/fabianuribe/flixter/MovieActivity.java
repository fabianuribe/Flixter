package com.fabianuribe.flixter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fabianuribe.flixter.adapters.MovieArrayAdapter;
import com.fabianuribe.flixter.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    private final int DETAIL_ACTIVIRY_REQUEST_CODE = 1;

    private ListView lvItems;
    private ArrayList<Movie> movies;
    private MovieArrayAdapter movieAdapter;
    private SwipeRefreshLayout swipeContainer;
    private static Parcelable lvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Check for Saved States
        if (savedInstanceState != null) {
            lvState = savedInstanceState.getParcelable("lvScrollState");
        }

        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);

        lvItems = (ListView) findViewById(R.id.lvMovies);
        lvItems.setAdapter(movieAdapter);

        fetchMoviesAsync();

        // Set up Refresh Listener
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Clear the current ListView scroll position to avoid scrolling to it on refresh
                lvState = null;
                fetchMoviesAsync();
            }
        });

        setupListViewListener();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the ListView current scroll position
        savedInstanceState.putParcelable("lvScrollState", lvItems.onSaveInstanceState());
        super.onSaveInstanceState(savedInstanceState);
    }


    private void fetchMoviesAsync() {
        AsyncHttpClient client = new AsyncHttpClient();
        // TODO: Hide API Key somewhere safe
        String apiKey = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
        String apiUrl = String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=%s", apiKey);

        client.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;
                try {
                    // Parse JSON response
                    movieJsonResults = response.getJSONArray("results");

                    // Clean up in case this is a refresh
                    movies.clear();

                    // Add all the Movie items
                    movies.addAll(Movie.fromJsonArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();

                    // Apply ListView scroll position if exists
                    if (lvState != null) {
                        lvItems.onRestoreInstanceState(lvState);
                    }

                    // Remove refreshing animation
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                    Movie selectedMovie = movies.get(pos);
                    Intent i = new Intent(MovieActivity.this, DetailActivity.class);

                    i.putExtra("movie", selectedMovie);
                    i.putExtra("moviePos", pos);

                    startActivityForResult(i, DETAIL_ACTIVIRY_REQUEST_CODE);
                }
            }
        );
    }
}
