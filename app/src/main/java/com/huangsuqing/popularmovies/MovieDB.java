package com.huangsuqing.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.huangsuqing.popularmovies.data.MovieContract;
import com.huangsuqing.popularmovies.data.MovieContract.MovieEntry;

import java.util.ArrayList;

/**
 * Created by Suqing on 4/2/16.
 */
public class MovieDB {
    private static final String ATHORITY_URI = "content://" + MovieContract.CONTENT_AUTHORITY;

    public boolean isMovieFavorited(ContentResolver resolver, int id) {
        boolean res = false;
        Cursor cursor = resolver.query(Uri.parse(ATHORITY_URI + "/" + id), null, null, null, null, null);
        if(cursor != null && cursor.moveToNext()) {
            res = true;
            cursor.close();
        }
        return res;
    }

    public void addMovie(ContentResolver contentResolver, Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_NAME, movie.getmOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getmOverview());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movie.getmBackdropImagePath());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getmImagePath());
        values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getmUserRating());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE, movie.getmReleaseDate());
        contentResolver.insert(MovieEntry.CONTENT_URI, values);
    }

    public void removeMovie(ContentResolver contentResolver, int id) {
        Uri uri = Uri.parse(ATHORITY_URI + "/" + id);
        contentResolver.delete(uri, null, new String[]{id + ""});
    }

    public ArrayList<Movie> getFavoriteMovies(ContentResolver contentResolver) {
        ArrayList<Movie> movies = new ArrayList<>();
        Uri uri = Uri.parse(ATHORITY_URI + "/movies");
        Cursor cursor = contentResolver.query(uri, null, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE)));
                movies.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return movies;
    }
}
