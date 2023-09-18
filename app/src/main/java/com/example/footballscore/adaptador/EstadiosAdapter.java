package com.example.footballscore.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.equipos.Team;
import com.example.footballscore.entidades.equipos.Venue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EstadiosAdapter extends RecyclerView.Adapter<EstadiosAdapter.ViewHolder>{
    private Context contexto;
    private List<Venue> datosEstadios;


    public EstadiosAdapter (Context context){
        this.contexto = contexto;
        this.datosEstadios=new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View equiposLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.estadios_detalles, parent, false);
        return new ViewHolder(equiposLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venue venue = datosEstadios.get(position);

        if (venue.getId() != 0){
            holder.setNombreEstadio(venue.getName());
            holder.setAdress(venue.getAddress());
            holder.setCapacidad(String.valueOf(venue.getCapacity()));
            holder.setEstadio(venue.getImage());
            holder.setCiudad(venue.getCity());
        }else {
            holder.layoutEstadio.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return datosEstadios.size();
    }

    public void agregarEstadio(List<Venue> lista) {
        datosEstadios.addAll(lista);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreEstadio;
        private TextView capacidad;
        private TextView adress;
        private TextView ciudad;
        private RecyclerView recyclerView;
        private ImageView estadio;
        private LinearLayout layoutEstadio;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreEstadio=itemView.findViewById(R.id.textViewNombreEstadio);
            capacidad=itemView.findViewById(R.id.textViewCapacidad);
            adress=itemView.findViewById(R.id.textViewNombreAdress);
            ciudad=itemView.findViewById(R.id.textViewNombreCiudad);
            recyclerView=itemView.findViewById(R.id.recyclerEstadios);
            estadio=itemView.findViewById(R.id.imageViewEstadio);
            layoutEstadio = itemView.findViewById(R.id.layoutEstadioDetalleLiga);


        }

        public void setNombreEstadio(String nombre) {
            nombreEstadio.setText(nombre);
        }

        public void setCapacidad(String capacity) {
            capacidad.setText(capacity);
        }

        public void setAdress(String  direccion) {
            adress.setText(direccion);
        }

        public void setCiudad(String city) {
            ciudad.setText(city);

        }

        public void setEstadio(String imagen) {
            Picasso.with(estadio.getContext()).load(imagen).into(estadio);
        }


    }
}
