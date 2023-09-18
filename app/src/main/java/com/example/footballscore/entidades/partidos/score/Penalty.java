package com.example.footballscore.entidades.partidos.score;

public class Penalty {
    private int home;
    private int away;

    private int commited;
    private int missed;
    private int saved;
    private int scored;
    private int won;

    public int getCommited() {
        return commited;
    }

    public int getMissed() {
        return missed;
    }

    public int getSaved() {
        return saved;
    }

    public int getScored() {
        return scored;
    }

    public int getWon() {
        return won;
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
}
