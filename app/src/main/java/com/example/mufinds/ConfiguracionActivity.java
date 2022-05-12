package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConfiguracionActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
    }

    public void onClickCambiarNombreUsuario (View view) {
        //intent = new Intent(ConfiguracionActivity.this, CambiarNombreUsuarioActivity.class);
        //startActivity(intent);
    }

    public void onClickCambiarContraseña (View view) {
        //intent = new Intent(ConfiguracionActivity.this, CambiarContraseñaActivity.class);
        //startActivity(intent);
    }

    public void onClickEliminarCuenta  (View view) {
        //intent = new Intent(ConfiguracionActivity.this, EliminarCuentaActivity.class);
        //startActivity(intent);
    }
}