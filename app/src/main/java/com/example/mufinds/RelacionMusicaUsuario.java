package com.example.mufinds;

public class RelacionMusicaUsuario {
    private String idUsuario;
    private String idCancion;

    public RelacionMusicaUsuario() {}

    public RelacionMusicaUsuario(String idCancion) {
        this.idCancion = idCancion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdCancion() {
        return idCancion;
    }

    public void setIdCancion(String idCancion) {
        this.idCancion = idCancion;
    }
}
