package com.example.footballscore.fragments.ligas;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.EquiposAdapter;
import com.example.footballscore.entidades.Response;
import com.example.footballscore.entidades.equipos.Team;
import com.example.footballscore.entidades.equipos.TeamResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class FragmentEquiposDetalleLiga extends Fragment {
    private EquiposAdapter adaptador;
    int idLiga;
    public FragmentEquiposDetalleLiga (int idLiga) {
        this.idLiga = idLiga;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_equipos, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerEquipos);

        adaptador = new EquiposAdapter(getContext());

        recyclerView.setAdapter(adaptador);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService=retrofit.create(ServicioAPI.class);
        Call<TeamResponse> call=apiService.getEquiposPorLiga(idLiga , 2022);
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, retrofit2.Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();

                    if (teams.size() > 0){
                        adaptador.agregarListaClasificacion(teams);

                    }

                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return view;
    }
}