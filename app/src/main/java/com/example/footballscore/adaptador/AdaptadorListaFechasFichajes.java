package com.example.footballscore.adaptador;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.Traductor;
import com.example.footballscore.entidades.Fichaje;
import com.example.footballscore.entidades.FichajesPorFecha;
import com.example.footballscore.entidades.JugadorDTO;
import com.example.footballscore.entidades.PlantillaDTO;
import com.example.footballscore.entidades.PosicionDTO;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadorListaFechasFichajes extends RecyclerView.Adapter<AdaptadorListaFechasFichajes.ViewHolder>{

    private ArrayList<FichajesPorFecha> datosFichajes;
    private Context contexto;

    public AdaptadorListaFechasFichajes(Context context){
        this.contexto = context;
        this.datosFichajes = new ArrayList<>();

    }

    public void agregarListaPosiciones(List<FichajesPorFecha> lista) {
        Collections.sort(lista, new Comparator<FichajesPorFecha>() {
            @Override
            public int compare(FichajesPorFecha o1, FichajesPorFecha o2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date fecha1 = dateFormat.parse(o1.getFecha());
                    Date fecha2 = dateFormat.parse(o2.getFecha());
                    return fecha2.compareTo(fecha1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        datosFichajes.addAll(lista);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdaptadorListaFechasFichajes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_fichajes_por_dia, parent, false);
        return new AdaptadorListaFechasFichajes.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaFechasFichajes.ViewHolder holder, int position) {

        FichajesPorFecha fichajesPorFecha = datosFichajes.get(position);

        String fecha = fichajesPorFecha.getFecha();
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatoSalida = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

        Date date = null;
        try {
            date = formatoEntrada.parse(fecha);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String fechaFormateada = formatoSalida.format(date);
        DateFormatSymbols symbols = new DateFormatSymbols(new Locale("es", "ES"));
        String[] meses = symbols.getMonths();
        int mes = date.getMonth();
        String nombreMes = meses[mes];
        String nombreMesCapitalizado = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1);
        String fechaFormateadaCapitalizada = fechaFormateada.replace(nombreMes, nombreMesCapitalizado);

        holder.setFecha(fechaFormateadaCapitalizada);

        ArrayList<Fichaje> fichajes = fichajesPorFecha.getFichajes();

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(contexto));
        AdaptadorListaFichajes adaptadorListaFichajes = new AdaptadorListaFichajes(contexto, fichajes);
        holder.recyclerView.setAdapter(adaptadorListaFichajes);

    }



    @Override
    public int getItemCount() {
        return datosFichajes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewFecha;

        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFecha = itemView.findViewById(R.id.textViewFechaFichajes);
            recyclerView = itemView.findViewById(R.id.recyclerViewFichajesPorFecha);
        }

        public void setFecha(String fecha) {
            textViewFecha.setText(fecha);
        }



    }
}
