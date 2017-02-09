package com.example.gesanidas.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
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
import com.example.gesanidas.popularmovies.R;

import java.util.List;



public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.MovieCursorAdapterViewHolder>
{

    private Cursor mCursor;
    private Context mContext;
    private final ListItemClickListener  mOnClickListener;



    public interface ListItemClickListener
    {
        void onClick(int id);
    }


    public MovieCursorAdapter(Context context,ListItemClickListener listItemClickListener)
    {
        mOnClickListener=listItemClickListener;
        this.mContext=context;
    }

    @Override
    public MovieCursorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context=parent.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieCursorAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieCursorAdapterViewHolder holder, int position)
    {
        mCursor.moveToPosition(position);
        Picasso.with(holder.imageView.getContext()).load("http://image.tmdb.org/t/p/w185/"+mCursor.getString(3)).into(holder.imageView);
    }



    @Override
    public int getItemCount()
    {
        if (mCursor == null)
        {
            return 0;
        }
        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor c)
    {
        if (mCursor == c)
        {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null)
        {
            this.notifyDataSetChanged();
        }
        return temp;
    }



    public class MovieCursorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public final ImageView imageView;


        public MovieCursorAdapterViewHolder(View view)
        {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            int adapterPosition=getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int ID =mCursor.getInt(0);
            mOnClickListener.onClick(ID);
        }

    }
}
