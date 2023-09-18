package com.example.footballscore.fragments.jugadores;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorLesionesJugador;
import com.example.footballscore.entidades.jugadores.TemporadasResponse;
import com.example.footballscore.entidades.lesiones.LesionesResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LesionesJugadorFragment  extends Fragment {

    private int idJugador;

    private RecyclerView recyclerView;
    private Spinner spinner;

    private AdaptadorLesionesJugador adaptador;

    public LesionesJugadorFragment(int idJugador){
        this.idJugador = idJugador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesiones_jugador, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLesionesJugador);
        spinner = view.findViewById(R.id.spinnerSeleccionTemporadaLesiones);

        adaptador = new AdaptadorLesionesJugador(requireActivity());
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> spinnerTemporadasList = new ArrayList<>();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<TemporadasResponse> call = apiService.getTemporadasPorJugador(idJugador);
        call.enqueue(new Callback<TemporadasResponse>() {
            @Override
            public void onResponse(Call<TemporadasResponse> call, Response<TemporadasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Integer> temporadas = response.body().getResponse();

                    if (temporadas.size() > 0) {
                        for (Integer temporada : temporadas) {
                            Call<LesionesResponse> call2 = apiService.getLesionesPorJugadorYTemporada(idJugador, temporada);
                            call2.enqueue(new Callback<LesionesResponse>() {
                                @Override
                                public void onResponse(Call<LesionesResponse> call, Response<LesionesResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        List<com.example.footballscore.entidades.Response> lesiones = response.body().getResponse();

                                        if (lesiones.size() > 0) {
                                            String season = temporada + "/" + (temporada + 1);

                                            if (!spinnerTemporadasList.contains(season)) {
                                                spinnerTemporadasList.add(season);
                                            }
                                        }

                                        if (spinnerAdapter.isEmpty()) {
                                            TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleLesionesJugador);

                                            recyclerView.setVisibility(View.GONE);
                                            spinner.setVisibility(View.GONE);

                                            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                                            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                                            txtMensajeNoDisponible.setVisibility(View.VISIBLE);

                                            LinearLayout.LayoutParams txtMensajeLayoutParams = (LinearLayout.LayoutParams) txtMensajeNoDisponible.getLayoutParams();
                                            txtMensajeLayoutParams.gravity = Gravity.CENTER;

                                            txtMensajeNoDisponible.setLayoutParams(txtMensajeLayoutParams);
                                        } else {
                                            TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleLesionesJugador);

                                            recyclerView.setVisibility(View.VISIBLE);
                                            spinner.setVisibility(View.VISIBLE);

                                            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                                            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                                            txtMensajeNoDisponible.setVisibility(View.GONE);

                                            LinearLayout.LayoutParams txtMensajeLayoutParams = (LinearLayout.LayoutParams) txtMensajeNoDisponible.getLayoutParams();
                                            txtMensajeLayoutParams.gravity = Gravity.CENTER;

                                            txtMensajeNoDisponible.setLayoutParams(txtMensajeLayoutParams);
                                        }

                                        Collections.sort(spinnerTemporadasList);

                                        spinnerAdapter.clear();

                                        for (String season : spinnerTemporadasList) {
                                            spinnerAdapter.add(season);
                                        }

                                        int index = spinnerAdapter.getPosition("2022/2023");
                                        if (index >= 0) {
                                            spinner.setSelection(index);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<LesionesResponse> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }
                    }
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
                String[] years = selectedItem.split("/");
                int firstYear = Integer.parseInt(years[0]);

                Call<LesionesResponse> call2 = apiService.getLesionesPorJugadorYTemporada(idJugador, firstYear);
                call2.enqueue(new Callback<LesionesResponse>() {
                    @Override
                    public void onResponse(Call<LesionesResponse> call, Response<LesionesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<com.example.footballscore.entidades.Response> lesiones = response.body().getResponse();

                            if (lesiones.size() > 0) {
                                adaptador.agregarListaLesiones(lesiones);
                            } else {
                                TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleLesionesJugador);

                                recyclerView.setVisibility(View.GONE);
                                spinner.setVisibility(View.GONE);

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
                    public void onFailure(Call<LesionesResponse> call, Throwable t) {
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
}
