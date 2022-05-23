package com.example.mufinds;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String genero;
    private String dataNaixement;
    //fechaNacimiento
    private String descripcion;
    private String nombreUsuari;
    private String fotoPerfil;
    private String instagram;

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String email, String password,
                   String genero, String dataNaixement, String descripcion, String nombreUsuari, String fotoPerfil, String instagram) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.genero = genero;
        this.dataNaixement = dataNaixement;
        this.descripcion = descripcion;
        this.nombreUsuari = nombreUsuari;
        this.fotoPerfil = fotoPerfil;
        this.instagram = instagram;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDataNaixement() {
        return dataNaixement;
    }

    public void setDataNaixement(String dataNaixement) {
        this.dataNaixement = dataNaixement;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreUsuari() {
        return nombreUsuari;
    }

    public void setNombreUsuari(String nombreUsuari) {
        this.nombreUsuari = nombreUsuari;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
}
