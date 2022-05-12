package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class VistaPerfilActivity extends AppCompatActivity {
    ImageView ivEditarPerfilFoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_perfil);

        ivEditarPerfilFoto = findViewById(R.id.ivEditarPerfilFoto);
        ivEditarPerfilFoto.setImageResource(R.drawable.interrogante);
    }
}