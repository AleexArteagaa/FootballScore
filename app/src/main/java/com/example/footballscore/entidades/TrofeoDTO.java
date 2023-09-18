package com.example.footballscore.entidades;

public class TrofeoDTO {

    private String competicion;
    private String pais;
    private String temporada;
    private String puesto;

    public TrofeoDTO(String competicion, String pais, String temporada, String puesto) {
        this.competicion = competicion;
        this.pais = pais;
        this.temporada = temporada;
        this.puesto = puesto;
    }

    public String getCompeticion() {
        return competicion;
    }

    public String getPais() {
        return pais;
    }

    public String getTemporada() {
        return temporada;
    }

    public String getPuesto() {
        return puesto;
    }
}
