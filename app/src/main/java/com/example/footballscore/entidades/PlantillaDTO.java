package com.example.footballscore.entidades;


import java.util.ArrayList;
import java.util.List;

public class PlantillaDTO {


    private ArrayList<PosicionDTO> posiciones;
    private ArrayList<EntrenadorDTO> entrenadores;

    public PlantillaDTO(ArrayList<EntrenadorDTO> entrenadores) {

        this.entrenadores = entrenadores;
    }

    public ArrayList<EntrenadorDTO> getEntrenadores() {
        return entrenadores;
    }

    public void setEntrenadores(ArrayList<EntrenadorDTO> entrenadores) {
        this.entrenadores = entrenadores;
    }

    public ArrayList<PosicionDTO> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(ArrayList<PosicionDTO> posiciones) {
        this.posiciones = posiciones;
    }
}
