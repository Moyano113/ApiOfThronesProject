package com.example.apiofthronesproject.model;

//Clase que contiene los métodos y campos que definen al usuario de la aplicaion donde guardaremos
// los datos de éste que se usaran para hacer operaciones en la base de datos
public class User {
    private int id;
    private String usuario;
    private String contrasena;

    public User(){}

    public User(String usuario, String contrasena){
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
