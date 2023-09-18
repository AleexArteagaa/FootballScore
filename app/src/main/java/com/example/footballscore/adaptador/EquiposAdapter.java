package com.example.footballscore.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.DetalleEquipos;
import com.example.footballscore.R;
import com.example.footballscore.entidades.Response;
import com.example.footballscore.entidades.clasificaciones.Standings;
import com.example.footballscore.entidades.equipos.Team;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EquiposAdapter extends RecyclerView.Adapter<EquiposAdapter.ViewHolder>{

    private Context contexto;
    private List<Response> datosEquipos;


    public EquiposAdapter (Context context){
        this.contexto = contexto;
        this.datosEquipos=new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View equiposLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_equipos_detalles, parent, false);
        return new ViewHolder(equiposLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull EquiposAdapter.ViewHolder holder, int position) {
        Team team = datosEquipos.get(position).getTeam();
        holder.setNombreEquipo(team.getName());
        holder.setEscudo(team.getLogo());
        int id = team.getId();

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetalleEquipos.class);
                intent.putExtra("equipo", id);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datosEquipos.size();
    }

    public void agregarListaClasificacion(List<Response> lista) {
        datosEquipos.addAll(lista);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreEquipo;
        private RecyclerView recyclerView;
        private ImageView escudo;
        private LinearLayout layout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreEquipo=itemView.findViewById(R.id.textViewNombreEquipo);
            recyclerView=itemView.findViewById(R.id.recyclerEquipos);
            escudo=itemView.findViewById(R.id.imageViewEscudo);
            layout = itemView.findViewById(R.id.layoutEquipoDetalleLigas);


        }
        public void setNombreEquipo(String nombre) {
            nombreEquipo.setText(nombre);
        }

        public void setEscudo(String imagen) {
            Picasso.with(escudo.getContext()).load(imagen).into(escudo);        }
    }
}
