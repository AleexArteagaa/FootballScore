package com.example.footballscore.entidades.equipos;

import com.example.footballscore.entidades.partidos.alineaciones.Colors;

import java.util.List;

public class Team  {

    private int id;
    private String name;
    private String country;
    private int founded;
    private boolean national;
    private String logo;
    private String code;

    private Colors colors;

    public String getCode() {
        return code;
    }

    public Colors getColors() {
        return colors;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getFounded() {
        return founded;
    }

    public void setFounded(int founded) {
        this.founded = founded;
    }

    public boolean isNational() {
        return national;
    }

    public void setNational(boolean national) {
        this.national = national;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", founded=" + founded +
                ", national=" + national +
                ", logo='" + logo + '\'' +
                '}';
    }
}
