package com.example.footballscore.entidades.competiciones;

public class Coverage {

    private boolean standings;
    private boolean players;
    private boolean top_scorers;
    private boolean top_assists;
    private boolean top_cards;
    private boolean injuries;
    private boolean predictions;
    private boolean odds;
    private Fixtures fixtures;

    public boolean isStandings() {
        return standings;
    }

    public boolean isPlayers() {
        return players;
    }

    public boolean isTop_scorers() {
        return top_scorers;
    }

    public boolean isTop_assists() {
        return top_assists;
    }

    public boolean isTop_cards() {
        return top_cards;
    }

    public boolean isInjuries() {
        return injuries;
    }

    public boolean isPredictions() {
        return predictions;
    }

    public boolean isOdds() {
        return odds;
    }

    public Fixtures getFixtures() {
        return fixtures;
    }
}
