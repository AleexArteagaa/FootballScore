package com.example.footballscore.fragments.ligas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.ClasificacionAdapter;
import com.example.footballscore.entidades.clasificaciones.Standings;
import com.example.footballscore.entidades.clasificaciones.StandingsResponse;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.competiciones.Seasons;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;


public class FragmentClasificacionDetalleLiga extends Fragment {
    private ClasificacionAdapter adaptador;
    int idLiga;
    RecyclerView recyclerView;
    Spinner spinnerGrupo;
    Spinner spinnerAnio;
    private LinearLayout layout;
    int selectedGroup = -1;
    int selectedYear = -1;



    public FragmentClasificacionDetalleLiga(int idLiga){
        this.idLiga = idLiga;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clasificacion, container, false);

        recyclerView = view.findViewById(R.id.recyclerClasificacion);
        spinnerGrupo = view.findViewById(R.id.spinnerSeleccionGrupoClasificacion);
        spinnerAnio = view.findViewById(R.id.spinnerSeleccionAnioClasificacion);
        layout = view.findViewById(R.id.layoutInfoClasificacion);

        adaptador = new ClasificacionAdapter(getContext());

        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayAdapter<String> adapterGrupo = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapterGrupo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupo.setAdapter(adapterGrupo);

        ArrayAdapter<String> adapterAnio = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        adapterAnio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnio.setAdapter(adapterAnio);

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService=retrofit.create(ServicioAPI.class);

        Call<LeagueResponse> call = apiService.getCompeticionesPorId(idLiga);
        call.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> liga = response.body().getResponse();

                    List<Seasons> temporadas = liga.get(0).getSeasons();

                    List<String> reversedList = new ArrayList<>();

                    for (int i = temporadas.size() - 1; i >= 0; i--) {
                        if (temporadas.get(i).getCoverage().isStandings()) {
                            int anio = temporadas.get(i).getYear();
                            String anioFormato = anio + "/" + (anio + 1);
                            reversedList.add(anioFormato);
                        }
                    }

                    ArrayAdapter<String> reversedAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, reversedList);
                    reversedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerAnio.setAdapter(reversedAdapter);

                    int index = reversedAdapter.getPosition("2022/2023");
                    if (index >= 0) {
                        spinnerAnio.setSelection(index);
                    }
                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Call<StandingsResponse>call2 = apiService.getClasificacion(idLiga,2022);
        call2.enqueue(new Callback<StandingsResponse>() {
            @Override
            public void onResponse(Call<StandingsResponse> call, retrofit2.Response<StandingsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> league = response.body().getResponse();
                    if (league.size() > 0) {

                        if (league.get(0).getLeague().getStandings().size() > 1) {
                            for (int i = 0; i < league.get(0).getLeague().getStandings().size(); i++) {
                                String grupo = "Grupo " + (i + 1);
                                adapterGrupo.add(grupo);
                            }

                            spinnerGrupo.setAdapter(adapterGrupo);
                        } else {
                            spinnerGrupo.setVisibility(View.GONE);
                            selectedGroup = 0;

                        }

                    }else {
                        TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleClasificacionLiga);

                        layout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        spinnerGrupo.setVisibility(View.GONE);
                        spinnerAnio.setVisibility(View.GONE);

                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                        txtMensajeNoDisponible.setVisibility(View.VISIBLE);

                        LinearLayout.LayoutParams txtMensajeLayoutParams = (LinearLayout.LayoutParams) txtMensajeNoDisponible.getLayoutParams();
                        txtMensajeLayoutParams.gravity = Gravity.CENTER;

                        txtMensajeNoDisponible.setText("Clasificacion no disponible");
                        txtMensajeNoDisponible.setLayoutParams(txtMensajeLayoutParams);
                    }

                }
            }

            @Override
            public void onFailure(Call<StandingsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getId() == R.id.spinnerSeleccionAnioClasificacion) {
                    String selectedItem = (String) spinnerAnio.getSelectedItem();
                    String[] years = selectedItem.split("/");
                    selectedYear = Integer.parseInt(years[0]);

                } else if (parent.getId() == R.id.spinnerSeleccionGrupoClasificacion) {
                    selectedGroup = spinnerGrupo.getSelectedItemPosition();
                }

                if (selectedGroup != -1 && selectedYear != -1) {

                    Call<StandingsResponse> call2 = apiService.getClasificacion(idLiga, selectedYear);
                    call2.enqueue(new Callback<StandingsResponse>() {
                        @Override
                        public void onResponse(Call<StandingsResponse> call, retrofit2.Response<StandingsResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<com.example.footballscore.entidades.Response> league = response.body().getResponse();
                                if (league.size() > 0) {
                                    List<Standings> clasificacion;
                                    if (selectedGroup == -1) {
                                        clasificacion = league.get(0).getLeague().getStandings().get(0);
                                    } else {
                                        clasificacion = league.get(0).getLeague().getStandings().get(selectedGroup);

                                    }

                                    adaptador.agregarListaClasificacion(clasificacion);
                                } else {
                                    TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleClasificacionLiga);

                                    recyclerView.setVisibility(View.GONE);
                                    spinnerAnio.setVisibility(View.GONE);
                                    spinnerGrupo.setVisibility(View.GONE);

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
                        public void onFailure(Call<StandingsResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinnerAnio.setOnItemSelectedListener(spinnerItemSelectedListener);
        spinnerGrupo.setOnItemSelectedListener(spinnerItemSelectedListener);

        return view;
    }
}