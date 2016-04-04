package com.huangsuqing.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private List<Movie> mMoviesList = new ArrayList<>();
    private static final String LOG_TAG = "MainActivityFragment";
    private GridView mGridView;
    private MovieGridViewAdapter movieGridViewAdapter;
    public static MainActivityFragment instance;
    public static String sortOrder = "popular";
    public boolean isDualPane = false;
    private boolean setting_cached = false;

    public MainActivityFragment() {
        instance = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView)rootView.findViewById(R.id.grid_view);
        movieGridViewAdapter = new MovieGridViewAdapter(mMoviesList, getContext());
        mGridView.setAdapter(movieGridViewAdapter);
        updateUI(setting_cached);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Movie movie = movieGridViewAdapter.getItem(position);
                if(isDualPane) {
                    android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    DetailActivityFragment detailActivityFragment = DetailActivityFragment.newInstance(movie);
                    ft.replace(R.id.detail_container, detailActivityFragment);
                    ft.commit();
                } else {
                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                    detailIntent.putExtra(Intent.EXTRA_TEXT, (Parcelable) movie);
                    startActivity(detailIntent);
                }
            }
        });

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setGridColumnCount(3);
        else setGridColumnCount(2);

        return rootView;
    }

    public void updateUI(boolean setting_cached) {
        mMoviesList.clear();
        movieGridViewAdapter.clearItems();
        this.setting_cached = setting_cached;
        if(!setting_cached)
            getMovies();
        else
            getFavorites();
    }

    private void getMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String sorting = prefs.getString(getString(R.string.pref_sorting_key),
//                getString(R.string.pref_sorting_default));
        movieTask.execute(sortOrder);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isDualPane = isDuoPaneLayout();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(setting_cached);
    }

    public void getFavorites() {
        // clear items in the old view
        mMoviesList.addAll((new MovieDB()).getFavoriteMovies(getContext().getContentResolver()));
        // because movieGridViewAdapter has the reference of mMoviesList, so the adapter would
        // automatically updated once mMoviesList updated.
        mGridView.setAdapter(movieGridViewAdapter);
    }

    public void setGridColumnCount(int n) {
        ((GridView)mGridView.findViewById(R.id.grid_view)).setNumColumns(n);
    }

    public boolean isDuoPaneLayout() {
        return (getActivity().findViewById(R.id.detail_container) != null);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
                //if(params[0].equals("popular")) {
                    MOVIE_BASE_URL = MOVIE_BASE_URL + params[0] + "?";
                //} else {
                  //  MOVIE_BASE_URL = MOVIE_BASE_URL + "top_rated?";
                //}
                final String API_KEY = "api_key";

                Uri buildUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.API).build();

                URL url = new URL(buildUri.toString());

                Log.v(LOG_TAG, "Built url " + buildUri.toString());

                //Create the request to themoviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // read the input stream into a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if(inputStream == null) {
                    // nothing to do
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                if(builder.length() == 0) {
                    return null;
                }

                movieJsonStr = builder.toString();

//                Log.v(LOG_TAG, "Movie JSON String: " + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> strings) {
            mMoviesList = strings;
//            Log.v(LOG_TAG, "Data return was : " + mMoviesList);

            movieGridViewAdapter = new MovieGridViewAdapter(mMoviesList, getContext());

            mGridView.setAdapter(movieGridViewAdapter);

        }

        private List<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {
            final String TMDB_RESULTS = "results";
            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_IMAGE_SUB_PATH = "poster_path";
            final String TMDB_IMAGE_BACKDROP_PATH = "backdrop_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_USER_RATING = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_ID = "id";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = movieJson.getJSONArray(TMDB_RESULTS);

            List<Movie> result = new ArrayList<>();
            for(int i = 0; i < moviesArray.length(); i++) {

                String originalTitle;
                String subImagePath;
                String subBackdropImagePath;
                String overview;
                String userRating;
                String releaseDate;
                int id;

                JSONObject singleMovie = moviesArray.getJSONObject(i);

                id = singleMovie.getInt(TMDB_ID);
                originalTitle = singleMovie.getString(TMDB_ORIGINAL_TITLE);
                subImagePath = singleMovie.getString(TMDB_IMAGE_SUB_PATH);
                subBackdropImagePath = singleMovie.getString(TMDB_IMAGE_BACKDROP_PATH);
                overview = singleMovie.getString(TMDB_OVERVIEW);
                userRating = singleMovie.getString(TMDB_USER_RATING);
                releaseDate = singleMovie.getString(TMDB_RELEASE_DATE);

                String imagePth = "http://image.tmdb.org/t/p/w500/" + subImagePath.substring(1);
                String backdropImagePath = "http://image.tmdb.org/t/p/w780/" + subBackdropImagePath.substring(1);

                Movie mMovie = new Movie(id, originalTitle, imagePth, backdropImagePath, overview, userRating, releaseDate);

                result.add(mMovie);
            }

            return result;
        }
     }
}
