package com.example.footballscore.fragments.equipos;

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
import com.example.footballscore.adaptador.AdaptadorListaCompeticionesPorEquipo;
import com.example.footballscore.adaptador.AdaptadorListaFechasFichajes;
import com.example.footballscore.entidades.Fichaje;
import com.example.footballscore.entidades.FichajesPorFecha;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.fichajes.FichajesResponse;
import com.example.footballscore.entidades.fichajes.Transfers;
import com.example.footballscore.entidades.partidos.league.League;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FichajesFragment extends Fragment {

    int idEquipo;
    Spinner spinner;
    RecyclerView recyclerView;

    AdaptadorListaFechasFichajes adaptador;

    public FichajesFragment(int idEquipo){
        this.idEquipo = idEquipo;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fichajes_equipo, container, false);

        spinner= view.findViewById(R.id.spinnerSeleccionAnio);
        recyclerView = view.findViewById(R.id.recyclerViewFichajesPorEquipoYAnio);

        adaptador = new AdaptadorListaFechasFichajes(requireActivity());
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Set<String> temporadas = new TreeSet<>(Collections.reverseOrder());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Call<FichajesResponse> call = apiService.getFichajesPorEquipo(idEquipo);

        call.enqueue(new Callback<FichajesResponse>() {
            @Override
            public void onResponse(Call<FichajesResponse> call, Response<FichajesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> fichajes = response.body().getResponse();
                    List<FichajesPorFecha> fichajesPorFecha = new ArrayList<>();
                    if (fichajes.size() > 0) {

                        for (int i = 0; i < fichajes.size(); i++) {
                            List<Transfers> fichaje = fichajes.get(i).getTransfers();

                            for (int j = 0; j < fichaje.size(); j++) {
                                String fecha = fichaje.get(j).getDate();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                try {
                                    Date date = dateFormat.parse(fecha);
                                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                                    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");

                                    int year = Integer.parseInt(yearFormat.format(date));
                                    int month = Integer.parseInt(monthFormat.format(date));

                                    String temporada;
                                    if (month >= 7) {
                                        temporada = year + "/" + (year + 1);
                                    } else {
                                        temporada = (year - 1) + "/" + year;
                                    }

                                    temporadas.add(temporada);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        for (String temporada : temporadas) {
                            adapter.add(temporada);
                        }

                        int index = adapter.getPosition("2022/2023");
                        if (index >= 0) {
                            spinner.setSelection(index);
                        }
                    }else {
                        spinner.setVisibility(View.GONE);

                        TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponible);

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
                public void onFailure (Call < FichajesResponse > call, Throwable t){
                    t.printStackTrace();
                }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                Call<FichajesResponse> call2 = apiService.getFichajesPorEquipo(idEquipo);
                call2.enqueue(new Callback<FichajesResponse>() {
                    @Override
                    public void onResponse(Call<FichajesResponse> call, Response<FichajesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<com.example.footballscore.entidades.Response> fichajes = response.body().getResponse();
                            List<FichajesPorFecha> fichajesPorFecha = new ArrayList<>();

                            if (fichajes.size() > 0) {

                                for (int i = 0; i < fichajes.size(); i++) {
                                    List<Transfers> fichaje = fichajes.get(i).getTransfers();
                                    int id = fichajes.get(i).getPlayer().getId();
                                    String jugador = fichajes.get(i).getPlayer().getName();

                                    for (int j = 0; j < fichaje.size(); j++) {
                                        String fecha = fichaje.get(j).getDate();
                                        String tipo = fichaje.get(j).getType();

                                        int idEquipoAlQueVa = fichaje.get(j).getTeams().getIn().getId();
                                        String logoEquipoAlQueVa = fichaje.get(j).getTeams().getIn().getLogo();
                                        String equipoAlQueVa = fichaje.get(j).getTeams().getIn().getName();
                                        int idEquipoDelQueViene = fichaje.get(j).getTeams().getOut().getId();
                                        String logoEquipoDelQueViene = fichaje.get(j).getTeams().getOut().getLogo();
                                        String equipoDelQueViene = fichaje.get(j).getTeams().getOut().getName();

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                        try {
                                            Date date = dateFormat.parse(fecha);

                                            // Definir la temporada según el criterio
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(date);

                                            int year = calendar.get(Calendar.YEAR);
                                            int month = calendar.get(Calendar.MONTH) + 1; // El mes es zero-based, se le suma 1
                                            int day = calendar.get(Calendar.DAY_OF_MONTH);

                                            int startYear;
                                            int endYear;
                                            if (month < 6 || (month == 6 && day <= 1)) {
                                                // Si el mes es anterior a julio o es julio pero el día es 1 o anterior, la temporada es del año anterior
                                                startYear = year - 1;
                                                endYear = year;
                                            } else {
                                                // De lo contrario, la temporada es del año actual al siguiente
                                                startYear = year;
                                                endYear = year + 1;
                                            }

                                            String temporada = startYear + "/" + endYear;

                                            if (temporada.equalsIgnoreCase(selectedItem)) {
                                                ArrayList<Fichaje> fichajesEnDia = null;

                                                for (FichajesPorFecha fichajesFecha : fichajesPorFecha) {
                                                    if (fichajesFecha.getFecha().equals(fecha)) {
                                                        fichajesEnDia = fichajesFecha.getFichajes();
                                                        break;
                                                    }
                                                }

                                                if (fichajesEnDia == null) {
                                                    fichajesEnDia = new ArrayList<>();
                                                    fichajesPorFecha.add(new FichajesPorFecha(fecha, fichajesEnDia));
                                                }

                                                Fichaje fichajeObj = new Fichaje(id, jugador, fecha, tipo, idEquipoAlQueVa, logoEquipoAlQueVa, equipoAlQueVa, idEquipoDelQueViene, logoEquipoDelQueViene, equipoDelQueViene);
                                                fichajesEnDia.add(fichajeObj);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                adaptador = new AdaptadorListaFechasFichajes(requireActivity());
                                recyclerView.setAdapter(adaptador);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                adaptador.agregarListaPosiciones(fichajesPorFecha);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FichajesResponse> call, Throwable t) {
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
