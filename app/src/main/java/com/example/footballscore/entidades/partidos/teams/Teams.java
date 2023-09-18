package com.example.footballscore.entidades.partidos.teams;

import com.example.footballscore.entidades.fichajes.In;
import com.example.footballscore.entidades.fichajes.Out;

public class Teams {

    private Home home;

    private Away away;

    private In in;
    private Out out;

    public In getIn() {
        return in;
    }

    public Out getOut() {
        return out;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Away getAway() {
        return away;
    }

    public void setAway(Away away) {
        this.away = away;
    }
}
