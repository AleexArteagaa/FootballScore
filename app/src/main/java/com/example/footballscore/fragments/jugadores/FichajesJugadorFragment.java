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
import com.example.footballscore.adaptador.AdaptadorFichajesJugador;
import com.example.footballscore.entidades.fichajes.FichajesResponse;
import com.example.footballscore.entidades.fichajes.Transfers;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FichajesJugadorFragment  extends Fragment {

    private int idJugador;
    private RecyclerView recyclerView;
    private AdaptadorFichajesJugador adaptador;


    public FichajesJugadorFragment(int idJugador){
        this.idJugador = idJugador;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fichajes_jugador, container, false);

        recyclerView = view.findViewById(R.id.recyclreViewFichajesJugador);

        adaptador = new AdaptadorFichajesJugador(requireActivity());
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);
        Call<FichajesResponse> call2 = apiService.getFichajesJugador(idJugador);
        call2.enqueue(new Callback<FichajesResponse>() {
            @Override
            public void onResponse(Call<FichajesResponse> call, Response<FichajesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> fichajes = response.body().getResponse();

                    if (fichajes.size() > 0){

                    List<Transfers> fichajesJugador = fichajes.get(0).getTransfers();
                    adaptador.agregarListaFichajes(fichajesJugador);

                    }else {
                        TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponibleFichajesJugador);

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
            public void onFailure(Call<FichajesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });



        return view;
    }
}
