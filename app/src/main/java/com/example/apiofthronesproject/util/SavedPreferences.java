package com.example.apiofthronesproject.util;

import android.content.Context;
import android.content.SharedPreferences;
//Clase encargada de recoger los datos que el usuario ha especificado en la ventana de ajustes(preferencias)
public class SavedPreferences {

    public static boolean notificaciones;
    public static boolean sesion;

    public static void obtenerPreferencias(SharedPreferences preferences, Context c){
        notificaciones = preferences.getBoolean("notificaciones", true);
        sesion = preferences.getBoolean("inicioSesion", true);
    }
}
