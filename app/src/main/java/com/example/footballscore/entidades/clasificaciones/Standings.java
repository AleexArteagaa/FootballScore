package com.example.footballscore.entidades.clasificaciones;

import com.example.footballscore.entidades.equipos.Team;
import com.example.footballscore.entidades.partidos.teams.Away;
import com.example.footballscore.entidades.partidos.teams.Home;


public class Standings {

    private int rank;
    private Team team;
    private int points;
    private int goalsDiff;
    private String group;
    private String form;
    private String status;
    private String description;
    private All all;
    private Home home;
    private Away away;
    private String update;


    public All getAll() {
        return all;
    }

    public void setAll(All all) {
        this.all = all;
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

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalsDiff() {
        return goalsDiff;
    }

    public void setGoalsDiff(int goalsDiff) {
        this.goalsDiff = goalsDiff;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
