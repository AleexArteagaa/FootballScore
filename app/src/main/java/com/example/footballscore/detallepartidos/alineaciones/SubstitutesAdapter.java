package com.example.footballscore.detallepartidos.alineaciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.partidos.alineaciones.Substitutes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubstitutesAdapter extends RecyclerView.Adapter<SubstitutesAdapter.SubstitutesViewHolder> {

    private List<Substitutes> substitutesList;
    private Context context;

    public SubstitutesAdapter(Context context, List<Substitutes> substitutesList) {
        this.context = context;
        this.substitutesList = substitutesList;
    }

    @NonNull
    @Override
    public SubstitutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_substitutes, parent, false);
        return new SubstitutesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubstitutesViewHolder holder, int position) {
        Substitutes substitutes = substitutesList.get(position);
        Picasso.with(holder.fotoSuplente.getContext()).load(substitutes.getPlayer().getPhoto()).into(holder.fotoSuplente);
        holder.nombreSuplente.setText(substitutes.getPlayer().getName());
        holder.dorsalSuplente.setText(substitutes.getPlayer().getNumber()+"");
        holder.posicionSuplente.setText(substitutes.getPlayer().getPos());

        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return substitutesList.size();
    }

    public static class SubstitutesViewHolder extends RecyclerView.ViewHolder {
        ImageView fotoSuplente;
        TextView nombreSuplente;
        TextView dorsalSuplente;
        TextView posicionSuplente;

        public SubstitutesViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoSuplente= itemView.findViewById(R.id.imagenSuplente);
            nombreSuplente= itemView.findViewById(R.id.nombreSuplente);
            dorsalSuplente= itemView.findViewById(R.id.dorsalSuplente);
            posicionSuplente= itemView.findViewById(R.id.posicionSuplente);
        }
    }
}
