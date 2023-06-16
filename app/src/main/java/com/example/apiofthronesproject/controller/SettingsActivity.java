package com.example.apiofthronesproject.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.apiofthronesproject.R;
import com.example.apiofthronesproject.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Habilita la flechita en la parte superior para volver atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Se inserta el Fragment ya creado en la clase 'SettingsFragment'
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }
}