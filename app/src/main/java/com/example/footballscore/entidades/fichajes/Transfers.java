package com.example.footballscore.entidades.fichajes;

import com.example.footballscore.entidades.partidos.teams.Teams;

public class Transfers {

    private String date;
    private String type;

    private Teams teams;

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Teams getTeams() {
        return teams;
    }
}
