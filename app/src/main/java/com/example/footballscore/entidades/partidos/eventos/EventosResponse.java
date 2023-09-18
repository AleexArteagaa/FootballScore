package com.example.footballscore.entidades.partidos.eventos;

import com.example.footballscore.entidades.Response;

import java.util.List;

public class EventosResponse {
    private List<Response> response;

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
}
