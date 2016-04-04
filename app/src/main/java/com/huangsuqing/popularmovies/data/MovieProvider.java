package com.huangsuqing.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Suqing on 3/30/16.
 */
public class MovieProvider extends ContentProvider {

    // the URI Matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int MOVIE_LIST = 1;
    private static final int MOVIE_DETAIL = 2;

    private MovieDBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        mDBHelper = new MovieDBHelper(getContext());
        mDatabase = mDBHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        if(sortOrder == null)
            sortOrder = MovieContract.MovieEntry.COLUMN_ID;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_LIST: {
                cursor = mDatabase.query(
                        MovieContract.MovieEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder
                );
                break;
            }
            case MOVIE_DETAIL: {
                cursor = mDatabase.query(
                        MovieContract.MovieEntry.TABLE_NAME, projection,
                        MovieContract.MovieEntry.COLUMN_ID + " = ?",
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder
                );
                break;
            }
            default: throw new UnsupportedOperationException("Not yet implemented");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not needed!");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_LIST: {
                count = mDatabase.delete(MovieContract.MovieEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case MOVIE_DETAIL: {
                count = mDatabase.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_ID + " = ?", selectionArgs);
                break;
            }
            default: throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        long id = mDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        if(id > 0) {
            returnUri = ContentUris.withAppendedId(MovieContract.BASE_CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // for each type of URI want to add, create a corresponding code
        matcher.addURI(authority, "movies", MOVIE_LIST);
        matcher.addURI(authority, "#", MOVIE_DETAIL);

        return matcher;
    }

}
