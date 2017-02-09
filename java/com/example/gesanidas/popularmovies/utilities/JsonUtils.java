package com.example.gesanidas.popularmovies.utilities;

import android.content.Context;

import com.example.gesanidas.popularmovies.Movie;
import com.example.gesanidas.popularmovies.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by gesanidas on 1/16/2017.
 */

public class JsonUtils
{
    public static String[] getStringsFromJson(Context context, String inputJsonStr) throws JSONException
    {
        final String MOVIE_LIST="results";
        final String POSTER_PATH="poster_path";
        final String ORIGINAL_TITLE="original_title";
        final String OVERVIEW="overview";
        final String USER_RATING="vote_average";
        final String RELEASE_DATE="release_date";

        JSONObject jsonObject=new JSONObject(inputJsonStr);
        JSONArray jsonArray=jsonObject.getJSONArray(MOVIE_LIST);

        String parsedMovieData[]=new String[jsonArray.length()];

        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject movieJsonObject=jsonArray.getJSONObject(i);
            String poster_path,original_title,overview,user_rating,release_date;
            poster_path=movieJsonObject.getString(POSTER_PATH);
            original_title=movieJsonObject.getString(ORIGINAL_TITLE);
            overview=movieJsonObject.getString(OVERVIEW);
            user_rating=movieJsonObject.getString(USER_RATING);
            release_date=movieJsonObject.getString(RELEASE_DATE);
            parsedMovieData[i]=poster_path+original_title+overview+user_rating+release_date;

        }
        return parsedMovieData;


    }


    public static Movie[] getMoviesFromJson(Context context, String inputJsonStr) throws JSONException
    {
        final String MOVIE_LIST="results";
        final String POSTER_PATH="poster_path";
        final String ORIGINAL_TITLE="original_title";
        final String OVERVIEW="overview";
        final String USER_RATING="vote_average";
        final String RELEASE_DATE="release_date";
        final String ID="id";

        JSONObject jsonObject=new JSONObject(inputJsonStr);
        JSONArray jsonArray=jsonObject.getJSONArray(MOVIE_LIST);

        Movie[] parsedMovieData=new Movie[jsonArray.length()];

        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject movieJsonObject=jsonArray.getJSONObject(i);
            String poster_path,original_title,overview,user_rating,release_date,id;
            poster_path=movieJsonObject.getString(POSTER_PATH);
            original_title=movieJsonObject.getString(ORIGINAL_TITLE);
            overview=movieJsonObject.getString(OVERVIEW);
            user_rating=movieJsonObject.getString(USER_RATING);
            release_date=movieJsonObject.getString(RELEASE_DATE);
            id=movieJsonObject.getString(ID);
            parsedMovieData[i] =new Movie(poster_path,original_title,overview,user_rating,release_date,id);

        }
        return parsedMovieData;


    }



    public static HashMap<String,String>  getTrailersFromJson(String inputJsonStr) throws JSONException
    {

        HashMap<String, String> hashMap = new HashMap<>();


        final String VIDEO_LIST="results";
        final String NAME="name";
        final String TRAILER_KEY="key";
        JSONObject jsonObject=new JSONObject(inputJsonStr);
        JSONArray jsonArray=jsonObject.getJSONArray(VIDEO_LIST);
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject videoJsonObject=jsonArray.getJSONObject(i);
            String trailer_key=videoJsonObject.getString(TRAILER_KEY);
            String name=videoJsonObject.getString(NAME);
            hashMap.put(name,trailer_key);
        }

        return hashMap;

    }


    public static HashMap<String,String>  getReviewsFromjSON(String inputJsonStr) throws JSONException
    {

        HashMap<String, String> hashMap = new HashMap<>();


        final String REVIEWS="results";
        final String AUTHOR="author";
        final String CONTENT="content";
        JSONObject jsonObject=new JSONObject(inputJsonStr);
        JSONArray jsonArray=jsonObject.getJSONArray(REVIEWS);
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject videoJsonObject=jsonArray.getJSONObject(i);
            String content=videoJsonObject.getString(CONTENT);
            String author=videoJsonObject.getString(AUTHOR);
            hashMap.put(author,content);
        }

        return hashMap;

    }


    public static HashMap<String,String> retrieveTrailers(Movie movie)
    {
        HashMap<String, String> hashMap = new HashMap<>();


            String id=movie.getId();
            String source= id+"/videos";
            URL url= NetworkUtils.buildUrl(source);
            String response=null;
            try
            {
                response=NetworkUtils.getResponseFromHttpUrl(url);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                hashMap= getTrailersFromJson(response);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            return hashMap;
    }




    public static HashMap<String,String> retrieveReviews(Movie movie)
    {
        HashMap<String, String> hashMap = new HashMap<>();


        String id=movie.getId();
        String source= id+"/reviews";
        URL url= NetworkUtils.buildUrl(source);
        String response=null;
        try
        {
            response=NetworkUtils.getResponseFromHttpUrl(url);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            hashMap=getReviewsFromjSON(response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return hashMap;
    }


}
