package com.example.footballscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footballscore.apuestas.historial.HistorialDTO;
import com.example.footballscore.favoritos.Favoritos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    TextView correo;
    TextView contraseña;
    TextView usuario;
    Button registrar;

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference colectionUsers = firebaseFirestore.collection("Usuarios");

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        correo=findViewById(R.id.editTextCorreo);
        contraseña=findViewById(R.id.editTextPassword2);
        registrar=findViewById(R.id.button);
        usuario= findViewById(R.id.editUsuarioNuevo);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contraseña.getText().toString().trim().isEmpty() &&
                        !usuario.getText().toString().trim().isEmpty() &&
                        !correo.getText().toString().trim().isEmpty()) {
                    if (checkMail(correo.getText().toString())) {

                        if (contraseña.getText().toString().length() >= 6) {

                            colectionUsers.whereEqualTo("nombre", usuario.getText().toString())
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (!task.getResult().isEmpty()) {
                                                usuario.setError("El nombre de usuario ya existe");
                                            } else {
                                                colectionUsers.whereEqualTo("correo", correo.getText().toString())
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (!task.getResult().isEmpty()) {
                                                                    correo.setError("El correo electrónico ya existe");
                                                                } else {
                                                                    creacionNuevoUsuario();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        } else {
                            contraseña.setError("Contraseña con minimo de 6 caracteres");
                        }
                    } else {
                        correo.setError("Mail invalido");

                    }
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Rellene todos los datos", Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#047709")));
                    snackbar.show();
                }

            }
        });
    }
    private void creacionNuevoUsuario() {
        registryUser(correo.getText().toString(), contraseña.getText().toString());
        List<HistorialDTO> list= new ArrayList<>();
        List<Favoritos> list2= new ArrayList<>();
        Map<String, Object> usuarioNuevo = new HashMap<>();
        usuarioNuevo.put("Nombre", usuario.getText().toString());
        usuarioNuevo.put("Correo", correo.getText().toString());
        usuarioNuevo.put("Password", contraseña.getText().toString());
        usuarioNuevo.put("Saldo", 1000);
        usuarioNuevo.put("Apuestas", list);
        usuarioNuevo.put("Favoritos", list2);

        colectionUsers.add(usuarioNuevo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RegistroActivity.this, "User registrado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registryUser(String mail, String passw) {

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(mail, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

    }

    private boolean checkMail(String email) {
        String patron = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}