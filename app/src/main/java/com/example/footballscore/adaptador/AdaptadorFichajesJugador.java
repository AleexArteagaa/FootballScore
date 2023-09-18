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
import com.example.footballscore.entidades.Response;
import com.example.footballscore.entidades.TrayectoriaDTO;
import com.example.footballscore.entidades.fichajes.Transfers;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdaptadorFichajesJugador extends RecyclerView.Adapter<AdaptadorFichajesJugador.ViewHolder>{

    private Context contexto;
    private ArrayList<Transfers> datosFichajes;
    private boolean showShimmer = true;


    public AdaptadorFichajesJugador(Context contexto) {
        this.contexto = contexto;
        datosFichajes = new ArrayList<>();

    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorFichajesJugador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_fichaje_jugador, parent, false);
        return new AdaptadorFichajesJugador.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorFichajesJugador.ViewHolder holder, int position) {


        Transfers fichaje = datosFichajes.get(position);

        holder.setImagenEquipoAlQueVa(fichaje.getTeams().getIn().getLogo());
        holder.setNombreEquipoAlQueViene(fichaje.getTeams().getIn().getName());
        holder.setImagenEquipoDelQueViene(fichaje.getTeams().getOut().getLogo());
        holder.setNombreEquipoDelQueViene(fichaje.getTeams().getOut().getName());

        if (fichaje.getType() == null){

        } else if (fichaje.getType().equalsIgnoreCase("Loan")) {
            holder.setInfoFichaje("Cesión");
        } else if (fichaje.getType().equalsIgnoreCase("N/A")) {
            holder.setInfoFichaje("Libre");
        } else if (fichaje.getType().contains("Free")) {
            holder.setInfoFichaje("Gratis");
        } else if (fichaje.getType().contains("€")){
            String tipo = fichaje.getType();
            String[] partes = tipo.split(" ");
            if (partes.length == 2) {
                String cantidad = partes[1];
                String moneda = partes[0];
                String nuevoTipo = cantidad + " " + moneda;
                holder.setInfoFichaje(nuevoTipo);
            } else {
                holder.setInfoFichaje(tipo);
            }
        }

        String fecha = fichaje.getDate();
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatoSalida = new SimpleDateFormat("d 'de' MMMM yyyy", new Locale("es", "ES"));

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = dateFormat.parse(fecha);

            // Definir la temporada según el criterio
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // El mes es zero-based, se le suma 1
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            int startYear;
            int endYear;
            if (month < 6 || (month == 6 && day <= 1)) {
                // Si el mes es anterior a julio o es julio pero el día es 1 o anterior, la temporada es del año anterior
                startYear = year - 1;
                endYear = year;
            } else {

                startYear = year;
                endYear = year + 1;
            }

            String temporada = startYear + "/" + endYear;
            holder.setTemporadaFichaje(temporada);
        }catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void agregarListaFichajes(List<Transfers> lista) {

        datosFichajes.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosFichajes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewFecha;
        private TextView textViewTemporada;

        private TextView textViewInformacionFichaje;
        private TextView textViewEquipoDelQueViene;
        private TextView textViewEquipoAlQueVa;
        private ImageView imagenEquipoDelQueViene;
        private ImageView imagenEquipoAlQueVa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFecha = itemView.findViewById(R.id.textViewFechaFichajeDetalleJugador);
            textViewInformacionFichaje = itemView.findViewById(R.id.textViewInformacionFichajeDetalleJugador);
            textViewEquipoDelQueViene = itemView.findViewById(R.id.textViewEquipoDelQueVieneFichajeDetalleJugador);
            textViewEquipoAlQueVa = itemView.findViewById(R.id.textViewEquipoAlQueVieneFichajeDetalleJugador);
            imagenEquipoDelQueViene = itemView.findViewById(R.id.imageViewEquipoDelQueVieneFichajeDetalleJugador);
            imagenEquipoAlQueVa = itemView.findViewById(R.id.imageViewEquipoAlQueVieneFichajeDetalleJugador);
            textViewTemporada = itemView.findViewById(R.id.textViewTemporadaFichajeJugador);
        }

        public void setFecha(String fecha) {
            textViewFecha.setText(fecha);
        }
        public void setInfoFichaje(String info) {
            textViewInformacionFichaje.setText(info);
        }

        public void setTemporadaFichaje(String temp) {
            textViewTemporada.setText(temp);
        }


        public void setNombreEquipoDelQueViene(String equipo) {
            textViewEquipoDelQueViene.setText(equipo);
        }
        public void setNombreEquipoAlQueViene(String equipo) {
            textViewEquipoAlQueVa.setText(equipo);
        }

        public void setImagenEquipoDelQueViene(String imagen) {
            Picasso.with(imagenEquipoDelQueViene.getContext()).load(imagen).into(imagenEquipoDelQueViene);
        }
        public void setImagenEquipoAlQueVa(String imagen) {
            Picasso.with(imagenEquipoAlQueVa.getContext()).load(imagen).into(imagenEquipoAlQueVa);
        }


    }
}
