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
        //intent = new Intent(ConfiguracionActivity.this, CambiarUsuarioActivity.class);
        //startActivity(intent);
    }

    public void onClickCambiarContraseña (View view) {
        //intent = new Intent(ConfiguracionActivity.this, CambiarContrasenaActivity.class);
        //startActivity(intent);
    }

    public void onClickEliminarCuenta  (View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Eliminar cuenta");
        alert.setMessage("Estas segur@ de que quieres eliminar tu cuenta? Esta acción será permanente");
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finishAffinity();
            }
        });
        alert.show();
    }
}