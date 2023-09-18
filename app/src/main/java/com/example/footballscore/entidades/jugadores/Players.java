package com.example.footballscore.entidades.jugadores;

public class Players {

    private int id;

    private String name;
    private int age;
    private int number;
    private String position;
    private String photo;
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getNumber() {
        return number;
    }

    public String getPosition() {
        return position;
    }

    public String getPhoto() {
        return photo;
    }
}
