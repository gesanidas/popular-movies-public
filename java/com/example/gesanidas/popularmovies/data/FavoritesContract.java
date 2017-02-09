package com.example.gesanidas.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;



public class FavoritesContract
{


        public static final String AUTHORITY="com.example.gesanidas.popularmovies";
        public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);
        public static final String PATH_FAVORITES="favorites";

        public static final class FavoritesEntry implements BaseColumns
        {
            public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
            public static final String TABLE_NAME = "favorites";
            public static final String COLUMN_ORIGINALTITLE = "original_title";
            public static final String COLUMN_POSTERPATH = "poster_path";
            public static final String COLUMN_OVERVIEW = "overview";
            public static final String COLUMN_USERRATING = "user_rating";
            public static final String COLUMN_RELEASEDATE = "release_date";
            public static final String COLUMN_ID = "id";


            public static Uri buildFavoritesUri(int id)
            {
                return CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
            }
        }
}
