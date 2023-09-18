package com.example.footballscore.adaptador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.detallepartidos.TabbedActivityDetallePartido;
import com.example.footballscore.entidades.PartidoDTO;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class AdaptadorListaPartidosPorMes extends RecyclerView.Adapter<AdaptadorListaPartidosPorMes.ViewHolder>{

    private ArrayList<PartidoDTO> datosPartidos;
    private Context context;


    public AdaptadorListaPartidosPorMes(Context context, ArrayList<PartidoDTO> datosPartidos){
        this.context = context;
        this.datosPartidos = ordenarPartidosPorFecha(datosPartidos);
    }

    private ArrayList<PartidoDTO> ordenarPartidosPorFecha(ArrayList<PartidoDTO> partidos) {
        Collections.sort(partidos, new Comparator<PartidoDTO>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(PartidoDTO partido1, PartidoDTO partido2) {

                String fecha1 = partido1.getDate();
                String fecha2 = partido2.getDate();

                LocalDateTime dateTime1 = LocalDateTime.parse(fecha1, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
                LocalDateTime dateTime2 = LocalDateTime.parse(fecha2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));

                int result = dateTime1.toLocalDate().compareTo(dateTime2.toLocalDate());
                if (result == 0) {
                    result = dateTime1.toLocalTime().compareTo(dateTime2.toLocalTime());
                }

                return result;
            }
        });

        return partidos;
    }

    @NonNull
    @Override
    public AdaptadorListaPartidosPorMes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_partido_detalle_equipo, parent, false);
        return new AdaptadorListaPartidosPorMes.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaPartidosPorMes.ViewHolder holder, int position) {

        PartidoDTO partido = datosPartidos.get(position);

        holder.setEquipoLocal(partido.getHomeTeam());
        holder.setEquipoVisitante(partido.getAwayTeam());
        int golesLocales = datosPartidos.get(position).getHomeScore();
        int golesVisitantes = datosPartidos.get(position).getAwayScore();

        if (!datosPartidos.get(position).getEstado().equalsIgnoreCase("Match Finished")){

            if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Not Started")){
                String fechaHora = datosPartidos.get(position).getDate();
                String hora = fechaHora.substring(11, 16);
                holder.setResultado(hora);
                holder.setEstadoPartido("");

                listenerDetalle(holder, partido, true);


            } else if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Match Abandoned")) {
                holder.setEstadoPartido("APL");
                holder.textViewEstadoPartidoDetallesEquipo.setBackgroundColor(Color.RED);
                holder.textViewEstadoPartidoDetallesEquipo.setTextColor(Color.WHITE);
                String fechaHora = datosPartidos.get(position).getDate();
                String hora = fechaHora.substring(11, 16);
                holder.setResultado(hora);

                listenerDetalle(holder, partido, true);


            } else if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Halftime")) {
                holder.setEstadoPartido("DES");
                holder.setResultado(golesLocales + " - " + golesVisitantes);
                holder.textViewEstadoPartidoDetallesEquipo.setBackgroundColor(Color.parseColor("#047709"));
                holder.textViewEstadoPartidoDetallesEquipo.setTextColor(Color.WHITE);

                listenerDetalle(holder, partido, false);


            }else if(datosPartidos.get(position).getEstado().equalsIgnoreCase("Time to be defined")){

                String fechaHora = datosPartidos.get(position).getDate();
                String hora = fechaHora.substring(11, 16);
                System.out.println(datosPartidos.get(position).getDate());
                holder.setResultado(hora);
                holder.setEstadoPartido("");

                listenerDetalle(holder, partido, false);

            } else {
                holder.setEstadoPartido(datosPartidos.get(position).getElapsed() + "'");
                holder.setResultado(golesLocales + " - " + golesVisitantes);
                holder.textViewEstadoPartidoDetallesEquipo.setBackgroundColor(Color.parseColor("#047709"));
                holder.textViewEstadoPartidoDetallesEquipo.setTextColor(Color.WHITE);

                listenerDetalle(holder, partido, false);

            }

        }else {
            holder.setEstadoPartido("FIN");
            holder.setResultado(golesLocales + " - " + golesVisitantes);
            holder.textViewEstadoPartidoDetallesEquipo.setBackgroundColor(Color.GRAY);
            holder.textViewEstadoPartidoDetallesEquipo.setTextColor(Color.WHITE);

            listenerDetalle(holder, partido, false);

        }

        if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Not Started")){

            holder.setFechaPartidoNoEmpezado(datosPartidos.get(position).getDate());

        }else {
            holder.setFechaPartido(datosPartidos.get(position).getDate());

        }

        holder.setCompeticion(datosPartidos.get(position).getLiga());
        holder.setImagenEquipoLocal(datosPartidos.get(position).getHomeLogo());
        holder.setImagenEquipoVisitante(datosPartidos.get(position).getAwayLogo());

    }

    private void listenerDetalle(@NonNull AdaptadorListaPartidosPorMes.ViewHolder holder, PartidoDTO partido, boolean isNotStarted) {
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, TabbedActivityDetallePartido.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("partido", partido);
                bundle.putBoolean("isNotStarted", isNotStarted);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
        holder.textViewEquipoLocalPartidoDetallesEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, TabbedActivityDetallePartido.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("partido", partido);
                bundle.putBoolean("isNotStarted", isNotStarted);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
        holder.textViewEquipoVisitantePartidoDetallesEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, TabbedActivityDetallePartido.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("partido", partido);
                bundle.putBoolean("isNotStarted", isNotStarted);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return datosPartidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewEstadoPartidoDetallesEquipo;
        public TextView textViewCompeticionDetallesEquipo;
        public TextView textViewEquipoLocalPartidoDetallesEquipo;
        public TextView textViewResultadoPartidoDetallesEquipo;
        public TextView textViewEquipoVisitantePartidoDetallesEquipo;
        public TextView textViewFechaPartidoDetallesEquipo;
        public ImageView imageViewEquipoLocalPartidoDetallesEquipo;
        public ImageView imageViewEquipoVisitantePartidoDetallesEquipo;

        public LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewEstadoPartidoDetallesEquipo = itemView.findViewById(R.id.textViewEstadoPartidoDetallesEquipo);
            textViewCompeticionDetallesEquipo = itemView.findViewById(R.id.textViewCompeticionDetallesEquipo);
            textViewEquipoLocalPartidoDetallesEquipo = itemView.findViewById(R.id.textViewEquipoLocalPartidoDetallesEquipo);
            textViewResultadoPartidoDetallesEquipo = itemView.findViewById(R.id.textViewResultadoPartidoDetallesEquipo);
            textViewEquipoVisitantePartidoDetallesEquipo = itemView.findViewById(R.id.textViewEquipoVisitantePartidoDetallesEquipo);
            textViewFechaPartidoDetallesEquipo = itemView.findViewById(R.id.textViewFechaPartidoDetallesEquipo);
            imageViewEquipoLocalPartidoDetallesEquipo = itemView.findViewById(R.id.imageViewEquipoLocalPartidoDetallesEquipo);
            imageViewEquipoVisitantePartidoDetallesEquipo = itemView.findViewById(R.id.imageViewEquipoVisitantePartidoDetallesEquipo);
            layout = itemView.findViewById(R.id.layoutPartidoDetalleEquipo);

        }

        public void setEquipoLocal(String nombreEquipo) {
            textViewEquipoLocalPartidoDetallesEquipo.setText(nombreEquipo);
        }

        public void setEquipoVisitante(String nombreEquipo) {
            textViewEquipoVisitantePartidoDetallesEquipo.setText(nombreEquipo);
        }
        public void setFechaPartidoNoEmpezado(String date) {
            String fechaFormateada = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                fechaFormateada = obtenerFechaFormateadaSinHora(date);
            }
            textViewFechaPartidoDetallesEquipo.setText(fechaFormateada);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private String obtenerFechaFormateadaSinHora(String fecha) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM", new Locale("es", "ES"));

            LocalDateTime dateTime = LocalDateTime.parse(fecha, inputFormatter);
            String fechaFormateada = dateTime.format(outputFormatter);

            // Obtener el nombre del mes con la primera letra en mayúscula
            DateFormatSymbols symbols = new DateFormatSymbols(new Locale("es", "ES"));
            String[] meses = symbols.getMonths();
            String nombreMes = meses[dateTime.getMonthValue() - 1];
            String primeraLetraMayuscula = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1);

            // Combinar la primera letra mayúscula del mes con el resto de la fecha formateada
            fechaFormateada = fechaFormateada.replace(nombreMes, primeraLetraMayuscula);

            return fechaFormateada;
        }
        public void setFechaPartido(String fecha) {
            String fechaFormateada = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                fechaFormateada = obtenerFechaFormateada(fecha);
            }
            textViewFechaPartidoDetallesEquipo.setText(fechaFormateada);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private String obtenerFechaFormateada(String fecha) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM HH:mm", new Locale("es", "ES"));

            LocalDateTime dateTime = LocalDateTime.parse(fecha, inputFormatter);
            String fechaFormateada = dateTime.format(outputFormatter);

            // Obtener el nombre del mes con la primera letra en mayúscula
            DateFormatSymbols symbols = new DateFormatSymbols(new Locale("es", "ES"));
            String[] meses = symbols.getMonths();
            String nombreMes = meses[dateTime.getMonthValue() - 1];
            String primeraLetraMayuscula = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1);

            // Combinar la primera letra mayúscula del mes con el resto de la fecha formateada
            fechaFormateada = fechaFormateada.replace(nombreMes, primeraLetraMayuscula);

            return fechaFormateada;
        }
        private String obtenerNombreMes(int mes) {
            String[] nombresMeses = {
                    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre",
                    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"
            };

            if (mes >= 0 && mes < nombresMeses.length) {
                return nombresMeses[mes];
            } else {
                return "";
            }
        }
        public void setResultado(String resultadoPartido) {
            textViewResultadoPartidoDetallesEquipo.setText(resultadoPartido);
        }

        public void setCompeticion(String competicion) {
            textViewCompeticionDetallesEquipo.setText(competicion);
        }
        public void setImagenEquipoLocal(String imagen) {
            Picasso.with(imageViewEquipoLocalPartidoDetallesEquipo.getContext()).load(imagen).into(imageViewEquipoLocalPartidoDetallesEquipo);
        }
        public void setImagenEquipoVisitante(String imagen) {
            Picasso.with(imageViewEquipoVisitantePartidoDetallesEquipo.getContext()).load(imagen).into(imageViewEquipoVisitantePartidoDetallesEquipo);
        }
        public void setEstadoPartido(String estado){
            textViewEstadoPartidoDetallesEquipo.setText(estado);
        }



    }
}
