package com.example.footballscore.apuestas.historial;

public class HistorialDTO {
    private String estado;
    private String nombre;
    private int cantidad;

    public HistorialDTO() {
    }
    public HistorialDTO(String estado, String nombre, int cantidad) {
        this.estado = estado;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "HistorialDTO{" +
                "estado='" + estado + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
