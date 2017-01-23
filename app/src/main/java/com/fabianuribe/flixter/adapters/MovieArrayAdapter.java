package com.fabianuribe.flixter.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.fabianuribe.flixter.models.Movie;

import java.util.List;

/**
 * Created by uribe on 1/23/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }
}
