package com.example.gesanidas.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gesanidas on 1/28/2017.
 */

public class TrailerAdapter extends BaseAdapter
{
    private ArrayList<String> names;
    private Context mContext;
    final HashMap hashMap;
    String source;


    public TrailerAdapter(ArrayList<String> names, Context mContext, HashMap hashMap)
    {
        this.names = names;
        this.mContext = mContext;
        this.hashMap=hashMap;
    }

    public String getSource()
    {
        return source;
    }

    @Override
    public int getCount()
    {
        return names.size();
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
        view= inflater.inflate(R.layout.trailer_item, viewGroup, false);
        ImageButton imageButton=(ImageButton)view.findViewById(R.id.trailer_button);
        TextView textView=(TextView)view.findViewById(R.id.trailer_textview);
        textView.setText(names.get(i));
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String key=(String)hashMap.get(names.get(i));
                source="https://www.youtube.com/watch?v="+key;
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(source)));
            }
        });

        return view;

    }




}
