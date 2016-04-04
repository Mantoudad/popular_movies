package com.huangsuqing.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageView headerImage = (ImageView)findViewById(R.id.header_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMovie = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mMovie.getmOriginalTitle());
        Picasso.with(getApplicationContext()).load(mMovie.getmBackdropImagePath()).into(headerImage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
