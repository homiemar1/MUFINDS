package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RecuperarUsuarioActivity extends AppCompatActivity {
    EditText etEmailRecuperarUsuario, etContraseñaRecuperarUsuario;
    TextView tvRespuestaRecuperarUsuario;
    SharedPreferences sharedPref;
    FirebaseFirestore database;

    String contraseña, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_usuario);
        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        database = FirebaseFirestore.getInstance();

        etEmailRecuperarUsuario = findViewById(R.id.etEmailRecuperarUsuario);
        etContraseñaRecuperarUsuario = findViewById(R.id.etContraseñaRecuperarUsuario);

        tvRespuestaRecuperarUsuario = findViewById(R.id.tvRespuestaRecuperarUsuario);
    }

    public void onClickRecuperar(View view) {
        //comprobar que sea igual al de la base de datos
        email = etEmailRecuperarUsuario.getText().toString();
        contraseña = etContraseñaRecuperarUsuario.getText().toString();

        if ("".equals(email)) {
            etEmailRecuperarUsuario.setError("Introduce tu email");
            return;
        }
        else if ("".equals(contraseña)) {
            etContraseñaRecuperarUsuario.setError("Introduce tu contraseña");
            return;
        }
        else {
            String nombreUsuario = sharedPref.getString("nombreUsuario","");

            database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            System.out.println(document.getId() + " => " + document.getData());
                            String dbEmail = document.getData().get("email").toString();
                            if (dbEmail.equals(email)) {
                                String dbPassword = document.getData().get("password").toString();
                                String usuario = document.getData().get("nombreUsuari").toString();
                                if (dbPassword.equals(contraseña) && dbEmail.equals(email)) {
                                    tvRespuestaRecuperarUsuario.setText("Tu nombre de usuario es: " + usuario);
                                }
                                else {
                                    tvRespuestaRecuperarUsuario.setText("Los datos no coinciden. Vuelva a intentarlo");
                                }

                            }
                        }
                    } else {
                        System.out.println("Error getting documents." + task.getException());
                    }
                }
            });
        }

    }
}