package com.example.footballscore.adaptador;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.Response;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdaptadorLesionesJugador extends RecyclerView.Adapter<AdaptadorLesionesJugador.ViewHolder>{

    private Context contexto;
    private ArrayList<Response> datosLesiones;
    private boolean showShimmer = true;


    public AdaptadorLesionesJugador(Context contexto) {
        this.contexto = contexto;
        datosLesiones = new ArrayList<>();

    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorLesionesJugador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_lesion, parent, false);
        return new AdaptadorLesionesJugador.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Response response = datosLesiones.get(position);


        holder.setTipoLesion(response.getPlayer().getReason());

        String fecha = response.getFixture().getDate();

        SimpleDateFormat inputFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

        try {
            Date date = inputFormat.parse(fecha);
            String fechaFormateada = outputFormat.format(date);

            DateFormatSymbols symbols = new DateFormatSymbols(new Locale("es", "ES"));
            String[] meses = symbols.getMonths();
            String mesFormateado = Character.toUpperCase(meses[date.getMonth()].charAt(0)) + meses[date.getMonth()].substring(1);

            fechaFormateada = fechaFormateada.replace(meses[date.getMonth()], mesFormateado);
            holder.setFechaLesion(fechaFormateada);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (response.getPlayer().getReason().equalsIgnoreCase("Yellow Cards")){
            holder.imagenTipoLesion.setImageResource(R.drawable.tarjeta_amarilla);
            holder.setTipoLesion("Acumulación de Tarjetas");
        } else if (response.getPlayer().getReason().equalsIgnoreCase("Red Card")) {
            holder.imagenTipoLesion.setImageResource(R.drawable.tarjeta_roja);
            holder.setTipoLesion("Tarjeta Roja");
        }else if (response.getPlayer().getReason().contains("Injury")){
            holder.imagenTipoLesion.setImageResource(R.drawable.lesion);
            if (response.getPlayer().getReason().contains("Muscle Injury")){
                holder.setTipoLesion("Lesión Muscular");
            } else if (response.getPlayer().getReason().contains("Ankle Injury")) {
                holder.setTipoLesion("Lesión en el Tobillo");
            }else if (response.getPlayer().getReason().contains("Leg Injury")) {
                holder.setTipoLesion("Lesión en la Pierna");
            }else if (response.getPlayer().getReason().contains("Knee Injury")) {
                holder.setTipoLesion("Lesión de Rodilla");
            } else if (response.getPlayer().getReason().contains("Calf Injury")) {
                holder.setTipoLesion("Lesión en la Pantorrilla");
            }else if (response.getPlayer().getReason().contains("Thight Injury")) {
                holder.setTipoLesion("Lesión en el Muslo");
            }

        }else if (response.getPlayer().getReason().equalsIgnoreCase("Suspended")){
            holder.setTipoLesion("Sancionado");
            holder.imagenTipoLesion.setImageResource(R.drawable.tarjeta_roja);
        }else if (response.getPlayer().getReason().contains("Stomach Disorder")) {
            holder.setTipoLesion("Gastroenteritis");
        }else if (response.getPlayer().getReason().contains("Lacking Match Fitness")) {
            holder.setTipoLesion("Baja Forma");
        }


    }

    public void agregarListaLesiones(List<Response> lista) {
        datosLesiones.clear();
        datosLesiones.addAll(lista);

        Collections.sort(datosLesiones, new Comparator<Response>() {
            @Override
            public int compare(Response les1, Response les2) {
                String fecha1 = les1.getFixture().getDate();
                String fecha2 = les2.getFixture().getDate();

                SimpleDateFormat inputFormat = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                }
                try {
                    Date date1 = inputFormat.parse(fecha1);
                    Date date2 = inputFormat.parse(fecha2);

                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosLesiones.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textviewTipoLesion;
        private TextView textViewFechaLesion;


        private ImageView imagenTipoLesion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textviewTipoLesion = itemView.findViewById(R.id.textViewTipoLesion);
            imagenTipoLesion = itemView.findViewById(R.id.imageViewTipoLesion);
            textViewFechaLesion = itemView.findViewById(R.id.textViewFechaLesion);

        }

        public void setTipoLesion(String lesion) {
            textviewTipoLesion.setText(lesion);
        }

        public void setFechaLesion(String fecha) {
            textViewFechaLesion.setText(fecha);
        }


        public void setImagenLesion(String imagen) {
            Picasso.with(imagenTipoLesion.getContext()).load(imagen).into(imagenTipoLesion);
        }


    }
}
