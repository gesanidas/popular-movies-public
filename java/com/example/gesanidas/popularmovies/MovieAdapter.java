package com.example.gesanidas.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gesanidas on 1/16/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>
{

    private Movie[] movies;
    private final ListItemClickListener  mOnClickListener;


    public interface ListItemClickListener
    {
        void onClick(Movie movie);
    }


    public MovieAdapter(Movie[] movies,ListItemClickListener listItemClickListener)
    {
        this.movies=movies;
        mOnClickListener=listItemClickListener;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context=parent.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position)
    {
        Movie movie=movies[position];
        Picasso.with(holder.imageView.getContext()).load("http://image.tmdb.org/t/p/w185/"+movie.getPoster_path()).into(holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        if (movies!=null)
            return movies.length;
        else
            return 0;
    }

    public void setMovies(Movie[] movies)
    {
        this.movies=movies;
        notifyDataSetChanged();
    }



    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public final ImageView imageView;


        public MovieAdapterViewHolder(View view)
        {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies[adapterPosition];
            mOnClickListener.onClick(movie);
        }

    }
}
