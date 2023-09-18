package com.example.footballscore.fragments.ligas;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.EstadiosAdapter;
import com.example.footballscore.entidades.Response;
import com.example.footballscore.entidades.equipos.TeamResponse;
import com.example.footballscore.entidades.equipos.Venue;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class FragmentEstadiosDetalleLiga extends Fragment {

    private EstadiosAdapter adaptador;
    int idLiga;
    RecyclerView recyclerView;

    public FragmentEstadiosDetalleLiga (int idLiga){
        this.idLiga = idLiga;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_estadios, container, false);
        recyclerView = view.findViewById(R.id.recyclerEstadios);

        adaptador = new EstadiosAdapter(getContext());

        recyclerView.setAdapter(adaptador);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService=retrofit.create(ServicioAPI.class);
        Call<TeamResponse> call=apiService.getEquiposPorLiga(idLiga , 2022);
        call.enqueue(new Callback<TeamResponse>() {
            public void onResponse(Call<TeamResponse> call, retrofit2.Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();
                    if (teams.size() > 0) {
                        List<Venue> estadiosList = new ArrayList<>();
                        boolean todosEstadiosConIdCero = true;

                        for (Response estadios : teams) {

                            estadiosList.add(estadios.getVenue());

                            if (estadios.getVenue().getId() != 0) {
                                todosEstadiosConIdCero = false;
                            }
                        }

                        if (!todosEstadiosConIdCero) {
                            adaptador.agregarEstadio(estadiosList);
                        }else {
                            TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleEstadioLiga);

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
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return view;
    }
}