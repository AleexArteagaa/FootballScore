package com.example.footballscore.entidades.fichajes;

import com.example.footballscore.entidades.partidos.teams.Away;
import com.example.footballscore.entidades.partidos.teams.Home;

public class Out {

    private Home home;

    private Away away;

    private int id;
    private String name;
    private String logo;

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


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
