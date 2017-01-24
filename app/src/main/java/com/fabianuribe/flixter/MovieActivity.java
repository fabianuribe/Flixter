package com.fabianuribe.flixter;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
    private ArrayList<Movie> movies;
    private MovieArrayAdapter movieAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Set up Refresh Listener
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoviesAsync();
            }
        });

        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);

        ListView lvItems;
        lvItems = (ListView) findViewById(R.id.lvMovies);
        lvItems.setAdapter(movieAdapter);

        fetchMoviesAsync();
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

                    // Remove refreshing animation
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
