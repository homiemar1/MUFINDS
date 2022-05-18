package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextView tv_olvidado_contraseña, tv_olvidado_usuario;
    EditText editTextTextPersonName, etContraseñaIniciarSesion;
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

    }

    public void onClickIniciarSession (View view) {
        String nombreUsuario = editTextTextPersonName.getText().toString();
        String password = etContraseñaIniciarSesion.getText().toString();

        if ("".equals(nombreUsuario)){
            editTextTextPersonName.setError("Introduce tu nombre de usuario");
        }
        else if ("".equals(password)) {
            etContraseñaIniciarSesion.setError("Introduce tu contraseña");
        }
        else {
            //comprobar los datos

            //si coinciden
            Intent intent = new Intent(this, PrincipalActivity.class);
            startActivity(intent);
            finish();

            //si no coinciden
            //editTextTextPersonName.setError("Usuario o Password incorrecto");
            //etContraseñaIniciarSesion.setError("Usuario o Password incorrecto");

        }


    }

    public void onClickRecuperarContraseña (View view) {
        Intent intent = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
        intent.putExtra("confirmacon", 1);
        startActivity(intent);
    }

    public void onClickRecuperarUsuario (View view) {
        Intent intent = new Intent(LoginActivity.this, RecuperarUsuarioActivity.class);
        intent.putExtra("confirmacon", 2);
        startActivity(intent);
    }
}