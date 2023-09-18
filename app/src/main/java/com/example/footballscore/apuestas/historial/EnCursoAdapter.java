package com.example.footballscore.apuestas.historial;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.apuestas.PopupAdapter;

import java.util.List;

public class EnCursoAdapter extends RecyclerView.Adapter<EnCursoAdapter.ViewHolder>{
    private List<HistorialDTO> historialDTOList;
    @NonNull
    @Override
    public EnCursoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_objeto_historial, parent, false);
        return new EnCursoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnCursoAdapter.ViewHolder holder, int position) {
        HistorialDTO item = historialDTOList.get(position);

        holder.cantidad.setText(String.valueOf(item.getCantidad()));
        if (item.getEstado().equals("Not Started")){
            holder.estado.setText(item.getEstado());
            holder.estado.setTextColor(Color.GREEN);
        } else if (item.getEstado().equals("Started")) {
            holder.estado.setText(item.getEstado());
            holder.estado.setTextColor(Color.YELLOW);
        } else if (item.getEstado().equals("Match Finished")) {
            holder.estado.setText(item.getEstado());
            holder.estado.setTextColor(Color.RED);
        }
        holder.equipos.setText(item.getNombre());

    }

    @Override
    public int getItemCount() {
        return historialDTOList.size();
    }
    public void setItemList(List<HistorialDTO> itemList) {
        this.historialDTOList = itemList;
        notifyDataSetChanged();
    }


    public class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView cantidad;
        private TextView estado;
        private TextView equipos;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cantidad= itemView.findViewById(R.id.cantidadHistorial);
            estado= itemView.findViewById(R.id.estadoApuesta);
            equipos= itemView.findViewById(R.id.nombreApuesta);
        }
    }

}
