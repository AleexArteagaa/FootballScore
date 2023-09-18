package com.example.footballscore.entidades.partidos.eventos;

public class Evento {
    private int minuto;
    private String equipo;
    private String jugador;
    private String tipo;

    public Evento(int minuto, String equipo, String jugador, String tipo) {
        this.minuto = minuto;
        this.equipo = equipo;
        this.jugador = jugador;
        this.tipo = tipo;
    }

    public int getMinuto() {
        return minuto;
    }

    public String getEquipo() {
        return equipo;
    }

    public String getJugador() {
        return jugador;
    }

    public String getTipo() {
        return tipo;
    }
}
