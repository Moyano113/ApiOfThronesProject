package com.example.apiofthronesproject.model;

public class User {
    private String usuario;

    private String contrasena;

    public User(){}

    public User(String usuario, String contrasena){
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contraseña) {
        this.contrasena = contraseña;
    }
}
