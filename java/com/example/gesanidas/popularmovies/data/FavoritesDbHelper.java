package com.example.gesanidas.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class FavoritesDbHelper extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME="favorites.db";
    private static final int DATABASE_VERSION=1;



    public FavoritesDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_FAVORITES_TABLE="CREATE TABLE "+ FavoritesContract.FavoritesEntry.TABLE_NAME+"("+
                FavoritesContract.FavoritesEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,  "+
                FavoritesContract.FavoritesEntry.COLUMN_ORIGINALTITLE+" TEXT NOT NULL,"+
                FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW+" TEXT NOT NULL ,"+
                FavoritesContract.FavoritesEntry.COLUMN_POSTERPATH+" TEXT NOT NULL ,"+
                FavoritesContract.FavoritesEntry.COLUMN_RELEASEDATE+" TEXT NOT NULL ,"+
                FavoritesContract.FavoritesEntry.COLUMN_ID+" TEXT NOT NULL ,"+
                FavoritesContract.FavoritesEntry.COLUMN_USERRATING+" TEXT NOT NULL "+");";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+  FavoritesContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
