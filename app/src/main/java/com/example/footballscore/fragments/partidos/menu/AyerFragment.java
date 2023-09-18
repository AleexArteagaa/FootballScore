package com.example.footballscore.fragments.partidos.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.entidades.LigaDTO;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorListaLigas;
import com.example.footballscore.entidades.partidos.PartidosResponse;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AyerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ayer, container, false);

        System.out.println("AYER");
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewTabbedAyer);

        AdaptadorListaLigas adaptadorListaLigas = new AdaptadorListaLigas(getContext());

        adaptadorListaLigas.showShimmer(true);

        recyclerView.setAdapter(adaptadorListaLigas);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();


        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<PartidosResponse> call = apiService.getPartidosPorDia(getFechaAyer());
        call.enqueue(new Callback<PartidosResponse>() {
            @Override
            public void onResponse(Call<PartidosResponse> call, Response<PartidosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> partidos = response.body().getResponse();

                    ArrayList<LigaDTO> ligas = new ArrayList<>();

                    for (com.example.footballscore.entidades.Response partido : partidos) {
                        // Obtener la información de la liga
                        int ligaId = partido.getLeague().getId();
                        String ligaNombre = partido.getLeague().getName();
                        String ligaLogo = partido.getLeague().getLogo();
                        String ligaBandera = partido.getLeague().getFlag();

                        // Buscar la liga en el ArrayList de ligas
                        LigaDTO liga = null;
                        for (LigaDTO l : ligas) {
                            if (l.getId() == ligaId) {
                                liga = l;
                                break;
                            }
                        }

                        // Si la liga no está en el ArrayList, crear una nueva Liga y agregarla al ArrayList
                        if (liga == null) {
                            liga = new LigaDTO(ligaId, ligaNombre, ligaLogo, ligaBandera);
                            ligas.add(liga);
                        }

                        // Agregar el partido a la liga correspondiente
                        PartidoDTO partidoNuevo = new PartidoDTO(
                                partido.getFixture().getId(),
                                partido.getFixture().getReferee(),
                                partido.getFixture().getTimezone(),
                                partido.getFixture().getDate(),
                                partido.getFixture().getTimestamp(),
                                partido.getFixture().getStatus().getElapsed(),
                                partido.getTeams().getHome().getId(),
                                partido.getTeams().getHome().getName(),
                                partido.getTeams().getAway().getId(),
                                partido.getTeams().getAway().getName(),
                                partido.getGoals().getHome(),
                                partido.getGoals().getAway(),
                                partido.getFixture().getStatus().getIsAcabadoLargo(),
                                partido.getLeague().getRound(),
                                partido.getTeams().getHome().getLogo(),
                                partido.getTeams().getAway().getLogo()
                        );

                        liga.getPartidos().add(partidoNuevo);


                    }
                    adaptadorListaLigas.agregarListaLigas(ligas);                    adaptadorListaLigas.showShimmer(false);
                    adaptadorListaLigas.showShimmer(false);

                }
            }

            @Override
            public void onFailure(Call<PartidosResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public String getFechaAyer(){

        Calendar ayer = Calendar.getInstance();
        ayer.add(Calendar.DATE, -1);
        String fechaPartidos = new SimpleDateFormat("yyyy-MM-dd").format(ayer.getTime());

        return fechaPartidos;
    }


}