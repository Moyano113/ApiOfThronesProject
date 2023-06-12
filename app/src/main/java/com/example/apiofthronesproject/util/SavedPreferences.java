package com.example.apiofthronesproject.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SavedPreferences {

    public static boolean notificaciones;
    public static boolean sesion;

    public static void obtenerPreferencias(SharedPreferences preferences, Context c){
        notificaciones = preferences.getBoolean("notificaciones", true);
        sesion = preferences.getBoolean("inicioSesion", true);
    }
}
