package com.huangsuqing.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Suqing on 4/2/16.
 */
public class TrailerAdapter extends BaseAdapter {

    private Context mContext;
    public List<Trailer> trailers;

    public TrailerAdapter(List<Trailer> trailers, Context mContext) {
        this.trailers = trailers;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View trailerRow;
        if(convertView == null) {
            trailerRow = View.inflate(mContext, R.layout.trailer_list_item, null);
        } else {
            trailerRow = convertView;
        }
        trailerRow.setId(1000+position);
        ((TextView)trailerRow.findViewById(R.id.trailer_label)).setText(trailers.get(position).getLabel());
        Picasso.with(mContext).load("http://img.youtube.com/vi/" + trailers.get(position).getUrl() + "/hqdefault.jpg")
                .placeholder(R.drawable.placeholder)
                .into((ImageView)trailerRow.findViewById(R.id.trailer_image));
        final String url = trailers.get(position).getUrl();
        trailerRow.findViewById(R.id.trailer_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivityFragment.instance.watchYoutubeVideo(url);
            }
        });
        return trailerRow;
    }

    public void addItem(Trailer trailer) {
            trailers.add(trailer);
    }
}
