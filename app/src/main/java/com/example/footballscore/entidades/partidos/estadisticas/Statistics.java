package com.example.footballscore.entidades.partidos.estadisticas;

import com.example.footballscore.entidades.equipos.Team;
import com.example.footballscore.entidades.jugadores.Cards;
import com.example.footballscore.entidades.jugadores.Dribbles;
import com.example.footballscore.entidades.jugadores.Duels;
import com.example.footballscore.entidades.jugadores.Fouls;
import com.example.footballscore.entidades.jugadores.Games;
import com.example.footballscore.entidades.jugadores.Passes;
import com.example.footballscore.entidades.jugadores.Shots;
import com.example.footballscore.entidades.jugadores.Tackles;
import com.example.footballscore.entidades.partidos.alineaciones.Substitutes;
import com.example.footballscore.entidades.partidos.goals.Goals;
import com.example.footballscore.entidades.partidos.league.League;
import com.example.footballscore.entidades.partidos.score.Penalty;

public class Statistics {

    private String type;
    private Object value;

    private Cards cards;
    private Dribbles dribbles;
    private Duels duels;
    private Fouls fouls;
    private Games games;
    private Goals goals;
    private League league;
    private Passes passes;
    private Penalty penalty;
    private Shots shots;
    private Substitutes substitutes;
    private Tackles tackles;
    private Team team;

    public Cards getCards() {
        return cards;
    }

    public Dribbles getDribbles() {
        return dribbles;
    }

    public Duels getDuels() {
        return duels;
    }

    public Fouls getFouls() {
        return fouls;
    }

    public Games getGames() {
        return games;
    }

    public Goals getGoals() {
        return goals;
    }

    public League getLeague() {
        return league;
    }

    public Passes getPasses() {
        return passes;
    }

    public Penalty getPenalty() {
        return penalty;
    }

    public Shots getShots() {
        return shots;
    }

    public Substitutes getSubstitutes() {
        return substitutes;
    }

    public Tackles getTackles() {
        return tackles;
    }

    public Team getTeam() {
        return team;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
