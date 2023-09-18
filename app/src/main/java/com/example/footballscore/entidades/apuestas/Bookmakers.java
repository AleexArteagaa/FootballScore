package com.example.footballscore.entidades.apuestas;

import java.util.List;

public class Bookmakers {

    private int id;
    private String name;

    private List<Bets> bets;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bets> getBets() {
        return bets;
    }

    public void setBets(List<Bets> bets) {
        this.bets = bets;
    }
}
