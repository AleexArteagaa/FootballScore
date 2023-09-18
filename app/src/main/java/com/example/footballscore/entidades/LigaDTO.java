package com.example.footballscore.entidades;

import java.util.ArrayList;

public class LigaDTO {

    private int id;
    private String name;
    private String logo;
    private String flag;
    private ArrayList<PartidoDTO> partidos;

    public LigaDTO(int id, String name, String logo, String flag) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.flag = flag;
        this.partidos = new ArrayList<>();
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

    public String getLogo() {
        return logo;
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

    public ArrayList<PartidoDTO> getPartidos() {
        return partidos;
    }

    public void setPartidos(ArrayList<PartidoDTO> partidos) {
        this.partidos = partidos;
    }
}
