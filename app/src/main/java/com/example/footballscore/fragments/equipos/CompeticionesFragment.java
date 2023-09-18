package com.example.footballscore.fragments.equipos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorListaCompeticionesPorEquipo;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.partidos.league.League;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CompeticionesFragment extends Fragment {


    int idEquipo;

    RecyclerView recyclerView;
    AdaptadorListaCompeticionesPorEquipo adaptador;




    public CompeticionesFragment(int idEquipo){
        this.idEquipo = idEquipo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_competiciones_equipo, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewCompeticionesPorEquipo);
        adaptador = new AdaptadorListaCompeticionesPorEquipo(requireActivity());
        adaptador.showShimmer(true);
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<LeagueResponse> call = apiService.getCompeticionesPorEquipoTemporada(2022, idEquipo);

        call.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> leagues = response.body().getResponse();

                    List<League> listaLigas = new ArrayList<>();

                    for (int i = 0; i < leagues.size(); i++) {
                        League liga = leagues.get(i).getLeague();
                        if (!liga.getName().equalsIgnoreCase("Friendlies Clubs")){
                            listaLigas.add(liga);
                        }

                    }

                    adaptador.agregarListaCompeticiones(listaLigas);
                    adaptador.showShimmer(false);

                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });



        return view;
    }


}
