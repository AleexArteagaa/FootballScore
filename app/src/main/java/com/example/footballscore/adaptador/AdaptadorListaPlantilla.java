package com.example.footballscore.adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.DetalleEntrenadores;
import com.example.footballscore.DetalleJugadores;
import com.example.footballscore.R;
import com.example.footballscore.entidades.EntrenadorDTO;
import com.example.footballscore.entidades.PlantillaDTO;
import com.example.footballscore.entidades.PosicionDTO;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class AdaptadorListaPlantilla extends RecyclerView.Adapter<AdaptadorListaPlantilla.ViewHolder>{

    private ArrayList<PlantillaDTO> datosPlantilla;
    private Context contexto;

    private boolean ocultar;

    public AdaptadorListaPlantilla(Context context){
        this.contexto = context;
        datosPlantilla = new ArrayList<>();

    }


    @NonNull
    @Override
    public AdaptadorListaPlantilla.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_entrenador, parent, false);
        return new AdaptadorListaPlantilla.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaPlantilla.ViewHolder holder, int position) {

        PlantillaDTO plantillaDTO = datosPlantilla.get(position);



        ArrayList<PosicionDTO> posiciones = plantillaDTO.getPosiciones();
        ArrayList<EntrenadorDTO> entrenadores = plantillaDTO.getEntrenadores();


        holder.recyclerView.setLayoutManager(new LinearLayoutManager(contexto));
        AdaptadorListaPosiciones adaptadorListaJugadores = new AdaptadorListaPosiciones(contexto, posiciones);
        holder.recyclerView.setAdapter(adaptadorListaJugadores);

        holder.recyclerViewEntrenadores.setLayoutManager(new LinearLayoutManager(contexto));
        AdaptadorListaEntrenadores adaptadorListaEntrenadores = new AdaptadorListaEntrenadores(contexto, entrenadores);
        holder.recyclerViewEntrenadores.setAdapter(adaptadorListaEntrenadores);

    }

    public void agregarListaPosiciones(PlantillaDTO lista) {
        datosPlantilla.add(lista);

        notifyDataSetChanged();
    }

    public void ocultaRecyclerView(boolean ocultar) {

        this.ocultar = ocultar;

    }


    @Override
    public int getItemCount() {
        return datosPlantilla.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {




        private RecyclerView recyclerView;
        private RecyclerView recyclerViewEntrenadores;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recyclerViewPosicionesPorEquipo);
            recyclerViewEntrenadores = itemView.findViewById(R.id.recyclerViewEntrenadores);

        }



    }
}
