package com.example.footballscore.entidades.partidos;



import com.example.footballscore.entidades.Response;

import java.util.List;

public class PartidosResponse {

    private List<Response> response;

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
}
