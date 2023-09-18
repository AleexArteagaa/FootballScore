package com.example.footballscore;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.footballscore.apuestas.PopupActivity;
import com.example.footballscore.apuestas.historial.PopupHistorial;
import com.example.footballscore.fragments.ExplorarFragment;
import com.example.footballscore.fragments.FavoritosFragment;
import com.example.footballscore.fragments.NosotrosFragment;
import com.example.footballscore.fragments.PartidosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;


public class MenuPrincipal extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    PartidosFragment partidosFragment = new PartidosFragment();
    ExplorarFragment explorarFragment = new ExplorarFragment();
    FavoritosFragment favoritosFragment = new FavoritosFragment();
    NosotrosFragment nosotrosFragment=new NosotrosFragment();
    Context contexto;
    private Intent respuesta;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    CollectionReference collectionUsers = firebaseFirestore.collection("Usuarios");

    private boolean dataLoginExecuted = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.navegation_menu);
        contexto = this;

        bottomNavigationView  = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.navigation_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, partidosFragment).commit();

        ColorStateList iconColorStateList = getResources().getColorStateList(R.color.bottom_nav_colors);
        bottomNavigationView.setItemIconTintList(iconColorStateList);
        ColorStateList textColors = getResources().getColorStateList(R.color.bottom_nav_text_colors);
        bottomNavigationView.setItemTextColor(textColors);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Fragment emptyFragment = new Fragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, emptyFragment)
                                .addToBackStack(null)
                                .commit();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new PartidosFragment())
                                .commit();
                        return true;
                    case R.id.explorar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,explorarFragment).commit();
                        return true;
                    case R.id.favoritos:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,favoritosFragment).commit();
                        break;
                }

                return false;
            }
        });




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent homeIntent = new Intent(contexto, MenuPrincipal.class);
                        startActivity(homeIntent);
                        break;
                    /*case R.id.nav_fav:
                        Intent galleryIntent = new Intent(MenuPrincipal.this, FavoritosActivity.class);
                        startActivity(galleryIntent);
                        break;*/
                    case R.id.nav_tienda:
                        PopupActivity popupActivity= new PopupActivity(MenuPrincipal.this);
                        popupActivity.show();

                        break;
                    case R.id.nav_apuesta:
                        PopupHistorial popupEnCurso= new PopupHistorial(MenuPrincipal.this);
                        popupEnCurso.show();

                        break;
                    case R.id.nav_sobre_nosotros:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,nosotrosFragment).commit();
                        //PopupHistorial popupEnCurso= new PopupHistorial(MenuPrincipal.this);
                        //popupEnCurso.show();
                        break;
                    case R.id.nav_cerrar_sesion:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
                        builder.setTitle("Cerrar sesión");
                        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?");
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent iniciarSesionIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                iniciarSesionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(iniciarSesionIntent);
                                finish();
                                return;

                            }
                        });
                        builder.setNegativeButton("No", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        getDataLogin();

    }

    private void getDataLogin() {
        respuesta = getIntent();
        String usuarioLateral = respuesta.getStringExtra("usuario");
        String mailLateral= respuesta.getStringExtra("email");

        View headerView = navigationView.getHeaderView(0);
        TextView userNameLateral = headerView.findViewById(R.id.userNameLateral);
        TextView userMailLateral = headerView.findViewById(R.id.userMailLateral);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedUser = prefs.getString("user", null);

        if (savedUser != null && savedUser.equals(usuarioLateral)) {
            userNameLateral.setText(usuarioLateral.toUpperCase(Locale.ROOT));
            userMailLateral.setText(mailLateral);
        } else {
            userNameLateral.setText(savedUser.toUpperCase(Locale.ROOT));

            Query query = collectionUsers.whereEqualTo("Nombre", savedUser);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        String userEmail = document.getString("Correo");
                        userMailLateral.setText(userEmail);

                        // Guardar los datos en SharedPreferences
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("user", savedUser);
                        editor.putString("email", userEmail);
                        editor.apply();

                        Log.d(TAG, "Correo electrónico del usuario: " + userEmail);
                    } else {
                        Log.d(TAG, "Error al obtener el documento del usuario", task.getException());
                    }
                }
            });
        }

        ImageView userImageLateral= headerView.findViewById(R.id.imageViewUser);
        userImageLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
                builder.setTitle("Seleccionar imagen");

                String[] options = {"Seleccionar de la galería", "Tomar una foto"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Acción para seleccionar imagen de la galería
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                pickPhoto.setType("image/");
                                startActivityForResult(pickPhoto, 1);
                                break;
                            case 1:
                                // Acción para tomar una foto en el momento
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                                break;
                        }
                    }
                });

                builder.show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        View headerView = navigationView.getHeaderView(0);
        ImageView foto= headerView.findViewById(R.id.imageViewUser);

        if (resultCode == RESULT_OK){
            Uri path= data.getData();
            foto.setImageURI(path);
            foto.setScaleType(ImageView.ScaleType.CENTER_CROP);

            ViewGroup.LayoutParams layoutParams = foto.getLayoutParams();
            layoutParams.width = 200;
            layoutParams.height = 200;
            foto.setLayoutParams(layoutParams);
/*
            // Obtener una referencia al documento del usuario correspondiente
            String usuario = getIntent().getStringExtra("usuario");
            DocumentReference docRef = collectionUsers.document(usuario);

            // Crear un campo llamado "imagenPerfil" en el documento del usuario y asignarle la URL de la imagen que se acaba de seleccionar
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference fotoRef = storageRef.child("imagenesPerfil/" + usuario);
            fotoRef.putFile(path)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri downloadUrl = uriTask.getResult();
                        Map<String, Object> imagen = new HashMap<>();
                        imagen.put("imagenPerfil", downloadUrl.toString());

                        // Actualizar el documento del usuario en la base de datos de Firebase Firestore con el nuevo campo "imagenPerfil"
                        docRef.update(imagen)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Imagen de perfil actualizada con éxito");
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error al actualizar la imagen de perfil", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error al subir la imagen de perfil", e);
                    });*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!dataLoginExecuted) {
            getDataLogin();
            dataLoginExecuted = true;
        }


    }
}
