package com.example.footballscore.detallepartidos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.PartidoDTO;

import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.example.footballscore.util.MetodosAPI;


import retrofit2.Retrofit;


public class EstadisticasFragment extends Fragment {

    private PartidoDTO partidoDTO;

    public EstadisticasFragment(PartidoDTO partidoDTO) {
        this.partidoDTO=partidoDTO;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);



        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        MetodosAPI metodosAPI= new MetodosAPI();
        metodosAPI.getEstadisticasPorPartido(apiService, partidoDTO, view);


        return view;
    }

}
