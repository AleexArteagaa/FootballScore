package com.example.footballscore.entidades;

import java.util.ArrayList;
import java.util.List;

public class TemporadaDTO {

    private String temporada;

    private ArrayList<TrofeoDTO> trofeos;

    public TemporadaDTO(String temporada) {
        this.temporada = temporada;
        trofeos = new ArrayList<>();
    }

    public String getTemporada() {
        return temporada;
    }

    public ArrayList<TrofeoDTO> getTrofeos() {
        return trofeos;
    }
}
