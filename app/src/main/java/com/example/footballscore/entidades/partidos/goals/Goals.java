package com.example.footballscore.entidades.partidos.goals;

import com.google.gson.annotations.SerializedName;

public class Goals {

    //Para sacar los partidos
    private int home;
    private int away;

    //Para sacar la clasificacion
    @SerializedName("for")
    private int goalsFor;
    private int against;

    private int assists;
    private int conceded;
    private int saves;
    private int total;

    public int getAssists() {
        return assists;
    }

    public int getConceded() {
        return conceded;
    }

    public int getSaves() {
        return saves;
    }

    public int getTotal() {
        return total;
    }

    public int getHome() {
        return home;
    }

    public void setHome(int home) {
        this.home = home;
    }

    public int getAway() {
        return away;
    }

    public void setAway(int away) {
        this.away = away;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getAgainst() {
        return against;
    }

    public void setAgainst(int against) {
        this.against = against;
    }
}
