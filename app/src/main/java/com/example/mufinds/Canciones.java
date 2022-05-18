package com.example.mufinds;

public class Canciones {
    private String idCancion;
    private String titulo;
    private String album;
    private String artista;
    private int año_lanzamiento;
    private String portada;

    public Canciones(String idCancion, String titulo, String album, String artista, int año_lanzamiento, String portada) {
        this.idCancion = idCancion;
        this.titulo = titulo;
        this.album = album;
        this.artista = artista;
        this.año_lanzamiento = año_lanzamiento;
        this.portada = portada;
    }

    public Canciones(){}

    public String getIdCancion() {
        return idCancion;
    }
    public void setIdCancion(String idCancion) {
        this.idCancion = idCancion;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getArtista() {
        return artista;
    }
    public void setArtista(String artista) {
        this.artista = artista;
    }
    public int getAño_lanzamiento() {
        return año_lanzamiento;
    }
    public void setAño_lanzamiento(int año_lanzamiento) {
        this.año_lanzamiento = año_lanzamiento;
    }
    public String getPortada() {
        return portada;
    }
    public void setPortada(String portada) {
        this.portada = portada;
    }
}
