package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RecuperarContrasenaActivity extends AppCompatActivity {
    EditText etNombreUsuarioRecuperarContraseña, etNombreRecuperarContraseña, etApellidosRecuperarContraseña;
    TextView tvRespuestaRecuperarContraseña;
    EditText etdFechaNacimientoRecuperarContraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        etNombreUsuarioRecuperarContraseña = findViewById(R.id.etNombreUsuarioRecuperarContraseña);
        etNombreRecuperarContraseña = findViewById(R.id.etNombreRecuperarContraseña);
        etApellidosRecuperarContraseña = findViewById(R.id.etApellidosRecuperarContraseña);

        tvRespuestaRecuperarContraseña = findViewById(R.id.tvRespuestaRecuperarContraseña);

    }

    public void onClickRecuperar(View view) {
        //comprobar que sea igual al de la base de datos

        String nombrUsuario = etNombreUsuarioRecuperarContraseña.getText().toString();
        String nombre = etNombreRecuperarContraseña.getText().toString();
        String apellidos = etApellidosRecuperarContraseña.getText().toString();

        if ("".equals(nombrUsuario)) {
            etNombreUsuarioRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }
        else if ("".equals(nombre)) {
            etNombreRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }
        else if ("".equals(apellidos)) {
            etApellidosRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }

        //si coincide
        tvRespuestaRecuperarContraseña.setText("Tu contraseña es: " + "variable de contraseña");
        //si no
        tvRespuestaRecuperarContraseña.setText("Los datos no coinciden. Vuelva a intentarlo");
    }
}