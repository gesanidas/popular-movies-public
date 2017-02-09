package com.example.gesanidas.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.gesanidas.popularmovies.data.MoviesPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by gesanidas on 1/28/2017.
 */

public class Helpers
{
    public static  boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected())
        {
            return true;
        }
        else
        {
            Log.v("tag", "Internet Connection Not Present");
            return false;
        }
    }

    public static boolean isFavorite(Context context)
    {
        if(MoviesPreferences.getPreferredSort(context).equals("favorites"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public static ArrayList<String> getNames(HashMap<String,String> hashMap)
    {
        ArrayList<String> names = new ArrayList<>(hashMap.keySet());
        ArrayList<String> toBeReturned = new ArrayList<>();
        for (String name : names)
        {
            if (name.contains("Trailer"))
            {
                toBeReturned.add(name);
            }
            break;
        }
        for (String name : names)
        {
            if (name.contains("Teaser"))
            {
                toBeReturned.add(name);
            }
            break;
        }
        if (toBeReturned.size()<2)
        {
            toBeReturned.clear();
            toBeReturned.add(names.get(0));
            toBeReturned.add(names.get(1));
        }
        if (toBeReturned.size()<1)
        {
            toBeReturned.clear();
            toBeReturned.add(names.get(0));
        }


        return toBeReturned;

    }




}
