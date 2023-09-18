package com.example.footballscore.fragments.jugadores;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorTrayectoriaJugador;
import com.example.footballscore.entidades.TrayectoriaDTO;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.jugadores.TemporadasResponse;
import com.example.footballscore.entidades.partidos.estadisticas.Statistics;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TrayectoriaJugadorFragment extends Fragment {

    private int idJugador;
    private RecyclerView recyclerView;
    private AdaptadorTrayectoriaJugador adaptador;


    public TrayectoriaJugadorFragment(int idJugador){
        this.idJugador = idJugador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trayectoria_jugador, container, false);

        recyclerView = view.findViewById(R.id.recyclerTrayectoriaJugador);

        adaptador = new AdaptadorTrayectoriaJugador(requireActivity());

        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<TemporadasResponse> call = apiService.getTemporadasPorJugador(idJugador);
        call.enqueue(new Callback<TemporadasResponse>() {
            @Override
            public void onResponse(Call<TemporadasResponse> call, Response<TemporadasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Integer> temporadas = response.body().getResponse();
                    List<TrayectoriaDTO> trayectorias = new ArrayList<>();

                    if (temporadas.size() > 0){

                        obtenerEstadisticasPorTemporada(temporadas, 0, trayectorias);

                    }else {
                        TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleTrayectoriaJugador);

                        recyclerView.setVisibility(View.GONE);

                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                        txtMensajeNoDisponible.setVisibility(View.VISIBLE);

                        LinearLayout.LayoutParams txtMensajeLayoutParams = (LinearLayout.LayoutParams) txtMensajeNoDisponible.getLayoutParams();
                        txtMensajeLayoutParams.gravity = Gravity.CENTER;

                        txtMensajeNoDisponible.setLayoutParams(txtMensajeLayoutParams);
                    }

                }
            }

            @Override
            public void onFailure(Call<TemporadasResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        return view;
    }


    private void obtenerEstadisticasPorTemporada (List <Integer> temporadas,int index, List<TrayectoriaDTO> trayectorias){
        if (index >= temporadas.size()) {
            adaptador.agregarListaEstadisticas(trayectorias);
            return;
        }

        int temporada = temporadas.get(index);

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<JugadoresResponse> call = apiService.getEstadisticasPorJugadorTemporada(idJugador, temporada);
        call.enqueue(new Callback<JugadoresResponse>() {
            @Override
            public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> estadisticas = response.body().getResponse();
                    List<Statistics> stats = estadisticas.get(0).getStatistics();

                    Set<String> equipoSet = new HashSet<>();

                    for (Statistics stat : stats) {
                        String equipo = stat.getTeam().getName();
                        String logoEquipo = stat.getTeam().getLogo();
                        String paisEquipo = stat.getLeague().getCountry();
                        if (equipo != null && !equipoSet.contains(equipo) && (paisEquipo != null && !paisEquipo.equalsIgnoreCase("World"))) {
                            TrayectoriaDTO trayectoria = new TrayectoriaDTO(temporada, equipo, logoEquipo);
                            trayectorias.add(trayectoria);
                            equipoSet.add(equipo);
                        }
                    }

                    obtenerEstadisticasPorTemporada(temporadas, index + 1, trayectorias);
                }
            }

            @Override
            public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
