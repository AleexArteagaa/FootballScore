package com.example.footballscore.favoritos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.DetallesLiga;
import com.example.footballscore.R;
import com.example.footballscore.apuestas.ObjetoApuesta;
import com.example.footballscore.apuestas.PopupAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdapterFavoritos extends RecyclerView.Adapter<AdapterFavoritos.ViewHolder> {
    private Context context;
    private List<Favoritos> favoritos;
    public AdapterFavoritos(Context context, List<Favoritos> favoritos) {
        this.context = context;
        this.favoritos= favoritos;

    }

    @NonNull
    @Override
    public AdapterFavoritos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_objeto_favoritos, parent, false);
        return new AdapterFavoritos.ViewHolder(view);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull AdapterFavoritos.ViewHolder holder, int position) {
        Favoritos fav= favoritos.get(position);
        holder.textLiga.setText(fav.getNombreLiga());
        Picasso.with(holder.imageLiga.getContext()).load(fav.getFotoLiga()).into(holder.imageLiga);
        //Picasso.with(holder.imagePais.getContext()).load(fav.getFotoPais()).into(holder.imagePais);
        if (fav.getFotoPais() != null) {
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
                        holder.imagePais.setImageDrawable(drawable);
                    }
                }
            }.execute(fav.getFotoPais());
        }
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("nameLiga", fav.getNombreLiga());
                bundle.putInt("idLeague", fav.idLeague);
                Intent intent= new Intent(context, DetallesLiga.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textLiga;
        ImageView imageLiga;
        ImageView imagePais;
        LinearLayout content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textLiga= itemView.findViewById(R.id.textNombreLigaFav);
            imageLiga= itemView.findViewById(R.id.imageFavLiga);
            imagePais= itemView.findViewById(R.id.imageFavPais);
            content= itemView.findViewById(R.id.contentFav);
        }
    }
}
