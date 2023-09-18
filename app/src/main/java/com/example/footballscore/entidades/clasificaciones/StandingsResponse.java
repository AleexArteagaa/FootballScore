package com.example.footballscore.entidades.clasificaciones;

import com.example.footballscore.entidades.Response;

import java.util.List;

public class StandingsResponse {

    private List<Response> response;

    private String get;

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
}
