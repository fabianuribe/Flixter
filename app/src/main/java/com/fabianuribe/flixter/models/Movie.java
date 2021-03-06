package com.fabianuribe.flixter.models;

import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by uribe on 1/23/17.
 */

public class Movie implements Parcelable {
    private String id;
    private String posterPath;
    private String originalTitle;
    private String overView;
    private String backdropPath;
    private String releaseDate;
    private Integer stars;
    private String youtubeTrailerSource;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overView = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.releaseDate = jsonObject.getString("release_date");
        this.stars = calculateStars(jsonObject.getDouble("vote_average"));
        this.youtubeTrailerSource = null;
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i =0; i < array.length(); i ++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public String getId() { return id; }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverView() {
        return overView;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w185%s", posterPath);
    }

    public Integer getStars() { return stars; }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780%s", backdropPath);
    }

    public String getListViewMovieImage(int orientation) {
        return (orientation == Configuration.ORIENTATION_LANDSCAPE) ?
                getBackdropPath() : getPosterPath();
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getReleaseYear() {
        int year = 0;
        try {
            Date parsedDate = parseDateString(getReleaseDate());
            Calendar c = Calendar.getInstance();
            c.setTime(parsedDate);
            year = c.get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return year;
    }

    public String getYoutubeTrailerSource() {
        return youtubeTrailerSource;
    }

    public void setYoutubeTrailerSource(String youtubeTrailerSource) {
        this.youtubeTrailerSource = youtubeTrailerSource;
    }

    private Date parseDateString(String datestring) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
        java.util.Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    private Integer calculateStars(Double voteAverage) {
        Integer stars = 0;
        try {
            stars = (int) Math.round((voteAverage/10)*5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stars;
    }

    // Parcelable implementation
    // ------------------------
    protected Movie(Parcel in) {
        id = in.readString();
        posterPath = in.readString();
        originalTitle = in.readString();
        overView = in.readString();
        backdropPath = in.readString();
        releaseDate = in.readString();
        stars = in.readInt();
        youtubeTrailerSource = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(posterPath);
        dest.writeString(originalTitle);
        dest.writeString(overView);
        dest.writeString(backdropPath);
        dest.writeString(releaseDate);
        dest.writeInt(stars);
        dest.writeString(youtubeTrailerSource);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
