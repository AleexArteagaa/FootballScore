package com.example.footballscore.entidades.partidos.alineaciones;

import com.example.footballscore.entidades.jugadores.Birth;

public class Player {

    private int id;
    private String name;
    private Object number;
    private String pos;
    private String grid;

    private int age;
    private String firstname;
    private String lastname;
    private String nationality;
    private String photo;
    private String weight;
    private boolean injured;
    private String height;
    private Birth birth;

    private String type;
    private String reason;

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public int getAge() {
        return age;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPhoto() {
        return photo;
    }

    public String getWeight() {
        return weight;
    }

    public boolean isInjured() {
        return injured;
    }

    public String getHeight() {
        return height;
    }

    public Birth getBirth() {
        return birth;
    }

    private String primary;


    private String border;

    public String getPrimary() {
        return primary;
    }


    public String getBorder() {
        return border;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Object getNumber() {
        return number;
    }

    public String getPos() {
        return pos;
    }

    public String getGrid() {
        return grid;
    }
}
