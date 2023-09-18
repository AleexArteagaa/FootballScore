package com.example.footballscore.entidades;

import java.util.ArrayList;

public class JornadaDTO {

    private String jornada;

    private ArrayList<PartidoDTO> partidos;

    public JornadaDTO(String jornada) {
        this.jornada = jornada;
        this.partidos = new ArrayList<>();
    }

    public String getJornada() {
        return jornada;
    }

    public ArrayList<PartidoDTO> getPartidos() {
        return partidos;
    }

}
