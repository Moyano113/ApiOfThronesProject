package com.example.apiofthronesproject.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.apiofthronesproject.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //Se crea el fragment a partir del xml
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
