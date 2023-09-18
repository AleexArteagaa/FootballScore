package com.example.footballscore.entidades;

public class JugadorDTO {

    private int id;
    private String name;
    private int age;
    private int number;
    private String position;
    private String photo;
    private String enlaceBandera;

    public JugadorDTO(int id, String name, int age, int number, String position, String photo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.number = number;
        this.position = position;
        this.photo = photo;
    }

    public String getName() {
        return name;
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

    public void setEnlaceBandera(String enlaceBandera) {
        this.enlaceBandera = enlaceBandera;
    }

    public String getEnlaceBandera() {
        return enlaceBandera;
    }

    public int getId() {
        return id;
    }
}
