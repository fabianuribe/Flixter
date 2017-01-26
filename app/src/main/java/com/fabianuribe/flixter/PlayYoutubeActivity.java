package com.fabianuribe.flixter;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by uribe on 1/26/17.
 */

public class PlayYoutubeActivity extends YouTubeBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_youtube);

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);
        final String youtubeSource = getIntent().getStringExtra("youtubeSource");

        youTubePlayerView.initialize("AIzaSyCCQ0vK11aQrKQS_WmAtNITSq6mn0HmHc0",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(youtubeSource);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                }
        );
    }
}
