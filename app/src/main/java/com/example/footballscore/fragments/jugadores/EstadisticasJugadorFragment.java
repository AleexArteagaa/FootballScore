package com.example.footballscore.fragments.jugadores;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
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

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorListaEstadisticasJugador;
import com.example.footballscore.adaptador.AdaptadorListaFechasFichajes;
import com.example.footballscore.entidades.Fichaje;
import com.example.footballscore.entidades.FichajesPorFecha;
import com.example.footballscore.entidades.fichajes.FichajesResponse;
import com.example.footballscore.entidades.fichajes.Transfers;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.jugadores.TemporadasResponse;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.entidades.partidos.estadisticas.Statistics;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

public class EstadisticasJugadorFragment extends Fragment {

    private int idJugador;
    private RecyclerView recyclerView;
    private int ultimaTemporada;
    private AdaptadorListaEstadisticasJugador adaptador;
    private Spinner spinner;



    public EstadisticasJugadorFragment(int idJugador){
        this.idJugador = idJugador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas_jugador, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEstadisticasJugador);
        spinner = view.findViewById(R.id.spinnerSeleccionTemporadaEstadisticasJugador);
        adaptador = new AdaptadorListaEstadisticasJugador(requireActivity());

        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Call<TemporadasResponse> call = apiService.getTemporadasPorJugador(idJugador);
        call.enqueue(new Callback<TemporadasResponse>() {
            @Override
            public void onResponse(Call<TemporadasResponse> call, Response<TemporadasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Integer> temporadas = response.body().getResponse();

                    if (temporadas.size() > 0) {

                        List<String> reversedList = new ArrayList<>();
                        for (int i = temporadas.size() - 1; i >= 0; i--) {
                            int anio = temporadas.get(i);
                            String anioFormato = anio + "/" + (anio + 1);
                            reversedList.add(anioFormato);
                        }

                        for (String elemento : reversedList) {
                            spinnerAdapter.add(elemento);
                        }

                        int index = spinnerAdapter.getPosition("2022/2023");
                        if (index >= 0) {
                            spinner.setSelection(index);
                        }
                    }else {
                        TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleEstadisticasJugador);

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

                Call<JugadoresResponse> call2 = apiService.getEstadisticasPorJugadorTemporada(idJugador, firstYear);
                call2.enqueue(new Callback<JugadoresResponse>() {
                    @Override
                    public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<com.example.footballscore.entidades.Response> estadisticas = response.body().getResponse();

                            List<Statistics> stats = estadisticas.get(0).getStatistics();
                            adaptador.agregarListaEstadisticas(stats);

                        }
                    }

                    @Override
                    public void onFailure(Call<JugadoresResponse> call, Throwable t) {

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
