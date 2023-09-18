package com.example.footballscore.entidades;

public class EntrenadorDTO {

    private String nombre;
    private int id;
    private String foto;
    private String bandera;

    public EntrenadorDTO(String nombre, int id, String foto, String bandera) {
        this.nombre = nombre;
        this.id = id;
        this.foto = foto;
        this.bandera = bandera;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public String getFoto() {
        return foto;
    }

    public String getBandera() {
        return bandera;
    }
}
