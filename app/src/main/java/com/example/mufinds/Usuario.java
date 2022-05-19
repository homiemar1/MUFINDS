package com.example.mufinds;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable {
    private String idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String genero;
    private Date dataNaixement;
    private String descripcion;
    private String nombreUsuari;
    private Uri fotoPerfil;

    public Usuario() {
    }

    public Usuario(String idUsuario, String nombre, String apellido, String email, String password,
                   String genero, Date dataNaixement, String descripcion, String nombreUsuari, Uri fotoPerfil) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.genero = genero;
        this.dataNaixement = dataNaixement;
        this.descripcion = descripcion;
        this.nombreUsuari = nombreUsuari;
        this.fotoPerfil = fotoPerfil;
    }

    public String getIdUsuario() {
        return nombreUsuari + "_" + email;
    }

    public void setIdUsuario() {
        this.idUsuario = nombreUsuari + "_" + email;
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

    public Date getDataNaixement() {
        return dataNaixement;
    }

    public void setDataNaixement(Date dataNaixement) {
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

    public Uri getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Uri fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
