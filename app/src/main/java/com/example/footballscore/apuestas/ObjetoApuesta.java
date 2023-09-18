package com.example.footballscore.apuestas;

public class ObjetoApuesta {
    private String nombreObjeto;
    private int precio;
    private String foto;

    public ObjetoApuesta(String nombreObjeto, int precio, String foto) {
        this.nombreObjeto = nombreObjeto;
        this.precio = precio;
        this.foto = foto;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
