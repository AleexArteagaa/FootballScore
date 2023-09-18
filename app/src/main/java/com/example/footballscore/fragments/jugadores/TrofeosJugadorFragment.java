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
import com.example.footballscore.adaptador.AdaptadorTemporadasTrofeos;
import com.example.footballscore.entidades.ResponseTrofeos;
import com.example.footballscore.entidades.TemporadaDTO;
import com.example.footballscore.entidades.TrofeoDTO;
import com.example.footballscore.entidades.trofeos.TrofeosResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TrofeosJugadorFragment extends Fragment {

    private int idJugador;
    private RecyclerView recyclerView;
    private AdaptadorTemporadasTrofeos adaptador;

    public TrofeosJugadorFragment(int idJugador){
        this.idJugador = idJugador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trofeos_jugador, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTrofeosJugador);

        adaptador = new AdaptadorTemporadasTrofeos(requireActivity());
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<TrofeosResponse> call2 = apiService.getTrofeosJugador(idJugador);
        call2.enqueue(new Callback<TrofeosResponse>() {
            @Override
            public void onResponse(Call<TrofeosResponse> call, Response<TrofeosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResponseTrofeos> trofeos = response.body().getResponse();

                    if (trofeos.size() > 0) {


                        List<TemporadaDTO> temporadas = new ArrayList<>();

                        for (ResponseTrofeos trofeo : trofeos) {
                            String temporada = trofeo.getSeason();
                            String nombreTrofeo = trofeo.getLeague();
                            String pais = trofeo.getCountry();
                            String puesto = trofeo.getPlace();

                            TemporadaDTO temporadaDTO = null;
                            for (TemporadaDTO temp : temporadas) {
                                if (temp.getTemporada().equals(temporada)) {
                                    temporadaDTO = temp;
                                    break;
                                }
                            }

                            if (temporadaDTO == null) {

                                temporadaDTO = new TemporadaDTO(temporada);
                                temporadas.add(temporadaDTO);
                            }

                            TrofeoDTO trofeoDTO = new TrofeoDTO(nombreTrofeo, pais, temporada, puesto);
                            temporadaDTO.getTrofeos().add(trofeoDTO);
                        }

                        adaptador.agregarListaTemporadas(temporadas);
                    }else {
                        TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleTrofeosJugador);

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
            public void onFailure(Call<TrofeosResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        return view;
    }

    }
