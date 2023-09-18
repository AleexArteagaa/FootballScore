package com.example.footballscore.util;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.detallepartidos.alineaciones.AlineacionFragment;
import com.example.footballscore.detallepartidos.alineaciones.AlineacionLocalFragment;
import com.example.footballscore.detallepartidos.alineaciones.SubstitutesAdapter;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.entidades.apuestas.ApuestasResponse;
import com.example.footballscore.entidades.apuestas.Bookmakers;
import com.example.footballscore.entidades.clasificaciones.Standings;
import com.example.footballscore.entidades.clasificaciones.StandingsResponse;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.equipos.Team;
import com.example.footballscore.entidades.equipos.TeamResponse;
import com.example.footballscore.entidades.jugadores.Cards;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.lesiones.LesionesResponse;
import com.example.footballscore.entidades.partidos.PartidosResponse;
import com.example.footballscore.entidades.partidos.alineaciones.AlineacionesResponse;
import com.example.footballscore.entidades.partidos.alineaciones.Coach;
import com.example.footballscore.entidades.partidos.alineaciones.Player;
import com.example.footballscore.entidades.partidos.alineaciones.StartXI;
import com.example.footballscore.entidades.partidos.alineaciones.Substitutes;
import com.example.footballscore.entidades.partidos.estadisticas.EstadisticasResponse;
import com.example.footballscore.entidades.partidos.estadisticas.Statistics;
import com.example.footballscore.entidades.partidos.eventos.EventosResponse;
import com.example.footballscore.entidades.partidos.goals.Goals;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MetodosAPI {


    private ServicioAPI inicializacionConexionAPI(){
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .addHeader("X-RapidAPI-Key", "5b372f399amshb4514c44c8be65ep123ea3jsn7e198cf6b6d9")
                                .addHeader("X-RapidAPI-Host", "api-football-beta.p.rapidapi.com")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-football-beta.p.rapidapi.com/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        return apiService;
    }

    public void getClasificacion(ServicioAPI apiService){
        Call<StandingsResponse> call = apiService.getClasificacion(140,2022);
        call.enqueue(new Callback<StandingsResponse>() {
            @Override
            public void onResponse(Call<StandingsResponse> call, Response<StandingsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> league = response.body().getResponse();

                    List <Standings> clasificacion = league.get(0).getLeague().getStandings().get(0);

                    for (int i = 0; i < clasificacion.size(); i++) {

                        String equipo = clasificacion.get(i).getTeam().getName();
                        int posicion = clasificacion.get(i).getRank();
                        int puntos = clasificacion.get(i).getPoints();
                        int partidosJugados = clasificacion.get(i).getAll().getPlayed();
                        int partidosGanados = clasificacion.get(i).getAll().getWin();
                        int partidosEmpatados = clasificacion.get(i).getAll().getDraw();
                        int partidosPerdidos = clasificacion.get(i).getAll().getLose();
                        System.out.printf("%2d. %-30s %2d Jugados: %2d Ganados: %2d Empatados: %2d Perdidos: %2d%n",
                                posicion, equipo, puntos, partidosJugados, partidosGanados, partidosEmpatados, partidosPerdidos);
                    }


                }
            }

            @Override
            public void onFailure(Call<StandingsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCompeticionesPorNombre(ServicioAPI apiService) {
        Call<LeagueResponse> call = apiService.getCompeticionesPorNombre(2022,"La Liga");
        call.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> leagues = response.body().getResponse();
                    for (int i = 0; i < leagues.size(); i++) {
                        String name = leagues.get(i).getLeague().getName();
                        System.out.println(name);
                    }

                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCompeticionesPorEquipo(ServicioAPI apiService) {

        Call<LeagueResponse> call = apiService.getCompeticionesPorEquipoTemporada(2022,69);
        call.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> leagues = response.body().getResponse();
                    for (int i = 0; i < leagues.size(); i++) {
                        String name = leagues.get(i).getLeague().getLogo();
                        System.out.println(name);
                    }

                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCompeticionesPorPais(ServicioAPI apiService) {
        Call<LeagueResponse> call = apiService.getCompeticionesPorPais(2022,"Spain");
        call.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> leagues = response.body().getResponse();
                    for (int i = 0; i < leagues.size(); i++) {
                        String name = leagues.get(i).getLeague().getName();
                        int id = leagues.get(i).getLeague().getId();
                        System.out.println(name + " Id: " + id);
                    }

                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getEquipoPorNombre(ServicioAPI apiService) {
        Call<TeamResponse> call = apiService.getEquipoPorNombre("Manchester City",2022, 39);
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();
                    String name = teams.get(0).getVenue().getName();
                    System.out.println(name);

                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPartidosPorLigaTemporada(ServicioAPI apiService) {
        Call<PartidosResponse> call = apiService.getPartidosPorLigaTemporada(39,  2022);
        call.enqueue(new Callback<PartidosResponse>() {
            @Override
            public void onResponse(Call<PartidosResponse> call, Response<PartidosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();
                    for (int i = 0; i < teams.size(); i++) {

                        String equipoLocal = teams.get(i).getTeams().getHome().getName();
                        int resultadoLocal = teams.get(i).getScore().getFulltime().getHome();

                        String equipoVisitante = teams.get(i).getTeams().getAway().getName();
                        int resultadoVisitante = teams.get(i).getScore().getFulltime().getAway();
                        String jorada = teams.get(i).getLeague().getRound();
                        System.out.println(equipoLocal + " " + resultadoLocal + " - " + resultadoVisitante + " " + equipoVisitante + " Jornada " + jorada);

                    }
                }
            }

            @Override
            public void onFailure(Call<PartidosResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPartidosPorLigaJornadaTemporada(ServicioAPI apiService) {
        Call<PartidosResponse> call = apiService.getPartidosPorLigaJornadaTemporada(39, "Regular Season - 31", 2022);
        call.enqueue(new Callback<PartidosResponse>() {
            @Override
            public void onResponse(Call<PartidosResponse> call, Response<PartidosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();
                    for (int i = 0; i < teams.size(); i++) {

                        String equipoLocal = teams.get(i).getTeams().getHome().getName();
                        int resultadoLocal = teams.get(i).getScore().getFulltime().getHome();

                        String equipoVisitante = teams.get(i).getTeams().getAway().getName();
                        int resultadoVisitante = teams.get(i).getScore().getFulltime().getAway();
                        String jorada = teams.get(i).getLeague().getRound();
                        System.out.println(equipoLocal + " " + resultadoLocal + " - " + resultadoVisitante + " " + equipoVisitante + " Jornada " + jorada);

                    }
                }
            }

            @Override
            public void onFailure(Call<PartidosResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPartidosPorDia(ServicioAPI apiService) {
        Call<PartidosResponse> call = apiService.getPartidosPorDia("2023-04-17");
        call.enqueue(new Callback<PartidosResponse>() {
            @Override
            public void onResponse(Call<PartidosResponse> call, Response<PartidosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();
                    for (int i = 0; i < teams.size(); i++) {


                        String equipoLocal = teams.get(i).getTeams().getHome().getName();
                        int resultadoLocal = teams.get(i).getScore().getFulltime().getHome();

                        String equipoVisitante = teams.get(i).getTeams().getAway().getName();
                        int resultadoVisitante = teams.get(i).getScore().getFulltime().getAway();
                        String jorada = teams.get(i).getLeague().getRound();
                        System.out.println(equipoLocal + " " + resultadoLocal + " - " + resultadoVisitante + " " + equipoVisitante + " Jornada " + jorada);
                    }
                    //System.out.println(response.body().getResponse().toString());
                }
            }

            @Override
            public void onFailure(Call<PartidosResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getEquiposPorLiga(ServicioAPI apiService) {
        Call<TeamResponse> call = apiService.getEquiposPorLiga(39, 2022);
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();
                    for (int i = 0; i < teams.size(); i++) {
                        String name = teams.get(i).getVenue().getName();
                        System.out.println(name);

                    }
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void getApuestaGanadorPartido(ServicioAPI apiService){
        Call<ApuestasResponse> call = apiService.getApuestaGanadorPartido(865369);
        call.enqueue(new Callback<ApuestasResponse>() {
            @Override
            public void onResponse(Call<ApuestasResponse> call, Response<ApuestasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> apuestas = response.body().getResponse();


                    List<Bookmakers> apuesta = apuestas.get(0).getBookmakers();

                    String tipoApuesta = apuesta.get(0).getBets().get(0).getName();
                    String valorLocal = apuesta.get(0).getBets().get(0).getValues().get(0).getValue();
                    String valorEmpate = apuesta.get(0).getBets().get(0).getValues().get(1).getValue();
                    String valorVisitante = apuesta.get(0).getBets().get(0).getValues().get(2).getValue();
                    String cuotaLocal = apuesta.get(0).getBets().get(0).getValues().get(0).getOdd();
                    String cuotaEmpate = apuesta.get(0).getBets().get(0).getValues().get(1).getOdd();
                    String cuotaVisitante = apuesta.get(0).getBets().get(0).getValues().get(2).getOdd();

                    System.out.println(tipoApuesta);
                    System.out.println(valorLocal + ": " + cuotaLocal);
                    System.out.println(valorEmpate + ": " + cuotaEmpate);
                    System.out.println(valorVisitante + ": " + cuotaVisitante);

                }
            }

            @Override
            public void onFailure(Call<ApuestasResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private boolean botonSeleccionado = false;

    public void getAlineacionesPorPartido(ServicioAPI apiService, PartidoDTO partidoDTO, AlineacionFragment alineacionFragment, int actualPage) {
        final String[] portero = {""};
        ArrayList<Player> defensasL = new ArrayList<>();
        ArrayList<Player> mediosL = new ArrayList<>();
        ArrayList<Player> delanterosL = new ArrayList<>();
        ArrayList<Player> defensasV = new ArrayList<>();
        ArrayList<Player> mediosV = new ArrayList<>();
        ArrayList<Player> delanterosV = new ArrayList<>();
        final String[] formacionMostrar = {"", ""};
        Call<AlineacionesResponse> call = apiService.getAlineacionesPartido(partidoDTO.getId());
        call.enqueue(new Callback<AlineacionesResponse>() {
            @Override
            public void onResponse(Call<AlineacionesResponse> call, Response<AlineacionesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> alineaciones = response.body().getResponse();

                    if (actualPage == 0) {
                        Team equipo = alineaciones.get(actualPage).getTeam();
                        String nombreEquipo = equipo.getName();
                        String colorPrincipal = equipo.getColors().getPlayer().getPrimary();
                        Coach coach = alineaciones.get(actualPage).getCoach();
                        String nombreEntrenador = coach.getName();
                        String formacion = alineaciones.get(actualPage).getFormation();
                        formacionMostrar[0] = formacion;
                        String[] numPosCampo = formacion.split("-");
                        List<StartXI> titulares = alineaciones.get(actualPage).getStartXI();
                        List<Substitutes> suplentes = alineaciones.get(actualPage).getSubstitutes();

                        System.out.println("Equipo: " + nombreEquipo);
                        System.out.println("Entrenador: " + nombreEntrenador);
                        System.out.println("Color principal: " + colorPrincipal);
                        System.out.println("Formacion: " + formacion);
                        System.out.println("Titulares: ");


                        for (StartXI jugador : titulares) {
                            String nombreJugador = jugador.getPlayer().getName();
                            String posicionJugador = jugador.getPlayer().getPos();
                            Object dorsalJugador = jugador.getPlayer().getNumber();
                            String fotoJugador = jugador.getPlayer().getPhoto();

                            if (posicionJugador.equals("G")) {
                                portero[0] = jugador.getPlayer().getName();
                            } else if (posicionJugador.equals("D")) {
                                defensasL.add(jugador.getPlayer());
                            } else if (posicionJugador.equals("M")) {
                                mediosL.add(jugador.getPlayer());
                            } else if (posicionJugador.equals("F")) {
                                delanterosL.add(jugador.getPlayer());
                            }
                            System.out.println("Jugador: " + nombreJugador + " Posicion: " + posicionJugador + " Dorsal: " + dorsalJugador);
                        }
                        System.out.println("Suplentes: ");
                        for (Substitutes jugador : suplentes) {
                            String nombreJugador = jugador.getPlayer().getName();
                            String posicionJugador = jugador.getPlayer().getPos();

                            Object dorsalJugador = jugador.getPlayer().getNumber();
                            System.out.println("Jugador: " + nombreJugador + " Posicion: " + posicionJugador + " Dorsal: " + dorsalJugador);
                        }

                        Button btnLocal = alineacionFragment.getView().findViewById(R.id.alineacionLocalbtn);
                        if (btnLocal.isSelected()) {
                            TextView formacionText = alineacionFragment.getView().findViewById(R.id.estiloFormacion);
                            formacionText.setText(formacion);
                            LinearLayout layPortero = alineacionFragment.getView().findViewById(R.id.layPorteroLocal);
                            LinearLayout layDefensas = alineacionFragment.getView().findViewById(R.id.layDefensasLocal);
                            LinearLayout layMedios = alineacionFragment.getView().findViewById(R.id.layMediosLocal);
                            LinearLayout layDelanteros = alineacionFragment.getView().findViewById(R.id.layDelanterosLocal);
                            LinearLayout layNuevo = alineacionFragment.getView().findViewById(R.id.layAuxLocal);

                            layPortero.removeAllViews();
                            layDefensas.removeAllViews();
                            layMedios.removeAllViews();
                            layDelanteros.removeAllViews();
                            layNuevo.removeAllViews();

                            if (numPosCampo.length == 4) {
                                layNuevo.setVisibility(View.VISIBLE);
                                for (int x = 0; x < Integer.parseInt(numPosCampo[3]); x++) {
                                    FloatingActionButton btnNuevo = new FloatingActionButton(alineacionFragment.getContext());
                                    btnNuevo.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                                    layNuevo.addView(btnNuevo);
                                }
                            }


                            FloatingActionButton btnPortero = new FloatingActionButton(alineacionFragment.getContext());
                            btnPortero.setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
                            layPortero.addView(btnPortero);

                            for (int j = 0; j < Integer.parseInt(numPosCampo[0]); j++) {
                                FloatingActionButton btnDef = new FloatingActionButton(alineacionFragment.getContext());
                                btnDef.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                TextView dorsal= new TextView(alineacionFragment.getContext());
                                dorsal.setText( defensasL.get(j).getNumber()+"");
                                layDefensas.addView(btnDef);
                            }
                            for (int x = 0; x < Integer.parseInt(numPosCampo[1]); x++) {
                                FloatingActionButton btnMed = new FloatingActionButton(alineacionFragment.getContext());
                                btnMed.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                                layMedios.addView(btnMed);
                            }
                            for (int x = 0; x < Integer.parseInt(numPosCampo[2]); x++) {
                                FloatingActionButton btnDelantero = new FloatingActionButton(alineacionFragment.getContext());
                                btnDelantero.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                                layDelanteros.addView(btnDelantero);
                            }
                        }
                        Bundle listBundle= new Bundle();
                        listBundle.putParcelableArrayList("suplentesLocales", new ArrayList<>(suplentes));

                    } else if (actualPage == 1) {
                        Team equipo = alineaciones.get(actualPage).getTeam();
                        String nombreEquipo = equipo.getName();
                        String colorPrincipal = equipo.getColors().getPlayer().getPrimary();
                        Coach coach = alineaciones.get(actualPage).getCoach();
                        String nombreEntrenador = coach.getName();
                        String formacion = alineaciones.get(actualPage).getFormation();
                        formacionMostrar[1] = formacion;
                        String[] numPosCampo = formacion.split("-");
                        List<StartXI> titulares = alineaciones.get(actualPage).getStartXI();
                        List<Substitutes> suplentes = alineaciones.get(actualPage).getSubstitutes();

                        System.out.println("Equipo: " + nombreEquipo);
                        System.out.println("Entrenador: " + nombreEntrenador);
                        System.out.println("Color principal: " + colorPrincipal);
                        System.out.println("Formacion: " + formacion);
                        System.out.println("Titulares: ");

                        for (StartXI jugador : titulares) {
                            String nombreJugador = jugador.getPlayer().getName();
                            String posicionJugador = jugador.getPlayer().getPos();
                            Object dorsalJugador = jugador.getPlayer().getNumber();
                            String foto= jugador.getPlayer().getPhoto();
                            if (posicionJugador.equals("G")) {
                                portero[0] = jugador.getPlayer().getName();
                            } else if (posicionJugador.equals("D")) {
                                defensasV.add(jugador.getPlayer());
                            } else if (posicionJugador.equals("M")) {
                                mediosV.add(jugador.getPlayer());
                            } else if (posicionJugador.equals("F")) {
                                delanterosV.add(jugador.getPlayer());
                            }
                            System.out.println("Jugador: " + nombreJugador + " Posicion: " + posicionJugador + " Dorsal: " + dorsalJugador+ " Foto:"+foto);
                        }
                        System.out.println("Suplentes: ");
                        for (Substitutes jugador : suplentes) {
                            String nombreJugador = jugador.getPlayer().getName();
                            String posicionJugador = jugador.getPlayer().getPos();

                            Object dorsalJugador = jugador.getPlayer().getNumber();
                            System.out.println("Jugador: " + nombreJugador + " Posicion: " + posicionJugador + " Dorsal: " + dorsalJugador);

                        }
                        Button btnVisit = alineacionFragment.getView().findViewById(R.id.alineacionVisbtn);
                        if (btnVisit.isSelected()) {
                            TextView formacionText = alineacionFragment.getView().findViewById(R.id.estiloFormacionVisit);
                            formacionText.setText(formacion);
                            LinearLayout layPortero = alineacionFragment.getView().findViewById(R.id.layPortero);
                            LinearLayout layDefensas = alineacionFragment.getView().findViewById(R.id.layDefensas);
                            LinearLayout layMedios = alineacionFragment.getView().findViewById(R.id.layMedios);
                            LinearLayout layDelanteros = alineacionFragment.getView().findViewById(R.id.layDelanteros);
                            LinearLayout layNuevo = alineacionFragment.getView().findViewById(R.id.layAux);


                            layPortero.removeAllViews();
                            layDefensas.removeAllViews();
                            layMedios.removeAllViews();
                            layDelanteros.removeAllViews();
                            layNuevo.removeAllViews();

                            if (numPosCampo.length == 4) {
                                layNuevo.setVisibility(View.VISIBLE);
                                for (int x = 0; x < Integer.parseInt(numPosCampo[3]); x++) {
                                    FloatingActionButton btnNuevo = new FloatingActionButton(alineacionFragment.getContext());
                                    btnNuevo.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

                                    layNuevo.addView(btnNuevo);
                                }
                            }


                            FloatingActionButton btnPortero = new FloatingActionButton(alineacionFragment.getContext());
                            btnPortero.setBackgroundTintList(ColorStateList.valueOf(Color.MAGENTA));
                            layPortero.addView(btnPortero);

                            for (int j = 0; j < Integer.parseInt(numPosCampo[0]); j++) {
                                FloatingActionButton btnDef = new FloatingActionButton(alineacionFragment.getContext());
                                btnDef.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                //Players players= new Players();
                                //players.setId(defensasV.get(j).getId());
                                //ImageView image= new ImageView(layDefensas.getContext());
                                //Picasso.with(image.getContext()).load(players.getPhoto()).into(image);
                                //System.out.println("sss "+players.getPhoto());
                                layDefensas.addView(btnDef);
                            }
                            for (int x = 0; x < Integer.parseInt(numPosCampo[1]); x++) {
                                FloatingActionButton btnMed = new FloatingActionButton(alineacionFragment.getContext());
                                btnMed.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                                layMedios.addView(btnMed);
                            }
                            for (int x = 0; x < Integer.parseInt(numPosCampo[2]); x++) {
                                FloatingActionButton btnDelantero = new FloatingActionButton(alineacionFragment.getContext());
                                btnDelantero.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                                layDelanteros.addView(btnDelantero);
                            }

                        }

                    }
                }
            }


            @Override
            public void onFailure(Call<AlineacionesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void getEventosPorPartido(ServicioAPI apiService, PartidoDTO partidoDTO, LinearLayout eventosLayout) {
        Call<EventosResponse> call = apiService.getEventosPartido(partidoDTO.getId());
        call.enqueue(new Callback<EventosResponse>() {
            @Override
            public void onResponse(Call<EventosResponse> call, Response<EventosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> eventos = response.body().getResponse();

                    for (com.example.footballscore.entidades.Response evento : eventos) {
                        actualizarVistaEvento(evento, eventosLayout, partidoDTO);
                    }

                }
            }

            @Override
            public void onFailure(Call<EventosResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void actualizarVistaEvento(com.example.footballscore.entidades.Response evento, LinearLayout eventosLayout, PartidoDTO partidoDTO) {
        if (eventosLayout != null) {
            View eventoView = LayoutInflater.from(eventosLayout.getContext()).inflate(R.layout.vista_evento_individual, eventosLayout, false);

            LinearLayout linearEvento= eventoView.findViewById(R.id.linearEvento);

            TextView textTiempo = eventoView.findViewById(R.id.tiempoEvento);
            TextView textArriba = eventoView.findViewById(R.id.textArribaEvento);
            TextView textAbajo = eventoView.findViewById(R.id.textAbajoEvento);
            ImageView imageEvento = eventoView.findViewById(R.id.iconoEvento);

            int minuto = evento.getTime().getElapsed();
            String equipo = evento.getTeam().getName();
            String jugador = evento.getPlayer().getName();
            String tipo = evento.getType();

            System.out.println("Equipo: " + equipo);
            System.out.println("Jugador: " + jugador);
            System.out.println("Evento: " + tipo + " Minuto: " + minuto);

            textTiempo.setText(minuto + "'");
            if (tipo.equals("Goal")){
                imageEvento.setImageResource(R.drawable.balonevento);
                textArriba.setText(jugador);
                textAbajo.setText(evento.getAssist().getName()!=null ? "Asistencia: "+evento.getAssist().getName():"");
            } else if (tipo.equals("subst")) {
                imageEvento.setImageResource(R.drawable.sustitucion);
                textArriba.setText("Jugador Sustituido");
                textAbajo.setText(jugador);
            } else if (tipo.equals("Card")) {
                imageEvento.setImageResource(R.drawable.sancionadoamarilla);
                textArriba.setText("Sancionado");
                textAbajo.setText(jugador);
            }

            if (equipo.equals(partidoDTO.getHomeTeam())){
                linearEvento.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                linearEvento.setBackgroundColor(Color.parseColor("#4008FF00"));
            } else if (equipo.equals(partidoDTO.getAwayTeam())) {
                linearEvento.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                linearEvento.setBackgroundColor(Color.parseColor("#400022FF"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.gravity = Gravity.RIGHT;
                linearEvento.setLayoutParams(layoutParams);

            }

            eventosLayout.addView(eventoView);
        }
    }



    public void getEstadisticasPorPartido(ServicioAPI apiService, PartidoDTO partidoDTO, View view){
        TextView textStatsLocal = view.findViewById(R.id.textStatsLocal);
        TextView textStatsVisit = view.findViewById(R.id.textStatsVisit);
        ProgressBar progressStatsLocal = view.findViewById(R.id.progressStatsLocal);
        ProgressBar progressStatsVisitante = view.findViewById(R.id.progressStatsVisitante);

        TextView textStats2Local = view.findViewById(R.id.textStats2Local);
        TextView textStats2Visit = view.findViewById(R.id.textStats2Visit);
        ProgressBar progressStats2Local = view.findViewById(R.id.progressStats2Local);
        ProgressBar progressStats2Visitante = view.findViewById(R.id.progressStats2Visitante);

        TextView textStats3Local = view.findViewById(R.id.textStats3Local);
        TextView textStats3Visit = view.findViewById(R.id.textStats3Visit);
        ProgressBar progressStats3Local = view.findViewById(R.id.progressStats3Local);
        ProgressBar progressStats3Visitante = view.findViewById(R.id.progressStats3Visitante);

        TextView textStats4Local = view.findViewById(R.id.textStats4Local);
        TextView textStats4Visit = view.findViewById(R.id.textStats4Visit);
        ProgressBar progressStats4Local = view.findViewById(R.id.progressStats4Local);
        ProgressBar progressStats4Visitante = view.findViewById(R.id.progressStats4Visitante);

        TextView textStats5Local = view.findViewById(R.id.textStats5Local);
        TextView textStats5Visit = view.findViewById(R.id.textStats5Visit);
        ProgressBar progressStats5Local = view.findViewById(R.id.progressStats5Local);
        ProgressBar progressStats5Visitante = view.findViewById(R.id.progressStats5Visitante);

        TextView textAmarillasLocal = view.findViewById(R.id.textStats6Local);
        TextView textAmarillasVisit = view.findViewById(R.id.textStats6Visit);
        ProgressBar progressStats6Local = view.findViewById(R.id.progressStats6Local);
        ProgressBar progressStats6Visitante = view.findViewById(R.id.progressStats6Visitante);

        TextView textStats7Local = view.findViewById(R.id.textStats7Local);
        TextView textStats7Visit = view.findViewById(R.id.textStats7Visit);
        ProgressBar progressStats7Local = view.findViewById(R.id.progressStats7Local);
        ProgressBar progressStats7Visitante = view.findViewById(R.id.progressStats7Visitante);

        TextView textStats8Local = view.findViewById(R.id.textStats8Local);
        TextView textStats8Visit = view.findViewById(R.id.textStats8Visit);
        ProgressBar progressStats8Local = view.findViewById(R.id.progressStats8Local);
        ProgressBar progressStats8Visitante = view.findViewById(R.id.progressStats8Visitante);

        TextView textStats9Local = view.findViewById(R.id.textStats9Local);
        TextView textStats9Visit = view.findViewById(R.id.textStats9Visit);
        ProgressBar progressStats9Local = view.findViewById(R.id.progressStats9Local);
        ProgressBar progressStats9Visitante = view.findViewById(R.id.progressStats9Visitante);

        TextView textStats10Local = view.findViewById(R.id.textStats10Local);
        TextView textStats10Visit = view.findViewById(R.id.textStats10Visit);
        ProgressBar progressStats10Local = view.findViewById(R.id.progressStats10Local);
        ProgressBar progressStats10Visitante = view.findViewById(R.id.progressStats10Visitante);

        Call<EstadisticasResponse> call = apiService.getEstadisticasPartido(partidoDTO.getId());
        call.enqueue(new Callback<EstadisticasResponse>() {
            @Override
            public void onResponse(Call<EstadisticasResponse> call, Response<EstadisticasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> estadisticas = response.body().getResponse();

                    for (int i = 0; i < estadisticas.size(); i++) {
                        if (i == 0) {
                            String equipo = estadisticas.get(i).getTeam().getName();

                            System.out.println("Equipo: " + equipo);
                            List<Statistics> estadistica = estadisticas.get(i).getStatistics();

                            for (Statistics stat : estadistica) {
                                String tipo = stat.getType();
                                Object cantidad = stat.getValue();

                                if (tipo.equals("Total Shots")) {
                                    if (cantidad != null) {
                                        textStats4Local.setText(String.valueOf(cantidad));
                                        progressStats4Local.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats4Local.setText("0");
                                        progressStats4Local.setProgress(0);
                                    }
                                } else if (tipo.equals("Yellow Cards")) {
                                    if (cantidad != null) {
                                        textAmarillasLocal.setText(String.valueOf(cantidad));
                                        progressStats6Local.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textAmarillasLocal.setText("0");
                                        progressStats6Local.setProgress(0);
                                    }
                                } else if (tipo.equals("Red Cards")) {
                                    if (cantidad != null) {
                                        textStats5Local.setText(String.valueOf(cantidad));
                                        progressStats5Local.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats5Local.setText("0");
                                        progressStats5Local.setProgress(0);
                                    }
                                } else if (tipo.equals("Ball Possession")) {
                                    if (cantidad != null) {
                                        textStats7Local.setText(String.valueOf(cantidad));
                                        progressStats7Local.setProgress((int) Double.parseDouble(cantidad.toString().split("%")[0]));
                                    } else {
                                        textStats7Local.setText("0");
                                        progressStats7Local.setProgress(0);
                                    }
                                } else if (tipo.equals("Corner Kicks")) {
                                    if (cantidad != null) {
                                        textStats3Local.setText(String.valueOf(cantidad));
                                        progressStats3Local.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats3Local.setText("0");
                                        progressStats3Local.setProgress(0);
                                    }
                                } else if (tipo.equals("Offsides")) {
                                    if (cantidad != null) {
                                        textStats2Local.setText(String.valueOf(cantidad));
                                        progressStats2Local.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats2Local.setText("0");
                                        progressStats2Local.setProgress(0);
                                    }
                                } else if (tipo.equals("Goalkeeper Saves")) {
                                    if (cantidad != null) {
                                        textStats8Local.setText(String.valueOf(cantidad));
                                        progressStats8Local.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats8Local.setText("0");
                                        progressStats8Local.setProgress(0);
                                    }
                                } else if (tipo.equals("Fouls")) {
                                    if (cantidad != null) {
                                        textStatsLocal.setText(String.valueOf(cantidad));
                                        progressStatsLocal.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStatsLocal.setText("0");
                                        progressStatsLocal.setProgress(0);
                                    }
                                } else if (tipo.equals("Total passes")) {
                                    if (cantidad != null) {
                                        textStats9Local.setText(String.valueOf(cantidad));
                                        progressStats9Local.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats9Local.setText("0");
                                        progressStats9Local.setProgress(0);
                                    }
                                } else if (tipo.equals("Passes accurate")) {
                                    if (cantidad != null) {
                                        textStats10Local.setText(String.valueOf(cantidad));
                                        progressStats10Local.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats10Local.setText("0");
                                        progressStats10Local.setProgress(0);
                                    }
                                }

                                System.out.println(tipo + ": " + cantidad);
                            }
                        } else if (i == 1) {
                            String equipo = estadisticas.get(i).getTeam().getName();

                            System.out.println("Equipo: " + equipo);
                            List<Statistics> estadistica = estadisticas.get(i).getStatistics();

                            for (Statistics stat : estadistica) {
                                String tipo = stat.getType();
                                Object cantidad = stat.getValue();

                                if (tipo.equals("Total Shots")) {
                                    if (cantidad != null) {
                                        textStats4Visit.setText(String.valueOf(cantidad));
                                        progressStats4Visitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats4Visit.setText("0");
                                        progressStats4Visitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Yellow Cards")) {
                                    if (cantidad != null) {
                                        textAmarillasVisit.setText(String.valueOf(cantidad));
                                        progressStats6Visitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textAmarillasVisit.setText("0");
                                        progressStats6Visitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Red Cards")) {
                                    if (cantidad != null) {
                                        textStats5Visit.setText(String.valueOf(cantidad));
                                        progressStats5Visitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats5Visit.setText("0");
                                        progressStats5Visitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Ball Possession")) {
                                    if (cantidad != null) {
                                        textStats7Visit.setText(String.valueOf(cantidad));
                                        progressStats7Visitante.setProgress((int) Double.parseDouble(cantidad.toString().split("%")[0]));
                                    } else {
                                        textStats7Visit.setText("0");
                                        progressStats7Visitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Corner Kicks")) {
                                    if (cantidad != null) {
                                        textStats3Visit.setText(String.valueOf(cantidad));
                                        progressStats3Visitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats3Visit.setText("0");
                                        progressStats3Visitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Offsides")) {
                                    if (cantidad != null) {
                                        textStats2Visit.setText(String.valueOf(cantidad));
                                        progressStats2Visitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats2Visit.setText("0");
                                        progressStats2Visitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Goalkeeper Saves")) {
                                    if (cantidad != null) {
                                        textStats8Visit.setText(String.valueOf(cantidad));
                                        progressStats8Visitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats8Visit.setText("0");
                                        progressStats8Visitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Fouls")) {
                                    if (cantidad != null) {
                                        textStatsVisit.setText(String.valueOf(cantidad));
                                        progressStatsVisitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStatsVisit.setText("0");
                                        progressStatsVisitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Total passes")) {
                                    if (cantidad != null) {
                                        textStats9Visit.setText(String.valueOf(cantidad));
                                        progressStats9Visitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats9Visit.setText("0");
                                        progressStats9Visitante.setProgress(0);
                                    }
                                } else if (tipo.equals("Passes accurate")) {
                                    if (cantidad != null) {
                                        textStats10Visit.setText(String.valueOf(cantidad));
                                        progressStats10Visitante.setProgress((int) Double.parseDouble(cantidad.toString()));
                                    } else {
                                        textStats10Visit.setText("0");
                                        progressStats10Visitante.setProgress(0);
                                    }
                                }

                                System.out.println(tipo + ": " + cantidad);
                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<EstadisticasResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getMaximoGoleadorLigaTemporada(ServicioAPI apiService){
        Call<JugadoresResponse> call = apiService.getMaximoGoleadorPorLigaTemporada(140, 2022);
        call.enqueue(new Callback<JugadoresResponse>() {
            @Override
            public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> estadisticas = response.body().getResponse();

                    for (int i = 0; i < estadisticas.size(); i++) {

                        String jugador = estadisticas.get(i).getPlayer().getName();

                        System.out.println("Jugador: " + jugador);
                        List<Statistics> estadistica = estadisticas.get(i).getStatistics();

                        for (Statistics stat: estadistica) {

                            Goals goles = stat.getGoals();
                            int totalGoles = goles.getTotal();

                            System.out.println("Goles: "+  totalGoles);

                        }

                    }

                }
            }
            @Override
            public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void getMaximoAsistenteLigaTemporada(ServicioAPI apiService){
        Call<JugadoresResponse> call = apiService.getMaximoAsistentePorLigaTemporada(140, 2022);
        call.enqueue(new Callback<JugadoresResponse>() {
            @Override
            public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> estadisticas = response.body().getResponse();

                    for (int i = 0; i < estadisticas.size(); i++) {

                        String jugador = estadisticas.get(i).getPlayer().getName();

                        System.out.println("Jugador: " + jugador);
                        List<Statistics> estadistica = estadisticas.get(i).getStatistics();

                        for (Statistics stat: estadistica) {

                            Goals asistencias = stat.getGoals();
                            int totalAsistencias = asistencias.getAssists();

                            System.out.println("Asistencias: "+  totalAsistencias);

                        }

                    }

                }
            }
            @Override
            public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void getMasTarjetasRojasLigaTemporada(ServicioAPI apiService){

        Call<JugadoresResponse> call = apiService.getMasTarjetasRojasPorLigaTemporada(140, 2022);
        call.enqueue(new Callback<JugadoresResponse>() {
            @Override
            public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> estadisticas = response.body().getResponse();

                    for (int i = 0; i < estadisticas.size(); i++) {

                        String jugador = estadisticas.get(i).getPlayer().getName();

                        System.out.println("Jugador: " + jugador);
                        List<Statistics> estadistica = estadisticas.get(i).getStatistics();

                        for (Statistics stat: estadistica) {

                            Cards tarjetas = stat.getCards();
                            int rojas = tarjetas.getRed();

                            System.out.println("Tarjetas Rojas: "+  rojas);

                        }

                    }

                }
            }
            @Override
            public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getMasTarjetasAmarillasLigaTemporada(ServicioAPI apiService){

        Call<JugadoresResponse> call = apiService.getMasTarjetasAmarillasPorLigaTemporada(140, 2022);
        call.enqueue(new Callback<JugadoresResponse>() {
            @Override
            public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> estadisticas = response.body().getResponse();

                    for (int i = 0; i < estadisticas.size(); i++) {

                        String jugador = estadisticas.get(i).getPlayer().getName();

                        System.out.println("Jugador: " + jugador);
                        List<Statistics> estadistica = estadisticas.get(i).getStatistics();

                        for (Statistics stat: estadistica) {

                            Cards tarjetas = stat.getCards();
                            int amarillas = tarjetas.getYellow();

                            System.out.println("Tarjetas Amarillas: "+  amarillas);

                        }

                    }

                }
            }
            @Override
            public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        }

    private void getLesionadosPorPartido(ServicioAPI apiService){
        Call<LesionesResponse> call = apiService.getLesionadosPorPartido(686308);
        call.enqueue(new Callback<LesionesResponse>() {
            @Override
            public void onResponse(Call<LesionesResponse> call, Response<LesionesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> lesionados = response.body().getResponse();

                    for (int i = 0; i < lesionados.size(); i++) {

                        String jugador = lesionados.get(i).getPlayer().getName();
                        String tipo = lesionados.get(i).getPlayer().getType();
                        String motivo = lesionados.get(i).getPlayer().getReason();

                        System.out.println("Jugador: " + jugador + " Tipo: " + tipo + " Motivo: " + motivo);


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
