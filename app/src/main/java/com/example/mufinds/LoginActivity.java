package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_olvidado_contraseña, tv_olvidado_usuario;
    private EditText editTextTextPersonName, etContraseñaIniciarSesion;
    private FirebaseFirestore database;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String nombreUsuario, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_olvidado_contraseña = findViewById(R.id.tv_olvidado_contraseña);
        tv_olvidado_usuario = findViewById(R.id.tv_olvidado_usuario);
        tv_olvidado_contraseña.setPaintFlags(tv_olvidado_contraseña.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_olvidado_usuario.setPaintFlags(tv_olvidado_usuario.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        etContraseñaIniciarSesion = findViewById(R.id.etContraseñaIniciarSesion);

        database = FirebaseFirestore.getInstance();

        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void onClickIniciarSession (View view) {
        nombreUsuario = editTextTextPersonName.getText().toString();
        password = etContraseñaIniciarSesion.getText().toString();

        if ("".equals(nombreUsuario)){
            editTextTextPersonName.setError("Introduce tu nombre de usuario");
            return;
        }
        else if ("".equals(password)) {
            etContraseñaIniciarSesion.setError("Introduce tu contraseña");
            return;
        }
        password = EncriptarContraseña.encriptarMensaje(password);
        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean usuarioCheck = false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(nombreUsuario)) {
                                    usuarioCheck = true;
                                    String pwd = document.getData().get("password").toString();
                                    if (pwd.equals(password)) {
                                        guardarInformacionSharedPreference(nombreUsuario);
                                        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        editTextTextPersonName.setError("Usuario o contraseña incorrecto");
                                        etContraseñaIniciarSesion.setError("Usuario o contraseña incorrecto");
                                        return;
                                    }
                                }
                            }
                        }
                        if (!usuarioCheck) {
                            editTextTextPersonName.setError("Usuario o contraseña incorrecto");
                            etContraseñaIniciarSesion.setError("Usuario o contraseña incorrecto");
                            return;
                        }
                    }
                });
    }

    public void guardarInformacionSharedPreference (String nombreUsuario) {
        Task<QuerySnapshot> task = database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(nombreUsuario)){
                            String nombreUsuario = document.getId();
                            String nombre = document.getData().get("nombre").toString();
                            String apellidos = document.getData().get("apellido").toString();
                            String fechaNacimiento = document.getData().get("dataNaixement").toString();
                            String pwd = document.getData().get("password").toString();
                            String descripcion = document.getData().get("descripcion").toString();
                            String email = document.getData().get("email").toString();
                            String genero = document.getData().get("genero").toString();
                            String fotoPerfil = document.getData().get("fotoPerfil").toString();
                            String instagram = document.getData().get("instagram").toString();

                            editor.putString("nombre", nombre);
                            editor.putString("apellido", apellidos);
                            editor.putString("email", email);
                            editor.putString("password", pwd);
                            editor.putString("genero", genero);
                            editor.putString("descripcion", descripcion);
                            editor.putString("nombreUsuario", nombreUsuario);
                            editor.putString("idFoto", fotoPerfil);
                            editor.putString("fechaNacimiento", fechaNacimiento);
                            editor.putString("instagram", instagram);
                            editor.commit();
                        }
                    }
                }
            }
        });
        while (!task.isComplete()){};
    }

    public void onClickRecuperarContraseña (View view) {
        Intent intent = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
        startActivity(intent);
    }

    public void onClickRecuperarUsuario (View view) {
        Intent intent = new Intent(LoginActivity.this, RecuperarUsuarioActivity.class);
        startActivity(intent);
    }
}