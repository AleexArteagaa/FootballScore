package com.example.footballscore.entidades;

public class TrayectoriaDTO {
    private int temporada;
    private String equipo;
    private String logoEquipo;

    public TrayectoriaDTO(int temporada, String equipo,  String logoEquipo) {
        this.temporada = temporada;
        this.equipo = equipo;
        this.logoEquipo = logoEquipo;
    }

    public String getLogoEquipo() {
        return logoEquipo;
    }

    public int getTemporada() {
        return temporada;
    }

    public String getEquipo() {
        return equipo;
    }
}
