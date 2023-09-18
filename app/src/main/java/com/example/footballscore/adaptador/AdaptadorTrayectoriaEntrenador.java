package com.example.footballscore.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.FichajesPorFecha;
import com.example.footballscore.entidades.entrenadores.Career;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdaptadorTrayectoriaEntrenador extends RecyclerView.Adapter<AdaptadorTrayectoriaEntrenador.ViewHolder>{

    private Context contexto;
    private ArrayList<Career> datosTrayectoria;
    private boolean showShimmer = true;


    public AdaptadorTrayectoriaEntrenador(Context contexto) {
        this.contexto = contexto;
        datosTrayectoria = new ArrayList<>();

    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorTrayectoriaEntrenador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_trayectoria_entrenador, parent, false);
        return new AdaptadorTrayectoriaEntrenador.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTrayectoriaEntrenador.ViewHolder holder, int position) {

        Career trayectoria = datosTrayectoria.get(position);

        holder.setImagenLogo(trayectoria.getTeam().getLogo());
        holder.setNombreEquipo(trayectoria.getTeam().getName());

        String fechaInicio = trayectoria.getStart();
        String fechaFinal = trayectoria.getEnd();


        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatoSalida = new SimpleDateFormat("d 'de' MMMM yyyy", new Locale("es", "ES"));

        Date dateInicio = null;
        try {
            dateInicio = formatoEntrada.parse(fechaInicio);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String fechaInicioFormateada = formatoSalida.format(dateInicio);
        DateFormatSymbols symbolsInicio = new DateFormatSymbols(new Locale("es", "ES"));
        String[] mesesInicio = symbolsInicio.getMonths();
        int mesInicio = dateInicio.getMonth();
        String nombreMesInicio = mesesInicio[mesInicio];
        String nombreMesCapitalizadoInicio = nombreMesInicio.substring(0, 1).toUpperCase() + nombreMesInicio.substring(1);
        String fechaInicioFormateadaCapitalizada = fechaInicioFormateada.replace(nombreMesInicio, nombreMesCapitalizadoInicio);

        holder.setInicioTrayectoria(fechaInicioFormateadaCapitalizada);

        if ( trayectoria.getEnd() != null) {


            Date dateFinal = null;
            try {
                dateFinal = formatoEntrada.parse(fechaFinal);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String fechaFinalFormateada = formatoSalida.format(dateFinal);
            DateFormatSymbols symbolsFinal = new DateFormatSymbols(new Locale("es", "ES"));
            String[] mesesFinal = symbolsFinal.getMonths();
            int mesFinal = dateFinal.getMonth();
            String nombreMesFinal = mesesFinal[mesFinal];
            String nombreMesCapitalizadoFinal = nombreMesFinal.substring(0, 1).toUpperCase() + nombreMesFinal.substring(1);
            String fechaFinalFormateadaCapitalizada = fechaFinalFormateada.replace(nombreMesFinal, nombreMesCapitalizadoFinal);

            holder.setFinalTrayectoria(fechaFinalFormateadaCapitalizada);

        }else {
            holder.setFinalTrayectoria("Actualidad");

        }

    }

    public void agregarListaTrayectoria(List<Career> lista) {

        Collections.sort(lista, new Comparator<Career>() {
            @Override
            public int compare(Career o1, Career o2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date fecha1 = dateFormat.parse(o1.getStart());
                    Date fecha2 = dateFormat.parse(o2.getStart());
                    return fecha2.compareTo(fecha1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        datosTrayectoria.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosTrayectoria.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreEquipo;
        private TextView textViewPrincipio;
        private TextView textViewFinal;

        private ImageView imagenEquipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreEquipo = itemView.findViewById(R.id.textViewNombreEquipoTrayectoriaEntrenador);
            imagenEquipo = itemView.findViewById(R.id.imageViewEquipoTrayectoriaEntrenador);
            textViewFinal = itemView.findViewById(R.id.textViewFechaFinalTrayectoriaEntrenador);
            textViewPrincipio = itemView.findViewById(R.id.textViewFechaInicioTrayectoriaEntrenador);


        }

        public void setNombreEquipo(String equipo) {
            nombreEquipo.setText(equipo);
        }

        public void setInicioTrayectoria(String fecha) {
            textViewPrincipio.setText(fecha);
        }

        public void setFinalTrayectoria(String fecha) {
            textViewFinal.setText(fecha);
        }

        public void setImagenLogo(String imagen) {
            Picasso.with(imagenEquipo.getContext()).load(imagen).into(imagenEquipo);
        }


    }
}
