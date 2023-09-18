package com.example.footballscore.entidades;

import java.util.ArrayList;

public class PosicionDTO {

    private String posicion;
    private ArrayList<JugadorDTO> jugadores;

    public PosicionDTO(String posicion) {
        this.posicion = posicion;
        this.jugadores = new ArrayList<>();
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public ArrayList<JugadorDTO> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<JugadorDTO> jugadores) {
        this.jugadores = jugadores;
    }
}
