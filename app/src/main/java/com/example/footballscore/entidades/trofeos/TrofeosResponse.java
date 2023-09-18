package com.example.footballscore.entidades.trofeos;

import com.example.footballscore.entidades.ResponseTrofeos;

import java.util.List;

public class TrofeosResponse {

    private List<ResponseTrofeos> response;

    public List<ResponseTrofeos> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseTrofeos> response) {
        this.response = response;
    }
}
