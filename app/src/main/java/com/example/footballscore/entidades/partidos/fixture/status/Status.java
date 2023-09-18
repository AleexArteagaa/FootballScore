package com.example.footballscore.entidades.partidos.fixture.status;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("long")
    private String isAcabadoLargo;

    @SerializedName("short")
    private String isAcabadoCorto;

    private int elapsed;

    public String getIsAcabadoLargo() {
        return isAcabadoLargo;
    }

    public void setIsAcabadoLargo(String isAcabadoLargo) {
        this.isAcabadoLargo = isAcabadoLargo;
    }

    public String getIsAcabadoCorto() {
        return isAcabadoCorto;
    }

    public void setIsAcabadoCorto(String isAcabadoCorto) {
        this.isAcabadoCorto = isAcabadoCorto;
    }

    public int getElapsed() {
        return elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }
}
