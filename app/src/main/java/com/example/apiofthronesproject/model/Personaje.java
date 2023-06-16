package com.example.apiofthronesproject.model;

//Clase que contiene los métodos y campos que definen un personaje donde guardaremos los resultados
//que obtengamos de la API
public class Personaje {
    private String id;
    private String nombre;
    private String titulo;
    private String familia;
    private String imgUrl;

    public Personaje(){}

    public Personaje(String id, String nombre, String titulo, String familia, String imgUrl) {
        this.id = id;
        this.nombre = nombre;
        this.titulo = titulo;
        this.familia = familia;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
