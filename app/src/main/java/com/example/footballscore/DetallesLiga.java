package com.example.footballscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.favoritos.AdapterFavoritos;
import com.example.footballscore.favoritos.Favoritos;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import com.example.footballscore.viewpager.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetallesLiga extends AppCompatActivity {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    TextView nombreLiga;
    ImageView image;
    ImageView imagenBandera;
    int idLiga;

    Context context = this;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    CollectionReference collectionUsers = firebaseFirestore.collection("Usuarios");

    public void setNombreLiga(String nombre) {
        nombreLiga.setText(nombre);
    }

    public void setImage(String imagen) {
        Picasso.with(image.getContext()).load(imagen).into(image);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_liga);

        viewPager2=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayoutDetalleLigas);

        ImageView imageViewFavoritos = findViewById(R.id.imageViewFavoritos);


        TextView textViewFavoritos = findViewById(R.id.textViewFavoritos);

        nombreLiga = findViewById(R.id.textViewNombreLeagueDetalleLiga);
        image = findViewById(R.id.imageViewEscudoLigaDetalleLiga);
        imagenBandera = findViewById(R.id.imageViewDetalleEquiposPais);




        Intent intent = getIntent();
        idLiga = intent.getIntExtra("liga", 0);
        Bundle bundle= intent.getExtras();
        int idLeague= bundle.getInt("idLeague");


        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService=retrofit.create(ServicioAPI.class);

        if(idLeague == 0){
            peticionLigaPorID(imageViewFavoritos, textViewFavoritos, apiService, 1, 0);
            viewPager2.setAdapter(new FragmentAdapter(this, idLiga));

        }else {
            peticionLigaPorID(imageViewFavoritos, textViewFavoritos, apiService, 2,idLeague);
            viewPager2.setAdapter(new FragmentAdapter(this, idLeague));

        }

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0: {
                        tab.setText("clasificacion");
                        break;
                    }
                    case 1: {
                        tab.setText("partidos");
                        break;
                    }
                    case 2: {
                        tab.setText("equipos");
                        break;
                    }
                    case 3: {
                        tab.setText("estadios");
                        break;
                    }
                }
            }
        });tabLayoutMediator.attach();
    }

    private void peticionLigaPorID(ImageView imageViewFavoritos, TextView textViewFavoritos, ServicioAPI apiService, int tipoPeticion, int idLeague) {
        Call<LeagueResponse> call = null;
        if(tipoPeticion==1){
            call= apiService.getCompeticionesPorId(idLiga);
        }else if (tipoPeticion == 2){
            call= apiService.getCompeticionesPorId(idLeague);

        }
        call.enqueue(new Callback<LeagueResponse>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> leagues = response.body().getResponse();

                    String name = leagues.get(0).getLeague().getName();
                    setNombreLiga(name);
                    String image = leagues.get(0).getLeague().getLogo();
                    setImage(image);
                    String bandera = leagues.get(0).getCountry().getFlag();
                    int idLeague= leagues.get(0).getLeague().getId();

                    if (bandera != null) {
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
                        }.execute(bandera);
                    }
                    SharedPreferences prefs = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);

                    collectionUsers.whereEqualTo("Nombre", prefs.getString("user", null))
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                        List<HashMap<String, Object>> apuestas = (List<HashMap<String, Object>>) documentSnapshot.get("Favoritos");

                                        if (apuestas != null) {
                                            for (HashMap<String, Object> fav : apuestas) {
                                                Long idLeague2= (Long) fav.get("idLeague");

                                                if (Math.toIntExact(idLeague2) == idLiga || Math.toIntExact(idLeague2) == idLeague){
                                                    imageViewFavoritos.setImageDrawable(getResources().getDrawable(R.drawable.estrella));
                                                }else {
                                                    imageViewFavoritos.setImageDrawable(getResources().getDrawable(R.drawable.estrellafavorito));

                                                }

                                            }


                                        }
                                    }
                                }
                            });


                    imageViewFavoritos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Drawable drawableOriginal = getResources().getDrawable(R.drawable.estrellafavorito);
                            Drawable drawableCambiada = getResources().getDrawable(R.drawable.estrella);
                            Drawable drawableActual = imageViewFavoritos.getDrawable();

                            if (drawableActual.getConstantState().equals(drawableOriginal.getConstantState())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetallesLiga.this);
                                builder.setTitle("Añadir a favoritos");
                                builder.setMessage("¿Quieres añadir esta liga a favoritos?");
                                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(DetallesLiga.this, name + " añadido a favoritos", Toast.LENGTH_SHORT).show();
                                        imageViewFavoritos.setImageDrawable(drawableCambiada);


                                        collectionUsers.whereEqualTo("Nombre",prefs.getString("user",null)).get().addOnCompleteListener(task -> {
                                            if(task.isSuccessful()){
                                                String documentId = task.getResult().getDocuments().get(0).getId();
                                                DocumentReference docRef = collectionUsers.document(documentId);
                                                docRef.update("Favoritos", FieldValue.arrayUnion(new Favoritos(name, image, bandera, idLeague)))
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(context, "Liga añadida correctamente", Toast.LENGTH_LONG).show();

                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(context, "No se ha podido añadir a favoritos", Toast.LENGTH_LONG).show();

                                                        });
                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton("No", null);
                                builder.show();
                            }  if (drawableActual.getConstantState().equals(drawableCambiada.getConstantState())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetallesLiga.this);
                                builder.setTitle("Quitar de favoritos");
                                builder.setMessage("¿Quieres quitar esta liga de favoritos?");
                                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(DetallesLiga.this, name + " quitado de favoritos", Toast.LENGTH_SHORT).show();
                                        imageViewFavoritos.setImageDrawable(drawableOriginal);


                                        // Aquí puedes agregar la lógica para quitar la liga de favoritos
                                        collectionUsers.whereEqualTo("Nombre", prefs.getString("user", null)).get().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                String documentId = task.getResult().getDocuments().get(0).getId();
                                                DocumentReference docRef = collectionUsers.document(documentId);
                                                docRef.update("Favoritos", FieldValue.arrayRemove(new Favoritos(name, image, bandera, idLeague)))
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(context, "Liga quitada correctamente", Toast.LENGTH_LONG).show();

                                                            SharedPreferences.Editor editor = prefs.edit();
                                                            editor.putBoolean("favorito_" + name, false);
                                                            editor.apply();

                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(context, "No se ha podido quitar de favoritos", Toast.LENGTH_LONG).show();
                                                        });
                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton("No", null);
                                builder.show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}