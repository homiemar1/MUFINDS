package com.example.mufinds;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.PreparedStatement;

public class PrincipalActivity extends AppCompatActivity {
    int condicion = 1;
    Button btEditarPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().show();

        btEditarPerfil = findViewById(R.id.btEditarPerfil);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_configuration:
                Intent intent  = new Intent(this, ConfiguracionActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_cambiarTema:
                Toast.makeText(this, "Has seleccionado cambiar tema", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_logout:
                Intent i1 = new Intent(this, IniciActivity.class);
                startActivity(i1);
                return true;

            case R.id.action_informacio:
                Toast.makeText(this, "Has seleccionado informacion", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_exit:
                finishAffinity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickEditarPerfil(View view) {
        if (condicion == 1) {
            Intent intent = new Intent(PrincipalActivity.this, MusicaActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(PrincipalActivity.this, EditarPerfilActivity.class);
            startActivity(intent);
        }

    }

    public void onClickAmistades(View view) {
        Intent intent = new Intent(PrincipalActivity.this, AmistadesActivity.class);
        startActivity(intent);
    }

    public void onClickMusica(View view) {
        condicion = 1;
        btEditarPerfil.setText("Gestionar Musica");
    }

    public void onClickPersonas (View view) {
        condicion = 2;
        btEditarPerfil.setText("Editar Perfil");
    }
}