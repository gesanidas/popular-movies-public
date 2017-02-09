package com.example.gesanidas.popularmovies.utilities;

import com.example.gesanidas.popularmovies.BuildConfig;
import com.example.gesanidas.popularmovies.data.MoviesPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by gesanidas on 1/16/2017.
 */

public class NetworkUtils
{
    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    public final static URL buildUrl(String source)
    {
        URL url=null;
        try
        {
            url=new URL(BASE_URL+source+ BuildConfig.API_KEY);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return url;

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
            {
                return scanner.next();
            }
            else
            {
                return null;
            }
        }
        finally
        {
            urlConnection.disconnect();
        }
    }


}
