package com.huangsuqing.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suqing on 2/15/16.
 */
public class Movie implements Parcelable {

    private String mOriginalTitle;
    private String mImagePath;
    private String mBackdropImagePath;
    private String mOverview;
    private String mUserRating;
    private String mReleaseDate;
    private int id;
    public static boolean cFlag = true;

    public Movie(int id, String mOriginalTitle, String mImagePath, String mBackdropImagePath,
                 String mOverview, String mUserRating, String mReleaseDate) {
        this.id = id;
        this.mOriginalTitle = mOriginalTitle;
        this.mImagePath = mImagePath;
        this.mBackdropImagePath = mBackdropImagePath;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
    }

    public Movie(Movie movie) {
        this.id = movie.getId();
        this.mOriginalTitle = movie.getmOriginalTitle();
        this.mImagePath = movie.getmImagePath();
        this.mBackdropImagePath = movie.getmBackdropImagePath();
        this.mOverview = movie.getmOverview();
        this.mUserRating = movie.getmUserRating();
        this.mReleaseDate = movie.getmReleaseDate();
    }

    private Movie(Parcel in) {
        id = in.readInt();
        mOriginalTitle = in.readString();
        mImagePath = in.readString();
        mBackdropImagePath = in.readString();
        mOverview = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mOriginalTitle);
        dest.writeString(mImagePath);
        dest.writeString(mBackdropImagePath);
        dest.writeString(mOverview);
        dest.writeString(mUserRating);
        dest.writeString(mReleaseDate);
    }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    public int getId() {
        return id;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public String getmBackdropImagePath() {
        return mBackdropImagePath;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmUserRating() {
        return mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public void setmBackdropImagePath(String mBackdropImagePath) {
        this.mBackdropImagePath = mBackdropImagePath;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setmUserRating(String mUserRating) {
        this.mUserRating = mUserRating;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mImagePath='" + mImagePath + '\'' +
                ", mBackdropImagePath='" + mBackdropImagePath + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mUserRating='" + mUserRating + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                '}';
    }
}
