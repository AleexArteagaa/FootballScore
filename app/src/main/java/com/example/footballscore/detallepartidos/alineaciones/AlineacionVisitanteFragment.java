package com.example.footballscore.detallepartidos.alineaciones;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballscore.R;

public class AlineacionVisitanteFragment extends Fragment {

    public AlineacionVisitanteFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alineacion_visitante, container, false);
    }
}