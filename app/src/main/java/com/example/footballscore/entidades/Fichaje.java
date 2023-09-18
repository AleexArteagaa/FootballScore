package com.example.footballscore.entidades;

public class Fichaje {
    private int id;
    private String jugador;
    private String fecha;
    private String tipo;
    private int idEquipoAlQueVa;
    private String logoEquipoAlQueVa;
    private String equipoAlQueVa;
    private int idEquipoDelQueViene;
    private String logoEquipoDelQueViene;
    private String equipoDelQueViene;

    public Fichaje(int id, String jugador, String fecha, String tipo, int idEquipoAlQueVa, String logoEquipoAlQueVa, String equipoAlQueVa, int idEquipoDelQueViene, String logoEquipoDelQueViene, String equipoDelQueViene) {
        this.id = id;
        this.jugador = jugador;
        this.fecha = fecha;
        this.tipo = tipo;
        this.idEquipoAlQueVa = idEquipoAlQueVa;
        this.logoEquipoAlQueVa = logoEquipoAlQueVa;
        this.equipoAlQueVa = equipoAlQueVa;
        this.idEquipoDelQueViene = idEquipoDelQueViene;
        this.logoEquipoDelQueViene = logoEquipoDelQueViene;
        this.equipoDelQueViene = equipoDelQueViene;
    }

    public int getId() {
        return id;
    }

    public String getJugador() {
        return jugador;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public int getIdEquipoAlQueVa() {
        return idEquipoAlQueVa;
    }

    public String getLogoEquipoAlQueVa() {
        return logoEquipoAlQueVa;
    }

    public String getEquipoAlQueVa() {
        return equipoAlQueVa;
    }

    public int getIdEquipoDelQueViene() {
        return idEquipoDelQueViene;
    }

    public String getLogoEquipoDelQueViene() {
        return logoEquipoDelQueViene;
    }

    public String getEquipoDelQueViene() {
        return equipoDelQueViene;
    }

    @Override
    public String toString() {
        return "Fichaje{" +
                "id=" + id +
                ", jugador='" + jugador + '\'' +
                ", fecha='" + fecha + '\'' +
                ", tipo='" + tipo + '\'' +
                ", equipoAlQueVa='" + equipoAlQueVa + '\'' +
                ", equipoDelQueViene='" + equipoDelQueViene + '\'' +
                '}';
    }
}

