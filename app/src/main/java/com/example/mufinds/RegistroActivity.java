package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegistroActivity extends AppCompatActivity {
    Spinner sp_genero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        sp_genero = findViewById(R.id.sp_genero);
        String[] datos = new String[] {"Hombre", "Mujer", "Prefiero no contestar", "No binario"};
        sp_genero.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,datos));

    }

    public void onClickSiguiente (View view) {
        Intent intent = new Intent(this, RegistroActivity2.class);
        startActivity(intent);
    }
}