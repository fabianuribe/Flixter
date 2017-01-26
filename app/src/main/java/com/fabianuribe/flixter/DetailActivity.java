package com.fabianuribe.flixter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.fabianuribe.flixter.models.Movie;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by uribe on 1/25/17.
 */

public class DetailActivity extends AppCompatActivity {

    private int moviePos;
    private Movie movie;
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

        TextView tvMoviewSynopsis = (TextView) findViewById(R.id.tvMovieSynopsis);
        tvMoviewSynopsis.setText(movie.getOverView());

        ImageView ivMovieBackdrop = (ImageView) findViewById(R.id.ivMovieBackdrop);
        Picasso.with(getApplicationContext()).load(movie.getBackdropPath()).fit()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(ivMovieBackdrop);

        ImageView ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        Picasso.with(getApplicationContext()).load(movie.getPosterPath())
                .transform(new RoundedCornersTransformation(10, 10))
                .fit()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(ivMoviePoster);

    }
}
