package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RecuperarContrasenaActivity extends AppCompatActivity {
    EditText etNombreUsuarioRecuperarContraseña, etNombreRecuperarContraseña, etApellidosRecuperarContraseña;
    TextView tvRespuestaRecuperarContraseña;
    EditText etdFechaNacimientoRecuperarContraseña;
    String nombreUsuario, nombre, apellidos;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
        database = FirebaseFirestore.getInstance();

        etNombreUsuarioRecuperarContraseña = findViewById(R.id.etNombreUsuarioRecuperarContraseña);
        etNombreRecuperarContraseña = findViewById(R.id.etNombreRecuperarContraseña);
        etApellidosRecuperarContraseña = findViewById(R.id.etApellidosRecuperarContraseña);

        tvRespuestaRecuperarContraseña = findViewById(R.id.tvRespuestaRecuperarContraseña);

    }

    public void onClickRecuperar(View view) {
        //comprobar que sea igual al de la base de datos

        nombreUsuario = etNombreUsuarioRecuperarContraseña.getText().toString();
        nombre = etNombreRecuperarContraseña.getText().toString();
        apellidos = etApellidosRecuperarContraseña.getText().toString();


        if ("".equals(nombreUsuario)) {
            etNombreUsuarioRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }
        else if ("".equals(nombre)) {
            etNombreRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }
        else if ("".equals(apellidos)) {
            etApellidosRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }
        else {
            database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            System.out.println(document.getId() + " => " + document.getData());
                            if (document.getId().equals(nombreUsuario)) {
                                String dbNombre = document.getData().get("nombre").toString();
                                String dbApellidos = document.getData().get("apellido").toString();
                                if (dbNombre.equals(nombre) && dbApellidos.equals(apellidos)) {
                                    tvRespuestaRecuperarContraseña.setText("Tu contraseña es: " + document.getData().get("password").toString());
                                }
                                else {
                                    tvRespuestaRecuperarContraseña.setText("Los datos no coinciden. Vuelva a intentarlo");
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