package com.example.footballscore.detallepartidos.eventos;

import android.os.Bundle;

import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.footballscore.R;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.example.footballscore.util.MetodosAPI;

import retrofit2.Retrofit;


public class EventosFragment extends Fragment {
    private PartidoDTO partidoDTO;

    public EventosFragment(PartidoDTO partidoDTO) {
        this.partidoDTO= partidoDTO;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eventos, container, false);


        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        LinearLayout eventosLayout = view.findViewById(R.id.eventosLayout);
        MetodosAPI metodosAPI= new MetodosAPI();
        metodosAPI.getEventosPorPartido(apiService, partidoDTO, eventosLayout);


        return view;
    }

}