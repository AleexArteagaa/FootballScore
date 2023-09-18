package com.example.footballscore.fragments.equipos;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorListaPlantilla;
import com.example.footballscore.adaptador.AdaptadorListaPosiciones;
import com.example.footballscore.entidades.EntrenadorDTO;
import com.example.footballscore.entidades.JugadorDTO;
import com.example.footballscore.entidades.LigaDTO;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.entidades.PlantillaDTO;
import com.example.footballscore.entidades.PosicionDTO;
import com.example.footballscore.entidades.entrenadores.EntrenadoresResponse;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.lesiones.LesionesResponse;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlantillaFragment extends Fragment {

    int idEquipo;
    TextView nombreEntrenador;
    ImageView fotoEntrenador;
    RecyclerView recyclerView;
    String pais;
    String enlaceBandera;
    String enlaceBanderaJugador;

    public PlantillaFragment(int idEquipo){
        this.idEquipo = idEquipo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_plantilla_equipo, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPlantilla);
        TextView txtMensajeNoDisponible = view.findViewById(R.id.txtMensajeNoDisponiblePlantillaJugador);


        AdaptadorListaPlantilla adaptadorListaPlantilla = new AdaptadorListaPlantilla(requireContext());
        recyclerView.setAdapter(adaptadorListaPlantilla);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<EntrenadoresResponse> callEntrenador = apiService.getEntrenadorPorEquipo(idEquipo);
        callEntrenador.enqueue(new Callback<EntrenadoresResponse>() {
            @Override
            public void onResponse(Call<EntrenadoresResponse> call, Response<EntrenadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> entrenadores = response.body().getResponse();

                    if (entrenadores.size() > 0) {

                        ArrayList<EntrenadorDTO> entrenadoresList = new ArrayList<>();

                        for (int i = 0; i < entrenadores.size(); i++) {

                            String nombreEntrenador = entrenadores.get(i).getName();
                            int idEntrenador =  entrenadores.get(i).getId();
                            String fotoEntrenador =  entrenadores.get(i).getPhoto();
                            String paisEntrenador =  entrenadores.get(i).getNationality();

                            EntrenadorDTO manager = new EntrenadorDTO(nombreEntrenador,idEntrenador,  fotoEntrenador, paisEntrenador);
                            entrenadoresList.add(manager);

                        }

                            PlantillaDTO plantillaDTO = new PlantillaDTO(entrenadoresList);

                            Call<JugadoresResponse> callPlantilla = apiService.getPlantillaPorEquipo(idEquipo);
                            callPlantilla.enqueue(new Callback<JugadoresResponse>() {
                                @Override
                                public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        List<com.example.footballscore.entidades.Response> jugadores = response.body().getResponse();
                                        ArrayList<PosicionDTO> posiciones = new ArrayList<>();

                                        if (jugadores.size() > 0) {

                                            for (int i = 0; i < jugadores.get(0).getPlayers().size(); i++) {
                                                String nombrePosicion = jugadores.get(0).getPlayers().get(i).getPosition();

                                                PosicionDTO pos = null;
                                                for (PosicionDTO l : posiciones) {
                                                    if (l.getPosicion().equalsIgnoreCase(nombrePosicion)) {
                                                        pos = l;
                                                        break;
                                                    }
                                                }

                                                if (pos == null) {
                                                    pos = new PosicionDTO(nombrePosicion);
                                                    posiciones.add(pos);
                                                }

                                                JugadorDTO jugadorDTO = new JugadorDTO(
                                                        jugadores.get(0).getPlayers().get(i).getId(),
                                                        jugadores.get(0).getPlayers().get(i).getName(),
                                                        jugadores.get(0).getPlayers().get(i).getAge(),
                                                        jugadores.get(0).getPlayers().get(i).getNumber(),
                                                        jugadores.get(0).getPlayers().get(i).getPosition(),
                                                        jugadores.get(0).getPlayers().get(i).getPhoto()
                                                );

                                                pos.getJugadores().add(jugadorDTO);
                                            }

                                            plantillaDTO.setPosiciones(posiciones);
                                        }else {

                                            adaptadorListaPlantilla.ocultaRecyclerView(true);

                                            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                                            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                                            txtMensajeNoDisponible.setVisibility(View.VISIBLE);

                                            LinearLayout.LayoutParams txtMensajeLayoutParams = (LinearLayout.LayoutParams) txtMensajeNoDisponible.getLayoutParams();
                                            txtMensajeLayoutParams.gravity = Gravity.CENTER;

                                            txtMensajeNoDisponible.setLayoutParams(txtMensajeLayoutParams);
                                        }
                                        adaptadorListaPlantilla.agregarListaPosiciones(plantillaDTO);

                                    }
                                }

                                @Override
                                public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }

                    }else {

                        recyclerView.setVisibility(View.GONE);

                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                        txtMensajeNoDisponible.setVisibility(View.VISIBLE);

                        LinearLayout.LayoutParams txtMensajeLayoutParams = (LinearLayout.LayoutParams) txtMensajeNoDisponible.getLayoutParams();
                        txtMensajeLayoutParams.gravity = Gravity.CENTER;

                        txtMensajeNoDisponible.setLayoutParams(txtMensajeLayoutParams);
                    }

                }

            @Override
            public void onFailure(Call<EntrenadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        return view;
    }

}
