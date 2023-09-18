package com.example.footballscore.fragments.jugadores;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.footballscore.R;
import com.example.footballscore.Traductor;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.jugadores.TemporadasResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InformacionJugadorFragment extends Fragment {

    private int idJugador;
    private TextView textViewNombreCompleto;
    private TextView textViewNombre;
    private TextView textViewEdad;
    private TextView textViewPeso;
    private TextView textViewAltura;
    private TextView textViewFechaNacimiento;
    private TextView textViewPaisNacimiento;
    private TextView textViewCiudadNacimiento;
    private TextView textViewPosicion;
    private LinearLayout layoutPeso;
    private LinearLayout layoutAltura;
    private LinearLayout layoutEdad;
    private LinearLayout layoutFechaNacimiento;
    private LinearLayout layoutPaisNacimiento;
    private LinearLayout layoutCiudadNacimiento;
    private LinearLayout layoutsInformacion;
    private LinearLayout layoutPesoEdadAltura;


    int ultimaTemporada;


    public InformacionJugadorFragment(int idJugador){
        this.idJugador = idJugador;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacion_jugador, container, false);

        textViewNombreCompleto = view.findViewById(R.id.textViewNombreCompletoJugador);
        textViewNombre = view.findViewById(R.id.textViewNombreJugadorInformacion);
        textViewEdad = view.findViewById(R.id.textViewEdadJugadorInformacion);
        textViewPeso = view.findViewById(R.id.textViewPesoJugadorInformacion);
        textViewAltura = view.findViewById(R.id.textViewAlturaJugadorInformacion);
        textViewFechaNacimiento = view.findViewById(R.id.textViewFechaNacimiento);
        textViewPaisNacimiento = view.findViewById(R.id.textViewPaisNacimiento);
        textViewCiudadNacimiento = view.findViewById(R.id.textViewCiudadNacimiento);
        textViewPosicion = view.findViewById(R.id.textViewPosicionJugadorInformacion);
        layoutEdad = view.findViewById(R.id.layoutEdad);
        layoutAltura = view.findViewById(R.id.layoutAltura);
        layoutPeso = view.findViewById(R.id.layoutPeso);
        layoutCiudadNacimiento = view.findViewById(R.id.layoutCiudadNacimiento);
        layoutFechaNacimiento = view.findViewById(R.id.layoutFechaNacimiento);
        layoutPaisNacimiento = view.findViewById(R.id.layoutPaisNacimiento);
        layoutsInformacion = view.findViewById(R.id.layoutsInformacion);
        layoutPesoEdadAltura = view.findViewById(R.id.layoutPesoEdadAlturaJugador);


        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);


        Call<TemporadasResponse> callLiga = apiService.getTemporadasPorJugador(idJugador);
        callLiga.enqueue(new Callback<TemporadasResponse>() {
            @Override
            public void onResponse(Call<TemporadasResponse> call, Response<TemporadasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Integer> temporadas = response.body().getResponse();

                    if (temporadas.size() > 0) {


                        ultimaTemporada = temporadas.get(temporadas.size() - 1);

                        Call<JugadoresResponse> call2 = apiService.getEstadisticasPorJugadorTemporada(idJugador, ultimaTemporada);
                        call2.enqueue(new Callback<JugadoresResponse>() {
                            @Override
                            public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    List<com.example.footballscore.entidades.Response> jugador = response.body().getResponse();

                                    String nombreCompleto = jugador.get(0).getPlayer().getFirstname() + " " + jugador.get(0).getPlayer().getLastname();
                                    if (nombreCompleto != null) {
                                        textViewNombreCompleto.setText(nombreCompleto);
                                    } else {
                                        textViewNombreCompleto.setVisibility(View.GONE);
                                    }
                                    if (jugador.get(0).getPlayer().getName() != null) {
                                        textViewNombre.setText(jugador.get(0).getPlayer().getName());
                                    } else {
                                        textViewNombre.setVisibility(View.GONE);
                                    }
                                    if (jugador.get(0).getPlayer().getAge() != 0 && jugador.get(0).getPlayer().getHeight() != null && jugador.get(0).getPlayer().getWeight() != null) {

                                        if (jugador.get(0).getPlayer().getAge() != 0) {
                                            textViewEdad.setText(jugador.get(0).getPlayer().getAge() + "");
                                        } else {
                                            layoutEdad.setVisibility(View.GONE);
                                        }
                                        if (jugador.get(0).getPlayer().getHeight() != null) {
                                            String altura = jugador.get(0).getPlayer().getHeight();
                                            altura = altura.replaceAll(" cm", "");
                                            textViewAltura.setText(altura);
                                        } else {
                                            layoutAltura.setVisibility(View.GONE);
                                        }
                                        if (jugador.get(0).getPlayer().getWeight() != null) {
                                            String peso = jugador.get(0).getPlayer().getWeight();
                                            peso = peso.replaceAll(" kg", "");
                                            textViewPeso.setText(peso);
                                        } else {
                                            layoutPeso.setVisibility(View.GONE);
                                        }
                                    }else {
                                        layoutPesoEdadAltura.setVisibility(View.GONE);

                                    }
                                    if (jugador.get(0).getPlayer().getBirth().getDate() != null) {
                                        String fechaNacimiento = jugador.get(0).getPlayer().getBirth().getDate();
                                        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat formatoSalida = new SimpleDateFormat("d MMMM yyyy", new Locale("es"));
                                        Date fecha;
                                        String fechaFormateada = "";
                                        try {
                                            fecha = formatoEntrada.parse(fechaNacimiento);
                                            fechaFormateada = formatoSalida.format(fecha);

                                            String mes = fechaFormateada.split(" ")[1];
                                            String mesCapitalizado = mes.substring(0, 1).toUpperCase() + mes.substring(1);
                                            fechaFormateada = fechaFormateada.replace(mes, mesCapitalizado);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        textViewFechaNacimiento.setText(fechaFormateada);
                                    } else {
                                        layoutFechaNacimiento.setVisibility(View.GONE);
                                    }
                                    if (jugador.get(0).getPlayer().getBirth().getCountry() != null) {
                                        String paisIngles = jugador.get(0).getPlayer().getBirth().getCountry();
                                        Traductor traductor = new Traductor();
                                        String paisTraducido = traductor.traducir(paisIngles);
                                        textViewPaisNacimiento.setText(paisTraducido);
                                    } else {
                                        layoutPaisNacimiento.setVisibility(View.GONE);
                                    }
                                    if (jugador.get(0).getPlayer().getBirth().getPlace() != null) {
                                        textViewCiudadNacimiento.setText(jugador.get(0).getPlayer().getBirth().getPlace());
                                    } else {
                                        layoutCiudadNacimiento.setVisibility(View.GONE);
                                    }
                                    if (jugador.get(0).getStatistics().get(0).getGames().getPosition() != null) {


                                        if (jugador.get(0).getStatistics().get(0).getGames().getPosition().equalsIgnoreCase("Attacker")) {
                                            textViewPosicion.setText("Delantero");
                                        } else if (jugador.get(0).getStatistics().get(0).getGames().getPosition().equalsIgnoreCase("Defender")) {
                                            textViewPosicion.setText("Defensa");
                                        } else if (jugador.get(0).getStatistics().get(0).getGames().getPosition().equalsIgnoreCase("Midfielder")) {
                                            textViewPosicion.setText("Mediocentro");
                                        } else if (jugador.get(0).getStatistics().get(0).getGames().getPosition().equalsIgnoreCase("Goalkeeper")) {
                                            textViewPosicion.setText("Portero");
                                        }
                                    } else {
                                        textViewPosicion.setVisibility(View.GONE);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    }else {
                        TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleInformacionJugador);

                        layoutsInformacion.setVisibility(View.GONE);

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

}
