package com.example.footballscore.entidades.equipos;

import com.example.footballscore.entidades.Response;

import java.util.List;

public class TeamResponse {
    private String get;
    private TeamParameters parameters;
    private List<String> errors;
    private int results;
    private List<Response> response;

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }

    public TeamParameters getParameters() {
        return parameters;
    }

    public void setParameters(TeamParameters parameters) {
        this.parameters = parameters;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
}



