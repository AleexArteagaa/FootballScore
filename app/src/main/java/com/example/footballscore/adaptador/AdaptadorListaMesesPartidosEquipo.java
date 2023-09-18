package com.example.footballscore.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.MesDTO;
import com.example.footballscore.entidades.PartidoDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdaptadorListaMesesPartidosEquipo extends RecyclerView.Adapter<AdaptadorListaMesesPartidosEquipo.ViewHolder>{

    private ArrayList<MesDTO> datosPartidos;
    private Context contexto;

    public AdaptadorListaMesesPartidosEquipo(Context context){
        this.contexto = context;
        this.datosPartidos = new ArrayList<>();

    }

    public void agregarListaPosiciones(List<MesDTO> lista) {
        // Ordenar la lista de meses utilizando un comparador personalizado
        Collections.sort(lista, new Comparator<MesDTO>() {
            private final String[] mesesOrdenados = {"Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"};

            @Override
            public int compare(MesDTO mes1, MesDTO mes2) {
                int index1 = getIndex(mes1.getMes());
                int index2 = getIndex(mes2.getMes());
                return Integer.compare(index1, index2);
            }

            private int getIndex(String mes) {
                for (int i = 0; i < mesesOrdenados.length; i++) {
                    if (mes.equalsIgnoreCase(mesesOrdenados[i])) {
                        return i;
                    }
                }
                return -1; // Si el mes no está en la lista, se coloca al final
            }
        });

        datosPartidos.addAll(lista);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdaptadorListaMesesPartidosEquipo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_partidos_por_mes, parent, false);
        return new AdaptadorListaMesesPartidosEquipo.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaMesesPartidosEquipo.ViewHolder holder, int position) {

        MesDTO fichajesPorFecha = datosPartidos.get(position);


        String mes = fichajesPorFecha.getMes();
        mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);

        holder.setMes(mes);


        ArrayList<PartidoDTO> fichajes = fichajesPorFecha.getPartidos();

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(contexto));
        AdaptadorListaPartidosPorMes adaptadorListaPartidosPorMes = new AdaptadorListaPartidosPorMes(contexto, fichajes);
        holder.recyclerView.setAdapter(adaptadorListaPartidosPorMes);

    }



    @Override
    public int getItemCount() {
        return datosPartidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMes;

        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewMes = itemView.findViewById(R.id.textViewMesPartidos);
            recyclerView = itemView.findViewById(R.id.recyclerViewPartidosPorMes);
        }

        public void setMes(String mes) {
            textViewMes.setText(mes);
        }



    }
}
