package com.example.footballscore.favoritos;

public class Favoritos {
    String nombreLiga ;
    String fotoLiga;
    String fotoPais;
    int idLeague;


    public Favoritos(String nombreLiga, String fotoLiga, String fotoPais, int idLeague) {
        this.nombreLiga = nombreLiga;
        this.fotoLiga = fotoLiga;
        this.fotoPais = fotoPais;
        this.idLeague= idLeague;

    }

    public String getNombreLiga() {
        return nombreLiga;
    }

    public void setNombreLiga(String nombreLiga) {
        this.nombreLiga = nombreLiga;
    }

    public String getFotoPais() {
        return fotoPais;
    }

    public void setFotoPais(String fotoPais) {
        this.fotoPais = fotoPais;
    }

    public String getFotoLiga() {
        return fotoLiga;
    }

    public void setFotoLiga(String fotoLiga) {
        this.fotoLiga = fotoLiga;
    }
    public int getIdLeague() {
        return idLeague;
    }

    public void setIdLeague(int idLeague) {
        this.idLeague = idLeague;
    }
}
