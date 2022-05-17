package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfiguracionActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
    }

    public void onClickCambiarNombreUsuario (View view) {
        intent = new Intent(ConfiguracionActivity.this, CambiarUsuarioActivity.class);
        intent.putExtra("variable", 1);
        startActivity(intent);
    }

    public void onClickCambiarContraseña (View view) {
        intent = new Intent(ConfiguracionActivity.this, CambiarUsuarioActivity.class);
        intent.putExtra("variable", 2);
        startActivity(intent);
    }

    public void onClickEliminarCuenta  (View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Eliminar cuenta");
        alert.setMessage("Estas segur@ de que quieres eliminar tu cuenta? Esta acción será permanente").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alert.show();
    }
}