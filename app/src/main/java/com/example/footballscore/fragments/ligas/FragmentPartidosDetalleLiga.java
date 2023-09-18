package com.example.footballscore.fragments.ligas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorListaFechasFichajes;
import com.example.footballscore.adaptador.AdaptadorListaJornadasPartidosLiga;
import com.example.footballscore.entidades.Fichaje;
import com.example.footballscore.entidades.FichajesPorFecha;
import com.example.footballscore.entidades.JornadaDTO;
import com.example.footballscore.entidades.LigaDTO;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.competiciones.Seasons;
import com.example.footballscore.entidades.fichajes.FichajesResponse;
import com.example.footballscore.entidades.fichajes.Transfers;
import com.example.footballscore.entidades.partidos.PartidosResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentPartidosDetalleLiga extends Fragment {

    private Spinner spinner;
    private ArrayList<LigaDTO> ligas;

    private ArrayAdapter<String> spinnerAdapter;
    private List<String> optionsList;
    AdaptadorListaJornadasPartidosLiga adaptador;
    int idLiga;


    public FragmentPartidosDetalleLiga(int idLiga){
        this.idLiga = idLiga;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_partidos_detalles_liga, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerListaPartidos);

        spinner = rootView.findViewById(R.id.optionsSpinnerSeleccionAnioPartidosLiga);
        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService=retrofit.create(ServicioAPI.class);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

        Call<LeagueResponse> call = apiService.getCompeticionesPorId(idLiga);
        call.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> liga = response.body().getResponse();

                    List<Seasons> temporadas = liga.get(0).getSeasons();

                    List<String> reversedList = new ArrayList<>();

                    for (int i = temporadas.size() - 1; i >= 0; i--) {
                        if (temporadas.get(i).getCoverage().getFixtures().isEvents()) {
                            int anio = temporadas.get(i).getYear();
                            String anioFormato = anio + "/" + (anio + 1);
                            reversedList.add(anioFormato);
                        }
                    }

                    ArrayAdapter<String> reversedAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, reversedList);
                    reversedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(reversedAdapter);

                    int index = reversedAdapter.getPosition("2022/2023");
                    if (index >= 0) {
                        spinner.setSelection(index);
                    }
                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] years = selectedItem.split("/");
                int firstYear = Integer.parseInt(years[0]);

                Call<PartidosResponse> call2 = apiService.getPartidosPorLigaTemporada(idLiga, firstYear);
                call2.enqueue(new Callback<PartidosResponse>() {
                    @Override
                    public void onResponse(Call<PartidosResponse> call, Response<PartidosResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<com.example.footballscore.entidades.Response> partidos = response.body().getResponse();
                            List<JornadaDTO> jornadas = new ArrayList<>();

                            if (partidos.size() > 0) {


                                for (com.example.footballscore.entidades.Response partido : partidos) {

                                    String jornada = partido.getLeague().getRound();


                                    JornadaDTO mes = null;
                                    for (JornadaDTO l : jornadas) {
                                        if (l.getJornada().equalsIgnoreCase(jornada)) {
                                            mes = l;
                                            break;
                                        }
                                    }

                                    if (mes == null) {
                                        mes = new JornadaDTO(jornada);
                                        jornadas.add(mes);
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

                                adaptador = new AdaptadorListaJornadasPartidosLiga(requireActivity());
                                recyclerView.setAdapter(adaptador);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                adaptador.agregarListaPosiciones(jornadas);
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





        return rootView;
    }





}