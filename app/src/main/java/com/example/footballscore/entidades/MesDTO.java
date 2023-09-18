package com.example.footballscore.entidades;

import java.util.ArrayList;

public class MesDTO {

    private String mes;

    private ArrayList<PartidoDTO> partidos;

    public MesDTO(String mes) {
        this.mes = mes;
        this.partidos = new ArrayList<>();
    }

    public String getMes() {
        return mes;
    }

    public ArrayList<PartidoDTO> getPartidos() {
        return partidos;
    }
}
