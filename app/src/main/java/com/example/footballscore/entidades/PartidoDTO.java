package com.example.footballscore.entidades;

import java.io.Serializable;

public class PartidoDTO implements Serializable {

    private int id;
    private String referee;
    private String timezone;
    private String date;
    private int timestamp;
    private int elapsed;
    private int idHomeTeam;
    private String homeTeam;
    private int idAwayTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
   // private boolean acabado;
    private String estado;
    private String round;
    private String homeLogo;
    private String awayLogo;
    private String liga;

    public PartidoDTO(int id, String referee, String timezone, String date, int timestamp, int elapsed, int idHomeTeam,String homeTeam, int idAwayTeam,String awayTeam, int homeScore, int awayScore, String acabado, String round, String homeLogo, String awayLogo) {
        this.id = id;
        this.referee = referee;
        this.timezone = timezone;
        this.date = date;
        this.timestamp = timestamp;
        this.elapsed = elapsed;
        this.idHomeTeam = idHomeTeam;
        this.homeTeam = homeTeam;
        this.idAwayTeam = idAwayTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.estado = acabado;
        this.round = round;
        this.homeLogo = homeLogo;
        this.awayLogo = awayLogo;
        this.liga = null;
    }

    public String getLiga() {
        return liga;
    }

    public void setLiga(String liga) {
        this.liga = liga;
    }

    public PartidoDTO() {

    }

    public int getIdHomeTeam() {
        return idHomeTeam;
    }

    public void setIdHomeTeam(int idHomeTeam) {
        this.idHomeTeam = idHomeTeam;
    }

    public int getIdAwayTeam() {
        return idAwayTeam;
    }

    public void setIdAwayTeam(int idAwayTeam) {
        this.idAwayTeam = idAwayTeam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getElapsed() {
        return elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getHomeLogo() {
        return homeLogo;
    }

    public void setHomeLogo(String homeLogo) {
        this.homeLogo = homeLogo;
    }

    public String getAwayLogo() {
        return awayLogo;
    }

    public void setAwayLogo(String awayLogo) {
        this.awayLogo = awayLogo;
    }
}
