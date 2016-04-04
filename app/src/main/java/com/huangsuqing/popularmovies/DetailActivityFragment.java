package com.huangsuqing.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private ImageView imageView;
    private TextView detailOverview, detailReleaseDate, detailUserRating;
    private Movie mMovie;
    private RequestQueue mRequestQueue;
    public static DetailActivityFragment instance;
    public static final String YOUTUBE_URL_BASE = "https://youtu.be/";
    private FloatingActionButton fab;
    private View rootView;
    private TrailerAdapter trailerAdapter;
    private List<Trailer> trailers;
    private LinearLayout trailersList, reviewsList;
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;

    public DetailActivityFragment() {
        instance = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getArguments() != null) {
            mMovie = getArguments().getParcelable("movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        trailersList = (LinearLayout) rootView.findViewById(R.id.trailer_list);
        reviewsList = (LinearLayout) rootView.findViewById(R.id.review_list);
        trailers = new ArrayList<>();
        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra(intent.EXTRA_TEXT)) {
            mMovie = intent.getParcelableExtra(intent.EXTRA_TEXT);
            Log.v("DetailFragment:", mMovie.toString());
            imageView = (ImageView)rootView.findViewById(R.id.detail_thumbnail);
            detailOverview = (TextView)rootView.findViewById(R.id.detail_overview);
            detailReleaseDate = (TextView)rootView.findViewById(R.id.detail_release_date);
            detailUserRating = (TextView)rootView.findViewById(R.id.detail_user_rating);
            Picasso.with(getContext()).load(mMovie.getmImagePath())
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(imageView);
            detailOverview.setText(mMovie.getmOverview());
            detailReleaseDate.setText(mMovie.getmReleaseDate());
            detailUserRating.setText(mMovie.getmUserRating() + "/10");
            ((RatingBar)rootView.findViewById(R.id.rating)).setRating(Float.parseFloat(mMovie.getmUserRating())/2f);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver contentResolver = getContext().getContentResolver();
                String message;
                MovieDB db = new MovieDB();
                Log.v("DetailActivityFragment:","========Movie id is " + mMovie.getId());
                if (db.isMovieFavorited(contentResolver, mMovie.getId())) {
                    message = "Removed this Movie from Favorites";
                    db.removeMovie(contentResolver, mMovie.getId());
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.fav_off));
                } else {
                    db.addMovie(contentResolver, mMovie);
                    message = "Added this Movie to Favorites";
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.fav_on));
                }

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        trailerAdapter = new TrailerAdapter(trailers, getActivity());
        mRequestQueue = Volley.newRequestQueue(getActivity());

        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        if(mShareActionProvider != null) {
            if(trailerAdapter.trailers.size() > 0) {
                mShareActionProvider.setShareIntent(createVideoShareIntent(YOUTUBE_URL_BASE +
                    trailerAdapter.trailers.get(0).getUrl()));
            } else {
                mShareActionProvider.setShareIntent(createVideoShareIntent("<No Trailers Found>"));
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public String formatDate(String date) {
        String res;
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat dfInput = new SimpleDateFormat("yyyy-MM-dd");
        try {
            res = df.format(dfInput.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            res = date;
        }
        return res;
    }

    private void updateUI() {
        MovieDB db = new MovieDB();
        boolean favStatus = db.isMovieFavorited(getActivity().getContentResolver(), mMovie.getId());
        if(favStatus) {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.fav_on));
        } else fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.fav_off));
        Picasso.with(getContext()).load(mMovie.getmImagePath()).placeholder(R.drawable.placeholder)
                .into((ImageView)rootView.findViewById(R.id.detail_thumbnail));

        // need to get those views again for Tablet layout
        ((TextView)rootView.findViewById(R.id.detail_release_date)).setText(
                formatDate(mMovie.getmReleaseDate()));
        ((RatingBar)rootView.findViewById(R.id.rating)).setRating(Float.parseFloat(mMovie.getmUserRating()) / 2f);
        ((TextView)rootView.findViewById(R.id.detail_overview)).setText(mMovie.getmOverview());
        ((TextView)rootView.findViewById(R.id.detail_user_rating)).setText(mMovie.getmUserRating() + "/10");

        getTrailers(mMovie.getId());
        getReviews(mMovie.getId());
    }

    public static DetailActivityFragment newInstance(Movie newMovie) {
        Bundle args = new Bundle();
        DetailActivityFragment fragment = new DetailActivityFragment();
        args.putParcelable("movie", newMovie);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * use JsonObjectRequest to get trailers
     * @param id
     */
    public void getTrailers(int id) {
        String url = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + BuildConfig.API;
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // add trailers to trailerAdapter
                        try {
                            JSONArray trailers = response.getJSONArray("results");
                            JSONObject trailerObj;
                            for(int i = 0; i < trailers.length(); i++) {
                                trailerObj = trailers.getJSONObject(i);
                                Trailer trailer = new Trailer(
                                        trailerObj.getString("key"),
                                        trailerObj.getString("name"),
                                        trailerObj.getString("id"));
                                Log.d("========", trailer.toString());
                                trailerAdapter.addItem(trailer);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i = 0; i < trailerAdapter.getCount(); i++) {
                            trailersList.addView(trailerAdapter.getView(i, null, null));
                        }
                        if(trailerAdapter.trailers.size() > 0) {
                            mShareActionProvider.setShareIntent(createVideoShareIntent(YOUTUBE_URL_BASE +
                                trailerAdapter.trailers.get(0).getUrl()));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(request);
    }

    public void getReviews(int id) {
        String url = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + BuildConfig.API;
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray reviews = response.getJSONArray("results");
                            JSONObject reviewObj;
                            View view;
                            for(int i = 0; i < reviews.length(); i++) {
                                reviewObj = reviews.getJSONObject(i);
                                Review review = new Review();
                                review.setUrl(reviewObj.getString("url"));
                                review.setAuthor(reviewObj.getString("author"));
                                review.setContent(reviewObj.getString("content"));
                                reviewsList.addView(view = createReviewView(review, i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(request);
    }

    public View createReviewView(Review review, int i) {
        View view = View.inflate(getContext(), R.layout.review_list_item, null);
        ((TextView) view.findViewById(R.id.review_author)).setText(review.getAuthor());
        ((TextView) view.findViewById(R.id.review_content)).setText(review.getContent());
        view.setId(2000+i);
        return view;
    }

    public void watchYoutubeVideo(String url){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + url));
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL_BASE+url));
            startActivity(intent);
        }
    }

    private Intent createVideoShareIntent(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

}
