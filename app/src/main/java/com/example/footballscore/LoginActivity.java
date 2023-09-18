package com.example.footballscore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    TextView usuario;
    TextView password;
    Button btniniciarSession;
    TextView pregunta;
    Button btnRegistrar;

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionUsers = firebaseFirestore.collection("Usuarios");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        usuario=findViewById(R.id.editUsuarioInicio);
        password=findViewById(R.id.editPasswInicio);
        btniniciarSession=findViewById(R.id.buttonIniciarSesion);
        pregunta=findViewById(R.id.textViewPregunta);
        btnRegistrar=findViewById(R.id.btnRegistro);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    Intent intent = new Intent(LoginActivity.this , RegistroActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No hay conexión a Internet", Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#047709")));
                    snackbar.show();
                }

            }
        });
        btniniciarSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    if (!password.getText().toString().trim().isEmpty() && !usuario.getText().toString().trim().isEmpty()) {
                        collectionUsers.whereEqualTo("Nombre", usuario.getText().toString())
                                .get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        comprobacionUserPass(v, task);
                                    } else {
                                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error en la consulta", Snackbar.LENGTH_SHORT);
                                        View snackbarView = snackbar.getView();
                                        snackbarView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#047709")));
                                        snackbar.show();
                                    }
                                });
                    } else {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Los campos no pueden estar vacíos", Snackbar.LENGTH_SHORT);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#047709")));
                        snackbar.show();

                    }
                }else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No hay conexión a Internet", Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#047709")));
                    snackbar.show();
                }
            }
        });



    }
    private void comprobacionUserPass(View v, Task<QuerySnapshot> task) {
        if (task.getResult().size() > 0) {
            String passw = task.getResult().getDocuments().get(0).get("Password").toString();
            if (passw.equals(password.getText().toString())) {
                String email = task.getResult().getDocuments().get(0).get("Correo").toString();


                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user", usuario.getText().toString());
                editor.putInt("saldo",  Integer.parseInt(task.getResult().getDocuments().get(0).get("Saldo")+""));
                editor.apply();


                Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
                intent.putExtra("usuario", usuario.getText().toString());
                intent.putExtra("email", email);

                v.getContext().startActivity(intent);
                finish();
            } else {
                password.setError("Contraseña incorrecta");
            }
        } else {
            usuario.setError("User incorrecto");
        }
    }
}