package com.example.gesanidas.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.gesanidas.popularmovies.data.FavoritesContract;

/**
 * Created by gesanidas on 1/16/2017.
 */

public class Movie implements Parcelable
{
    private String poster_path,original_title,overview,user_rating,release_date,id;

    public Movie(String poster_path, String original_title, String overview, String user_rating, String release_date,String id)
    {
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.overview = overview;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.id=id;
    }


    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(poster_path);
        parcel.writeString(original_title);
        parcel.writeString(overview);
        parcel.writeString(user_rating);
        parcel.writeString(release_date);
        parcel.writeString(id);
    }


    private Movie(Parcel in)
    {
        poster_path = in.readString();
        original_title = in.readString();
        overview = in.readString();
        user_rating = in.readString();
        release_date = in.readString();
        id = in.readString();

    }


    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel parcel)
        {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i)
        {
            return new Movie[i];
        }
    };


    public boolean existsInDb(Context context)
    {

            String[] id = {getId()};
            String[] projection = {FavoritesContract.FavoritesEntry.COLUMN_ID};
            String selection = FavoritesContract.FavoritesEntry.COLUMN_ID;
            Cursor cursor = context.getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, projection, selection + "=?", id, null);
            if (cursor.getCount() == 0)
            {
                Log.i("cursor", "is null");
                return false;
            }
            else
            {
                cursor.moveToFirst();
                DatabaseUtils.dumpCursor(cursor);
                Log.i("curs", String.valueOf(cursor.getCount()));
                Log.i("cursor", cursor.getString(0));
                cursor.close();
                return true;
            }


    }
}
