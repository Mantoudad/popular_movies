package com.huangsuqing.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Suqing on 3/30/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.huangsuqing.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME = "display_name";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE = "released_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_BACKDROP = "backdrop_url";
        public static final String COLUMN_POSTER = "poster_url";
        public static final String COLUMN_ID = "_id";
    }

}
