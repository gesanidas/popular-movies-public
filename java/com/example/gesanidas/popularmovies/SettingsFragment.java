package com.example.gesanidas.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import com.example.gesanidas.popularmovies.data.MoviesPreferences;

/**
 * Created by gesanidas on 1/17/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener
{


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {}




    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        addPreferencesFromResource(R.xml.pref_general);
    }

}
