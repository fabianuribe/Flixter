package com.fabianuribe.flixter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fabianuribe.flixter.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by uribe on 1/25/17.
 */

public class DetailActivity extends AppCompatActivity {

    private int moviePos;
    private Movie movie;
    private ImageView ivMovieBackdrop ;
    private ImageView ivMoviePoster;
    private ImageView ivPlayIcon;
    private String movieTrailerPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_movie);

        movie = getIntent().getParcelableExtra("movie");
        moviePos = getIntent().getIntExtra("moviePos", 0);

        TextView tvHeadingMovieTitle = (TextView) findViewById(R.id.tvHeadingMovieTitle);
        tvHeadingMovieTitle.setText(movie.getOriginalTitle());

        TextView tvHeadingMovieYear = (TextView) findViewById(R.id.tvHeadingMovieYear);
        tvHeadingMovieYear.setText(String.format("(%s)", movie.getReleaseYear()));

        TextView tvMovieTitle = (TextView) findViewById(R.id.tvMovieTitle);
        tvMovieTitle.setText(movie.getOriginalTitle());

        TextView tvMoviewSynopsis = (TextView) findViewById(R.id.tvMovieSynopsis);
        tvMoviewSynopsis.setText(movie.getOverView());

        ivMovieBackdrop = (ImageView) findViewById(R.id.ivMovieBackdrop);
        ivMovieBackdrop.setClickable(true);
        Picasso.with(getApplicationContext()).load(movie.getBackdropPath()).fit()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(ivMovieBackdrop);

        ivPlayIcon = (ImageView) findViewById(R.id.ivPlayIcon);

        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        Picasso.with(getApplicationContext()).load(movie.getPosterPath())
                .transform(new RoundedCornersTransformation(10, 10))
                .fit()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(ivMoviePoster);

        setUpClickListeners();
    }

    private void setUpClickListeners() {
        if (movie.getYoutubeTrailerSource() == null) {
            fetchYoutubeTrailer();
        } else {
            ivPlayIcon.setVisibility(View.VISIBLE);
        }

        ivMovieBackdrop.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Intent i = new Intent(DetailActivity.this, PlayYoutubeActivity.class);
                    i.putExtra("youtubeSource", movie.getYoutubeTrailerSource());
                    startActivityForResult(i, 1);
                }
            }
        );
    }

    private void fetchYoutubeTrailer() {
        AsyncHttpClient client = new AsyncHttpClient();
        // TODO: Hide API Key somewhere safe
        String apiKey = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

        String apiUrl = String.format(
                "https://api.themoviedb.org/3/movie/%s/trailers?api_key=%s",
                movie.getId(),
                apiKey
        );

        client.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray youtubeVideos = null;
                try {
                    youtubeVideos = response.getJSONArray("youtube");

                    if (youtubeVideos.length() > 0) {
                        // Use the first video on the response
                        JSONObject youtubeVideo = youtubeVideos.getJSONObject(0);
                        String source = youtubeVideo.getString("source");
                        movie.setYoutubeTrailerSource(source);

                        // Make Play Icon visible
                        ivPlayIcon.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
