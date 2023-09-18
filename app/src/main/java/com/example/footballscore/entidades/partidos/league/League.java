package com.example.footballscore.entidades.partidos.league;

import com.example.footballscore.entidades.clasificaciones.Standings;

import java.util.List;

public class League {

    //Para sacar partidos
    private int id;
    private String name;
    private String logo;
    private String flag;
    private Object season;
    private String round;
    private String country;

    //Para sacar clasificaciones
    private List<List<Standings>> standings;

    public String getCountry() {
        return country;
    }

    public List<List<Standings>> getStandings() {
        return standings;
    }

    public void setStandings(List<List<Standings>> standings) {
        this.standings = standings;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public Object getSeason() {
        return season;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public void setSeason(int season) {
        this.season = season;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }
}
