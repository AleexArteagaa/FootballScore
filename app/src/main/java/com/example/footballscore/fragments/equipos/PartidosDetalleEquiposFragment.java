package com.example.footballscore.fragments.equipos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorListaFechasFichajes;
import com.example.footballscore.adaptador.AdaptadorListaMesesPartidosEquipo;
import com.example.footballscore.entidades.Fichaje;
import com.example.footballscore.entidades.FichajesPorFecha;
import com.example.footballscore.entidades.LigaDTO;
import com.example.footballscore.entidades.MesDTO;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.entidades.fichajes.FichajesResponse;
import com.example.footballscore.entidades.fichajes.Transfers;
import com.example.footballscore.entidades.jugadores.TemporadasResponse;
import com.example.footballscore.entidades.partidos.PartidosResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PartidosDetalleEquiposFragment extends Fragment {

    int idEquipo;
    Spinner spinner;
    RecyclerView recyclerView;

    AdaptadorListaMesesPartidosEquipo adaptador;

    public PartidosDetalleEquiposFragment(int idEquipo){
        this.idEquipo = idEquipo;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_partidos_equipo, container, false);

        spinner= view.findViewById(R.id.spinnerSeleccionTemporadaPartidosEquipo);
        recyclerView = view.findViewById(R.id.recyclerViewPartidosPorEquipoYAnio);

        adaptador = new AdaptadorListaMesesPartidosEquipo(requireActivity());
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Call<TemporadasResponse> call = apiService.getTemporadasPorEquipo(idEquipo);
        call.enqueue(new Callback<TemporadasResponse>() {
            @Override
            public void onResponse(Call<TemporadasResponse> call, Response<TemporadasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Integer> temporadas = response.body().getResponse();

                    if (temporadas.size() > 0) {


                        for (int i = temporadas.size() - 1; i >= 0; i--) {
                            int temporadaActual = temporadas.get(i);
                            int temporadaSiguiente = temporadaActual + 1;
                            String temporadaCompleta = temporadaActual + "/" + temporadaSiguiente;
                            adapter.add(temporadaCompleta);
                        }
                    }
                    int index = adapter.getPosition("2022/2023");
                    if (index >= 0) {
                        spinner.setSelection(index);
                    }
                }else {
                    spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TemporadasResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] anios = selectedItem.split("/");
                int primerAnio = Integer.parseInt(anios[0]);

                ArrayList<MesDTO> meses = new ArrayList<>();


                Call<PartidosResponse> call2 = apiService.getPartidosPorEquipoYTemporada(idEquipo, primerAnio);
                call2.enqueue(new Callback<PartidosResponse>() {
                    @Override
                    public void onResponse(Call<PartidosResponse> call, Response<PartidosResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<com.example.footballscore.entidades.Response> partidos = response.body().getResponse();
                            List<MesDTO> meses = new ArrayList<>();

                            if (partidos.size() > 0) {

                                for (com.example.footballscore.entidades.Response partido : partidos) {
                                    String fecha = partido.getFixture().getDate();
                                    String[] partesFecha = fecha.split("-");
                                    String nombreMes = obtenerNombreMes(Integer.parseInt(partesFecha[1]));

                                    MesDTO mes = null;
                                    for (MesDTO l : meses) {
                                        if (l.getMes().equalsIgnoreCase(nombreMes)) {
                                            mes = l;
                                            break;
                                        }
                                    }

                                    if (mes == null) {
                                        mes = new MesDTO(nombreMes);
                                        meses.add(mes);
                                    }

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
                                    partidoNuevo.setLiga(partido.getLeague().getName());

                                    mes.getPartidos().add(partidoNuevo);
                                }

                                adaptador = new AdaptadorListaMesesPartidosEquipo(requireActivity());
                                recyclerView.setAdapter(adaptador);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                adaptador.agregarListaPosiciones(meses);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PartidosResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    private String obtenerNombreMes(int numeroMes) {
        String[] meses = new DateFormatSymbols(new Locale("es", "ES")).getMonths();
        return meses[numeroMes - 1];
    }

}
