package com.example.mufinds;

import java.util.ArrayList;

public class RelacionMusicaUsuario {
    private String idUsuario;
    private ArrayList<String> idCancion;

    public RelacionMusicaUsuario() {}

    public RelacionMusicaUsuario(String idUsuario, ArrayList<String> idCancion) {
        this.idUsuario = idUsuario;
        this.idCancion = idCancion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ArrayList<String> getIdCancion() {
        return idCancion;
    }

    public void setIdCancion(ArrayList<String> idCancion) {
        this.idCancion = idCancion;
    }
}
