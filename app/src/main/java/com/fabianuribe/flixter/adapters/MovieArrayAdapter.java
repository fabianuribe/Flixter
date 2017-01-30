package com.fabianuribe.flixter.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fabianuribe.flixter.R;
import com.fabianuribe.flixter.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static jp.wasabeef.picasso.transformations.RoundedCornersTransformation.CornerType.BOTTOM_LEFT;
import static jp.wasabeef.picasso.transformations.RoundedCornersTransformation.CornerType.TOP_LEFT;

/**
 * Created by uribe on 1/23/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {
        TextView title;
        TextView overview;
        ImageView image;
        LinearLayout rating;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.rating = (LinearLayout) convertView.findViewById(R.id.llRating);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie movie = getItem(position);
        if (movie != null) {
            int orientation = getContext().getResources().getConfiguration().orientation;
            viewHolder.title.setText(movie.getOriginalTitle());
            viewHolder.overview.setText(movie.getOverView());

            if (viewHolder.rating != null) {
                viewHolder.rating.removeAllViews();

                for (int x = 0; x < movie.getStars(); x++) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    ImageView image = new ImageView(getContext());
                    image.setBackgroundResource(R.drawable.ic_star_black_24dp);
                    image.setLayoutParams(layoutParams);
                    image.getLayoutParams().width = 60;
                    image.getLayoutParams().height = 60;
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    image.setColorFilter(new
                            PorterDuffColorFilter(0xffff00, PorterDuff.Mode.MULTIPLY));
                    viewHolder.rating.addView(image);
                }
            }

            Picasso.with(getContext()).load(movie.getListViewMovieImage(orientation))
                    .transform(new RoundedCornersTransformation(10, 4, TOP_LEFT))
                    .transform(new RoundedCornersTransformation(10, 4, BOTTOM_LEFT))
                    .fit()
                    .centerCrop()
                    .into(viewHolder.image);
        }

        return convertView;
    }
}

