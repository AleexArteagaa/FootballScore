package com.example.footballscore.entidades;

import com.example.footballscore.entidades.apuestas.Bookmakers;
import com.example.footballscore.entidades.competiciones.Country;
import com.example.footballscore.entidades.competiciones.Seasons;
import com.example.footballscore.entidades.entrenadores.Career;
import com.example.footballscore.entidades.equipos.Team;
import com.example.footballscore.entidades.equipos.Venue;
import com.example.footballscore.entidades.fichajes.Transfers;
import com.example.footballscore.entidades.jugadores.Birth;
import com.example.footballscore.entidades.jugadores.Players;
import com.example.footballscore.entidades.partidos.alineaciones.Coach;
import com.example.footballscore.entidades.partidos.alineaciones.Player;
import com.example.footballscore.entidades.partidos.alineaciones.StartXI;
import com.example.footballscore.entidades.partidos.alineaciones.Substitutes;
import com.example.footballscore.entidades.partidos.estadisticas.Statistics;
import com.example.footballscore.entidades.partidos.eventos.Assist;
import com.example.footballscore.entidades.partidos.eventos.Time;
import com.example.footballscore.entidades.partidos.fixture.Fixture;
import com.example.footballscore.entidades.partidos.goals.Goals;
import com.example.footballscore.entidades.partidos.league.League;
import com.example.footballscore.entidades.partidos.score.Score;
import com.example.footballscore.entidades.partidos.teams.Teams;

import java.util.List;

public class Response {


    //Para sacar datos de los equipos
    private Team team;
    private Venue venue;

    //Para sacar datos de los partidos
    private Fixture fixture;
    private Score score;
    private League league;
    private Teams teams;
    private Goals goals;

    //Para sacar datos de las competiciones

   // private League league;
    private Country country;
    private List<Seasons> seasons;


    //Para sacar datos de los paises

    private String name;
    private String code;
    private String flag;

    //Para sacar las apuestas

    private List<Bookmakers> bookmakers;


    //Para sacar las alineaciones de los partidos

    //Team
    private Coach coach;
    private String formation;
    private List<StartXI> startXI;
    private List<Substitutes> substitutes;


    //Para sacar eventos de los partidos

    private Time time;
    private Player player;
    private Assist assist;
    private String type;
    private String detail;
    private String comments;


    //Para sacar estadisticas de los partidos

    private List<Statistics> statistics;


    //Para sacar datos de los entrenadores

    //private String name;
    private String firstname;
    private String lastname;
    private int age;
    private String nationality;
    private String height;
    private String weight;
    private String photo;

    //Para sacar plantilla de los equipos

    private List<Players> players;

    //Para sacar los fichajes

    private List<Transfers> transfers;

    //Para sacar los entrenadores

    private Birth birth;
    private List<Career> career;
    private int id;

    public int getId() {
        return id;
    }

    public List<Career> getCareer() {
        return career;
    }

    public Birth getBirth() {
        return birth;
    }

    public List<Transfers> getTransfers() {
        return transfers;
    }

    public List<Players> getPlayers() {
        return players;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public String getNationality() {
        return nationality;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getPhoto() {
        return photo;
    }

    public List<Statistics> getStatistics() {
        return statistics;
    }

    public Time getTime() {
        return time;
    }

    public Player getPlayer() {
        return player;
    }

    public Assist getAssist() {
        return assist;
    }

    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public String getComments() {
        return comments;
    }

    public Response(String name, String flag){
        this.name = name;
        this.flag = flag;
    }

    public List<Bookmakers> getBookmakers() {
        return bookmakers;
    }

    public Coach getCoach() {
        return coach;
    }

    public String getFormation() {
        return formation;
    }

    public List<StartXI> getStartXI() {
        return startXI;
    }

    public List<Substitutes> getSubstitutes() {
        return substitutes;
    }

    public void setBookmakers(List<Bookmakers> bookmakers) {
        this.bookmakers = bookmakers;
    }

    public Team getTeam() {
        return team;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Teams getTeams() {
        return teams;
    }

    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Seasons> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Seasons> seasons) {
        this.seasons = seasons;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
