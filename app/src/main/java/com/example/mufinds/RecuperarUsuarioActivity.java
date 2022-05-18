package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RecuperarUsuarioActivity extends AppCompatActivity {
    EditText etEmailRecuperarUsuario, etContraseñaRecuperarUsuario;
    TextView tvRespuestaRecuperarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_usuario);

        etEmailRecuperarUsuario = findViewById(R.id.etEmailRecuperarUsuario);
        etContraseñaRecuperarUsuario = findViewById(R.id.etContraseñaRecuperarUsuario);

        tvRespuestaRecuperarUsuario = findViewById(R.id.tvRespuestaRecuperarUsuario);
    }

    public void onClickRecuperar(View view) {
        //comprobar que sea igual al de la base de datos
        String email = etEmailRecuperarUsuario.getText().toString();
        String contraseña = etContraseñaRecuperarUsuario.getText().toString();

        if ("".equals(email)) {
            etEmailRecuperarUsuario.setError("Introduce tu email");
        }
        else if ("".equals(contraseña)) {
            etContraseñaRecuperarUsuario.setError("Introduce tu contraseña");
        }

        //si coincide
        tvRespuestaRecuperarUsuario.setText("Tu nombre de usuario es: " + "variable de usuario");
        //si no
        tvRespuestaRecuperarUsuario.setText("Los datos no coinciden. Vuelva a intentarlo");
    }
}