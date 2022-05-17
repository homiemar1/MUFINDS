package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EditarPerfilActivity extends AppCompatActivity {
    Spinner sp_editar_genero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        sp_editar_genero = findViewById(R.id.sp_editar_genero);
        String[] datos = new String[] {"Mujer", "Hombre", "Prefiero no contestar", "No binario"};
        sp_editar_genero.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,datos));

    }

    public void onClickAceptar(View view) {
        finish();
    }
}