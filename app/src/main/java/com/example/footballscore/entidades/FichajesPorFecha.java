package com.example.footballscore.entidades;

import java.util.ArrayList;
import java.util.List;

public class FichajesPorFecha {
    private String fecha;
    private ArrayList<Fichaje> fichajes;

    public FichajesPorFecha(String fecha, ArrayList<Fichaje> fichajes) {
        this.fecha = fecha;
        this.fichajes = fichajes;
    }

    public String getFecha() {
        return fecha;
    }

    public ArrayList<Fichaje> getFichajes() {
        return fichajes;
    }
}