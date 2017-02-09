package com.example.gesanidas.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.gesanidas.popularmovies.data.FavoritesContract;
import com.example.gesanidas.popularmovies.data.MoviesPreferences;
import com.example.gesanidas.popularmovies.utilities.Helpers;
import com.example.gesanidas.popularmovies.utilities.JsonUtils;
import com.example.gesanidas.popularmovies.utilities.NetworkUtils;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener,SharedPreferences.OnSharedPreferenceChangeListener,LoaderManager.LoaderCallbacks<Cursor>,MovieCursorAdapter.ListItemClickListener
{
    private MovieAdapter movieAdapter;
    private MovieCursorAdapter movieCursorAdapter;
    private RecyclerView recyclerView;
    private FetchMovieTask fetchMovieTask;
    private Movie[] movies=null;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private static final int ID_MOVIE_LOADER = 44;
    GridLayoutManager layoutManager;
    int currentPosition;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_movies);
        layoutManager= new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        movieAdapter=new MovieAdapter(movies,MainActivity.this);
        movieCursorAdapter=new MovieCursorAdapter(MainActivity.this,this);


        if (Helpers.isOnline(this) && !Helpers.isFavorite(this))
        {

            fetchMovieTask=new FetchMovieTask();
            fetchMovieTask.execute();
            recyclerView.setAdapter(movieAdapter);
        }
        else
        {
            recyclerView.setAdapter(movieCursorAdapter);
            getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        int currentPosition=layoutManager.findFirstVisibleItemPosition();
        outState.putInt("currentPosition",currentPosition);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        currentPosition=savedInstanceState.getInt("currentPosition");
    }

    @Override
    public void onClick(Movie movie)
    {
        if(Helpers.isOnline(this)&& !Helpers.isFavorite(this))
        {
            Intent intent = new Intent(MainActivity.this,DetailActivity.class).putExtra("movie",movie);
            startActivity(intent);
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED && Helpers.isOnline(this) && !Helpers.isFavorite(this))
        {
            fetchMovieTask=new FetchMovieTask();
            fetchMovieTask.execute();
            recyclerView.setAdapter(movieAdapter);
            PREFERENCES_HAVE_BEEN_UPDATED=false;
        }
        else if(!PREFERENCES_HAVE_BEEN_UPDATED && Helpers.isOnline(this) && !Helpers.isFavorite(this))
        {
            return;
        }
        else
        {
            recyclerView.setAdapter(movieCursorAdapter);
            getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        PREFERENCES_HAVE_BEEN_UPDATED=true;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new AsyncTaskLoader<Cursor>(this)
        {
            Cursor movies=null;


            @Override
            public void onStartLoading()
            {
                if (movies != null)
                {
                    Log.i("tag","delievered");
                    deliverResult(movies);
                }
                else
                {
                    Log.i("tag","not delievered");
                    forceLoad();
                }
            }
            @Override
            public Cursor loadInBackground()
            {


                try
                {
                    movies=getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,null,null,null,null);
                    return movies;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }





            public void deliverResult(Cursor data)
            {
                movies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if (data != null)
        {
            movieCursorAdapter.swapCursor(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        movieCursorAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int id)
    {
        if (!Helpers.isOnline(this) || (Helpers.isOnline(this) && Helpers.isFavorite(this)))
        {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            Uri uriForMovieClicked = FavoritesContract.FavoritesEntry.buildFavoritesUri(id);
            intent.setData(uriForMovieClicked);
            startActivity(intent);
        }


    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]>
    {

        @Override
        protected Movie[] doInBackground(String... strings)
        {

                URL url=null;
                String source= MoviesPreferences.getPreferredSort(MainActivity.this);
                if (!source.equals("favorites"))
                {
                    url= NetworkUtils.buildUrl(source);
                }

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
                    movies=JsonUtils.getMoviesFromJson(MainActivity.this,response);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return movies;



        }

        @Override
        protected void onPostExecute(Movie[] movies)
        {
            super.onPostExecute(movies);
            movieAdapter.setMovies(movies);
            recyclerView.scrollToPosition(currentPosition);
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
