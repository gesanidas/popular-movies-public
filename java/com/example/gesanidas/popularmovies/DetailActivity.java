package com.example.gesanidas.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.DeadObjectException;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gesanidas.popularmovies.data.FavoritesContract;
import com.example.gesanidas.popularmovies.data.MoviesPreferences;
import com.example.gesanidas.popularmovies.utilities.Helpers;
import com.example.gesanidas.popularmovies.utilities.JsonUtils;
import com.example.gesanidas.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DetailActivity extends AppCompatActivity
{
    TextView titleView,overviewView,user_ratingView,release_dateView;
    ImageView posterView;
    Movie movie;
    ImageButton imageButton;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    String poster_path,original_title,overview,user_rating,release_date,id;
    String trailerSource;

    private Uri mUri;
    public boolean isMarked;


    public static final String[] MOVIE_DETAIL_PROJECTION ={
            FavoritesContract.FavoritesEntry.COLUMN_ID,
            FavoritesContract.FavoritesEntry.COLUMN_POSTERPATH,
            FavoritesContract.FavoritesEntry.COLUMN_USERRATING,
            FavoritesContract.FavoritesEntry.COLUMN_RELEASEDATE,
            FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW,
            FavoritesContract.FavoritesEntry.COLUMN_ORIGINALTITLE

    };

    public static final int INDEX_ID=0;
    public static final int INDEX_POSTERPATH=1;
    public static final int INDEX_USERRATING=2;
    public static final int INDEX_RELEASEDATE=3;
    public static final int INDEX_OVERVIEW=4;
    public static final int INDEX_ORIGINALTITLE=5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleView=(TextView)findViewById(R.id.title);
        overviewView=(TextView)findViewById(R.id.overview);
        user_ratingView=(TextView)findViewById(R.id.user_rating);
        release_dateView=(TextView)findViewById(R.id.release_date);
        posterView=(ImageView)findViewById(R.id.poster);
        imageButton=(ImageButton)findViewById(R.id.favorite_button);




        if(Helpers.isOnline(this) && !Helpers.isFavorite(this))
        {
            movie=getIntent().getParcelableExtra("movie");
            displayMovie(movie);
        }
        else
        {
            mUri = getIntent().getData();
            if (savedInstanceState==null)
            {
                movie=getMovieFromDb(mUri);
                displayMovie(movie);
                Log.i("saved"," null");
            }
            else if(savedInstanceState!=null)
            {
                movie=savedInstanceState.getParcelable("movie");
                Log.i("saved","not null");
                Log.i("saved",movie.getId());
                displayMovie(movie);
            }

        }


        if (existsInDb(movie))
        {
            imageButton.setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            isMarked=true;
        }
        else
        {
            imageButton.setColorFilter(Color.WHITE,PorterDuff.Mode.MULTIPLY);
            isMarked=false;
        }



    }


    public void star(View view)
    {
        if (!isMarked)
        {
            mUri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, getContentValues(movie));
            //Toast.makeText(DetailActivity.this, mUri.toString(), Toast.LENGTH_LONG).show();
            imageButton.setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            Log.i("uri",mUri.toString());
            isMarked=true;
        }
        else
        {

            if(movie==null) movie=getMovieFromDb(mUri);
            if (mUri==null)
            {
                mUri=getUriFromMovie(movie);
            }
            int rows= getContentResolver().delete(mUri, null, null);
            //Toast.makeText(DetailActivity.this,String.valueOf(rows), Toast.LENGTH_LONG).show();
            imageButton.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            isMarked=false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie", movie);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    private Intent createShareForecastIntent()
    {
        if(trailerAdapter!=null)
        {
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText("https://www.youtube.com/watch?v="+trailerSource)
                    .getIntent();
            return shareIntent;
        }
        else
        {
            return null;
        }

    }




    public ContentValues getContentValues (Movie movie)
    {
        ContentValues contentValues = new ContentValues();

        String poster_path,original_title,overview,user_rating,release_date,id;
        poster_path=movie.getPoster_path();
        original_title=movie.getOriginal_title();
        overview=movie.getOverview();
        user_rating=movie.getUser_rating();
        release_date=movie.getRelease_date();
        id=movie.getId();


        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTERPATH,poster_path);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_ORIGINALTITLE,original_title);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW,overview);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_USERRATING,user_rating);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASEDATE,release_date);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_ID,id);

        return contentValues;

    }

    public void displayMovie(Movie movie)
    {
        String poster_path,original_title,overview,user_rating,release_date;
        poster_path=movie.getPoster_path();
        original_title=movie.getOriginal_title();
        overview=movie.getOverview();
        user_rating=movie.getUser_rating();
        release_date=movie.getRelease_date();


        titleView.setText(original_title);
        overviewView.setText(overview);
        user_ratingView.setText(user_rating+"/10");
        release_dateView.setText(release_date);
        Picasso.with(this.posterView.getContext()).load("http://image.tmdb.org/t/p/w185/"+poster_path).into(this.posterView);
        if(Helpers.isOnline(this))
        {
            DisplayTrailer displayTrailer=new DisplayTrailer();
            displayTrailer.execute(movie);
            DisplayReview displayReview=new DisplayReview();
            displayReview.execute(movie);
        }

    }

    public Movie getMovieFromDb(Uri uri)
    {
        Cursor data= getContentResolver().query(uri, MOVIE_DETAIL_PROJECTION, null, null, null);
        data.moveToFirst();
        poster_path=data.getString(INDEX_POSTERPATH);
        original_title=data.getString(INDEX_ORIGINALTITLE);
        overview=data.getString(INDEX_OVERVIEW);
        user_rating=data.getString(INDEX_USERRATING);
        release_date=data.getString(INDEX_RELEASEDATE);
        id=data.getString(INDEX_ID);
        Movie movie=new Movie(poster_path,original_title,overview,user_rating,release_date,id);
        return  movie;
    }

    public Uri getUriFromMovie(Movie movie)
    {

        String[] id = {movie.getId()};
        String[] projection = {FavoritesContract.FavoritesEntry._ID};
        String selection = FavoritesContract.FavoritesEntry.COLUMN_ID;
        Cursor cursor = getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, projection, selection + "=?", id, null);
        cursor.moveToFirst();
        String i=cursor.getString(0);
        Uri uri = FavoritesContract.FavoritesEntry.CONTENT_URI.buildUpon().appendPath(i).build();
        cursor.close();
        return uri;
    }



    public class DisplayTrailer extends  AsyncTask<Movie,Void,HashMap>
    {
        @Override
        protected HashMap doInBackground(Movie... params)
        {
            return JsonUtils.retrieveTrailers(params[0]);
        }

        @Override
        protected void onPostExecute(HashMap hashMap)
        {
            super.onPostExecute(hashMap);
            ArrayList<String> names=new ArrayList<>(hashMap.keySet());
            if (hashMap.keySet().size()!=0)
            {
                trailerSource=(String)hashMap.get(names.get(0));
                trailerAdapter=new TrailerAdapter(names,DetailActivity.this,hashMap);
                LinearLayout linearLayout=(LinearLayout)findViewById(R.id.trailer_item);
                final int adapterCount = trailerAdapter.getCount();
                for (int i = 0; i < adapterCount; i++)
                {
                    View item = trailerAdapter.getView(i, null, null);
                    linearLayout.addView(item);
                }
            }


        }
    }


    public class DisplayReview extends  AsyncTask<Movie,Void,HashMap>
    {
        @Override
        protected HashMap doInBackground(Movie... params)
        {
            return JsonUtils.retrieveReviews(params[0]);
        }

        @Override
        protected void onPostExecute(HashMap hashMap)
        {
            super.onPostExecute(hashMap);

            reviewAdapter=new ReviewAdapter(DetailActivity.this,hashMap);
            LinearLayout reviewLinearLayout=(LinearLayout)findViewById(R.id.review_item);
            final int adapterCount = reviewAdapter.getCount();
            for (int i = 0; i < adapterCount; i++)
            {
                View item = reviewAdapter.getView(i, null, null);
                reviewLinearLayout.addView(item);
            }

        }


    }


    public boolean existsInDb(Movie movie)
    {
        if (movie!=null)
        {
            String[] id = {movie.getId()};
            String[] projection = {FavoritesContract.FavoritesEntry.COLUMN_ID};
            String selection = FavoritesContract.FavoritesEntry.COLUMN_ID;
            Cursor cursor = getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, projection, selection + "=?", id, null);
            if (cursor.getCount() == 0)
            {
                Log.i("cursor", "is null");
                cursor.close();
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
        return false;

    }







}
