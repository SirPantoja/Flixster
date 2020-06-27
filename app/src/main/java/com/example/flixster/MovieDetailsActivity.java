package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    TextView tvLang;
    RatingBar rbVoteAverage;
    ImageButton ibBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // Give the variables their values
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvLang = (TextView) findViewById(R.id.tvLang);
        ibBackdrop = (ImageButton) findViewById(R.id.ibBackdrop);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview and Language
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvLang.setText(movie.getLanguage() + ", Popularity: " + movie.getPop() +"%");
        String imageUrl = movie.getBackdropPath();

        // Check if in landscape to adjust size and pick image
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUrl = movie.getBackdropPath();
            ibBackdrop.getLayoutParams().height = 620;
        } else {
            imageUrl = movie.getPosterPath();
            ibBackdrop.getLayoutParams().height = 1300;
        }

        // Round the edges
        int radius = 30; // corner radius, higher value = more rounded
        int margin = 30; // crop margin, set to 0 for corners with no crop
        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().transform(new RoundedCornersTransformation(radius, margin), new RoundedCorners(radius)))
                .into(ibBackdrop);

        // Create the onClick listener and play the video
        ibBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent and use it to embed the Youtube video
                Intent i = new Intent(view.getContext(), MovieTrailerActivity.class);
                i.putExtra("id", movie.getId());
                startActivity(i);
            }
        });

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }
}