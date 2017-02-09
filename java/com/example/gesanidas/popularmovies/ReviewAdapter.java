package com.example.gesanidas.popularmovies;

/**
 * Created by gesanidas on 1/28/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;



public class ReviewAdapter extends BaseAdapter
{
    private ArrayList<String> authors;
    private ArrayList<String> content;
    private Context mContext;
    final HashMap hashMap;


    public ReviewAdapter(Context mContext, HashMap hashMap)
    {
        this.mContext = mContext;
        this.hashMap=hashMap;
        this.authors=new ArrayList<>(hashMap.keySet());
        this.content=new ArrayList<>(hashMap.values());


    }

    @Override
    public int getCount()
    {
        return authors.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view= inflater.inflate(R.layout.review_item, viewGroup, false);

        TextView textView=(TextView)view.findViewById(R.id.author_textview);
        TextView textView1=(TextView)view.findViewById(R.id.content_textview);
        textView.setText(authors.get(i));
        textView1.setText(content.get(i));



        return view;

    }
}
