package com.example.footballscore.entidades.partidos.alineaciones;

import com.example.footballscore.entidades.Response;

import java.util.List;

public class AlineacionesResponse {

    private List<Response> response;

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
}
