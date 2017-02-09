package com.example.gesanidas.popularmovies.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import com.example.gesanidas.popularmovies.R;


/**
 * Created by gesanidas on 1/18/2017.
 */

public class MoviesPreferences
{
    public static String getPreferredSort(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyforSort = context.getString(R.string.sort_key);
        String defaultSort = context.getString(R.string.sort_default);

        return prefs.getString(keyforSort, defaultSort);

    }

}
