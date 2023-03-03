package com.example.apiofthronesproject.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.apiofthronesproject.model.User;

import java.util.ArrayList;

public class CtrlUser {
    Context c;
    User u;
    ArrayList<User> listUser;
    SQLiteDatabase bd;
    String bdName = "LOGIN";

    //Constructor del controlador donde se crea la tabla USER si no habia sido creada antes (IF NOT
    //EXISTS).
    public CtrlUser(@NonNull Context c){
        this.c = c;
        bd = c.openOrCreateDatabase(bdName, c.MODE_PRIVATE, null);
        bd.execSQL("CREATE TABLE IF NOT EXISTS USER(ID INTEGER PRIMARY KEY AUTOINCREMENT, USER TEXT, PASSWORD TEXT)");
        u = new User();
    }

    //Metodo usado para validar el login del usuario, es decir si el usuario y la contraseña
    //coinciden con alguien registrado en la base de datos.
    public int validate(String usuario, String contrasena){
        int key = 1;
        Cursor cr = bd.rawQuery("SELECT * FROM USER", null);
        if(cr != null && cr.moveToFirst()){
            do{
                if(cr.getString(1).equals(usuario) && cr.getString(2).equals(contrasena)){
                    key = 0;
                }
            }while(cr.moveToNext());
        }
        return key;
    }

    //Metodo encargado de devolver un usuario si existe en la base de datos, dado su nombre de
    //usuario.
    public User getUser(String usuario){
        listUser = listAllUsers();
        User us = null;
        for(User u:listUser){
            if(u.getUsuario().equals(usuario)){
                us = u;
            }
        }
        return us;
    }

    //Metodo encargado de devolver un usuario si existe en la base de datos, dado su id.
    public User getUserById(int id){
        listUser = listAllUsers();
        User us = null;
        for(User u:listUser){
            if(u.getId() == id){
                us = u;
            }
        }
        return us;
    }

    //Metodo encargado de dar de alta a un usuario en la base de datos
    public boolean insertUser(@NonNull User u){
        boolean key = false;

        if(exists(u.getUsuario()) == 1){
            ContentValues cv = new ContentValues();
            cv.put("USER", u.getUsuario());
            cv.put("PASSWORD", u.getContrasena());
            key = bd.insert("USER", null, cv) > 0;
        }

        return key;
    }

    //Metodo encargado de hacer una lista de todos los usuarios que esten dados de alta en la base
    //de datos.
    public ArrayList<User> listAllUsers() {
        ArrayList<User> list = new ArrayList<User>();
        list.clear();
        Cursor cr = bd.rawQuery("SELECT * FROM USER", null);
        if(cr != null && cr.moveToFirst()){
            do{
                User u = new User();
                u.setId(cr.getInt(0));
                u.setUsuario(cr.getString(1));
                u.setContrasena(cr.getString(2));
                list.add(u);
            }while(cr.moveToNext());
        }
        return list;
    }

    //Metodo encargado de comprobar si un usuario existe o no.
    public int exists(String user){
        int key = 1;
        listUser = listAllUsers();
        for(User u:listUser){
            if(u.getUsuario().equals(user)){
                key = 0;
            }
        }
        return key;
    }

    //Metodo encargado de comprobar si un usuario es nulo o no.
    public int isNull(User u){
        int key = 1;
        if(u.getUsuario().isEmpty()){
            key = 0;
        }else if(u.getContrasena().isEmpty()){
            key = 0;
        }
        return key;
    }
}
