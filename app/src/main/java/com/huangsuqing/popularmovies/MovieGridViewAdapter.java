package com.huangsuqing.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Suqing on 2/14/16.
 */
public class MovieGridViewAdapter extends BaseAdapter {

    private static final String LOG_TAG = MovieGridViewAdapter.class.getSimpleName();
    private List<Movie> mMoviesList;
    private Context mContext;

    public MovieGridViewAdapter(List<Movie> moviesList, Context context) {
        this.mMoviesList = moviesList;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.browse, null);
        ImageView imageView;
        //TextView textView;

        imageView = (ImageView)convertView.findViewById(R.id.thumbnail);
        //textView = (TextView)convertView.findViewById(R.id.title);

        Picasso.with(mContext).load(mMoviesList.get(position).getmImagePath())
                .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(imageView);
        //textView.setText(mMoviesList.get(position).getmOriginalTitle());
        return convertView;
    }

    @Override
    public Movie getItem(int position) {
        return mMoviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return (null != mMoviesList ? mMoviesList.size() : 0);
    }

    public void addItem(Movie movie) {
        mMoviesList.add(movie);
    }

    public void addAll(List<Movie> movies) {
        mMoviesList.addAll(movies);
    }

    public void clearItems() {
        mMoviesList.clear();
    }

}
