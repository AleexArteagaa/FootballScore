package com.example.footballscore.adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.R;
import com.example.footballscore.Traductor;
import com.example.footballscore.entidades.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AdaptadorListaPaises extends RecyclerView.Adapter<AdaptadorListaPaises.ViewHolder>{

    private Context contexto;
    private ArrayList<Response> datosPaises;
    private boolean showShimmer = true;


    public AdaptadorListaPaises(Context contexto) {
        this.contexto = contexto;
        datosPaises = new ArrayList<>();

    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorListaPaises.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_paises, parent, false);
        return new AdaptadorListaPaises.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaPaises.ViewHolder holder, int position) {

        Traductor traductor = new Traductor();
        String paisTraducido = traductor.traducir(datosPaises.get(position).getName());


        holder.setNombrePais(paisTraducido);
        holder.setImagenBandera(datosPaises.get(position).getFlag());

    }

    public void agregarListaPaises(List<Response> lista) {
        datosPaises.addAll(lista);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosPaises.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombrePais;

        private ImageView imagenBandera;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombrePais = itemView.findViewById(R.id.textViewNombreEquipo);
            imagenBandera = itemView.findViewById(R.id.imageViewEscudo);
        }

        public void setNombrePais(String pais) {
            nombrePais.setText(pais);
        }

        @SuppressLint("StaticFieldLeak")
        public void setImagenBandera(String imagen){

            if (imagen != null) {
                new AsyncTask<String, Void, Drawable>() {
                    @Override
                    protected Drawable doInBackground(String... params) {
                        try {
                            InputStream inputStream = new URL(params[0]).openStream();
                            SVG svg = SVG.getFromInputStream(inputStream);
                            return new PictureDrawable(svg.renderToPicture());
                        } catch (IOException | SVGParseException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Drawable drawable) {
                        if (drawable != null) {
                            imagenBandera.setImageDrawable(drawable);
                        }
                    }
                }.execute(imagen);
            }
        }
    }
}
