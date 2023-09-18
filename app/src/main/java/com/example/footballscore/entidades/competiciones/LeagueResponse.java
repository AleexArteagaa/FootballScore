package com.example.footballscore.entidades.competiciones;

import com.example.footballscore.entidades.Response;

import java.util.List;

public class LeagueResponse {
    private List<Response> response;

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
}
